package com.mwj.cms.framework.mybatis.page;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mwj.cms.common.constant.Const;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author Meng Wei Jin
 * @description 需要在layui的前台设置请求参数名和返回的数据的参数名根当前类对应
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Component
public class MyPage<T> extends Page<T> {

    private static final long serialVersionUID = -5428777368936766478L;

    /**
     * 默认当前页
     */
    public static final long DEFAULT_PAGE = 1;

    /**
     * 默认每页展示数目
     */
    public static final long DEFAULT_LIMIT = 10;

    /**
     * 默认数据结果总数，当 count 不为 0 时分页插件不会进行 count 查询
     */
    public static final long DEFAULT_COUNT = 0;

    /**
     * 常数变量名 layui当前页
     */
    public static final String VAR_PAGE = "page";

    /**
     * 常数变量名 layui每页展示数目
     */
    public static final String VAR_LIMIT = "limit";

    /**
     * 常数变量名 layui数据结果总数
     */
    public static final String VAR_COUNT = "count";

    /**
     * 常数变量名 mybatis-plus当前页
     */
    public static final String VAR_CURRENT = "current";

    /**
     * 常数变量名 mybatis-plus每页展示数目
     */
    public static final String VAR_SIZE = "size";

    /**
     * 常数变量名 mybatis-plus数据结果总数
     */
    public static final String VAR_TOTAL = "total";

    /**
     * 当前页
     */
    private long page = DEFAULT_PAGE;

    /**
     * 每页显示条数，默认 10
     */
    private long limit = DEFAULT_LIMIT;

    /**
     * 总数，当 count 不为 0 时分页插件不会进行 count 查询
     */
    private long count = DEFAULT_COUNT;

    /**
     * 查询数据列表 Layui table返回对象
     */
    private List<T> data = Collections.emptyList();

    /**
     * 查询数据列表 Bootstrap table返回对象
     */
    private List<T> rows = Collections.emptyList();


    /**
     * 成功的状态码，默认 0 为成功
     */
    private int code = 0;

    /**
     * 状态信息
     */
    private String msg = Const.EMPTY;

    /**
     * 手动分页
     * @param page
     * @param list
     * @param <E>
     * @return
     */
    public static <E> IPage<E> handPage(IPage<E> page, List<E> list){
        if(list == null){
            list = Collections.EMPTY_LIST;
        } else {
            page.setTotal((long)list.size());
        }

        int current = (int)page.getCurrent();
        int size = (int)page.getSize();

        // 截取的开始位置
        int startIndex = (current - 1) * size;
        // 截取的结束位置
        int endIndex = current * size;
        endIndex = list.size() > endIndex ? endIndex : (list.size() % size + startIndex);

        list = list.subList(startIndex, endIndex);
        page.setRecords(list);
        return page;
    }
}
