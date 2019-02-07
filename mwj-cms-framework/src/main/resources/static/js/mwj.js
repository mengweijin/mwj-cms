/**
 * 依赖jquery, layer
 */
const mwj = {

    /**
     * 禁用所有表单元素
     * @param selector jquery选择器，示例：#content
     */
    disableForm : function(selector){
        // *：选择元素下所有元素； unbind(): 解绑所有事件 这里其实并不需要unbind也可以同样的效果。
        $(selector + " *").attr("disabled", true).unbind();
    },

    /**
     * 注意：要使用empty()方法清理对象，否则可能导致内存泄漏
     * 并且所加载的页面中的js一定要写在$(function(){......});中，否则可能导致变量重复定义，控制台发生异常。
     * @param selector jquery选择器，示例：#content
     * @param url
     * @param params
     */
    loadPage : function(selector, url, params){
        let $selector = $(selector).empty();
        $.get(url, params, function (data) {
            $selector.html(data);
        });
    },

    /**
     * 从父页面关闭layer弹窗
     * 注意：window.parent和parent是有区别的
     */
    closeLayer : function(){
        //先得到当前iframe层的索引
        let index = parent.layer.getFrameIndex(window.name);
        //再执行关闭弹出层
        parent.layer.close(index);
        // 重载整个父页面
        // parent.location.reload();
    },

    msg : function (result) {
        if(result.code === 0){
            mwj.success(result.msg);
        } else {
            mwj.error(result.msg);
        }
    },
    /**
     * result.code=0成功，否则失败
     * result.msg展示的信息。
     */
    msgAndLoadPage : function (result, selector, url, params) {
        if(result.code === 0){
            mwj.success(result.msg);
            mwj.loadPage(selector, url, params);
        } else {
            mwj.error(result.msg);
        }
    },
    info: function(msg){
        layer.msg(msg, {});
    },
    warn: function(msg){
        msg = msg === undefined ? '警告！' : msg;
        layer.msg(msg, {icon: 0});
    },
    success: function(msg){
        msg = msg === undefined ? '成功！' : msg;
        layer.msg(msg, {icon: 1});
    },
    error: function(msg){
        msg = msg === undefined ? '失败！' : msg;
        layer.msg(msg, {icon: 2});
    },
    unknow: function(msg){
        msg = msg === undefined ? '未知！' : msg;
        layer.msg(msg, {icon: 3});
    },
    locked: function(msg){
        msg = msg === undefined ? '已锁定！' : msg;
        layer.msg(msg, {icon: 4});
    },
    cry: function(msg){
        msg = msg === undefined ? '不开心！' : msg;
        layer.msg(msg, {icon: 5});
    },

    /**
     * layui表单必填项label添加红色星号
     */
    addRedStar : function () {
        let $labels = $("[lay-verify*='required']").parent("div").siblings("label.layui-form-label");
        $labels.each(function(index, item){
            if(!$(item).hasClass("mwj-red-star")){
                $(item).addClass("mwj-red-star")
                       .append($("<strong style='color: red; font-size: 20px;'>*</strong>"));
            }
        });
    },

    /**
     * @params dataList List<Object>或者List<Map<String, Object>>数据
     * @params rootId 根值，树图或者级联数据最顶层的数据的id
     */
    buildTreeData: function (dataList, rootId){
        let itemArr = [];
        for(let i = 0; i < dataList.length; i++){
            let node = dataList[i];
            if(node.parentId === rootId){
                let newNode = {id : node.id, name : node.name, children : mwj.buildTreeData(dataList, node.id)};
                itemArr.push(newNode);
            }
        }
        return itemArr;
    }
};

var HtmlUtil = {
    /*1.用浏览器内部转换器实现html转码*/
    htmlEncode:function (html){
        //1.首先动态创建一个容器标签元素，如DIV
        let temp = document.createElement ("div");
        //2.然后将要转换的字符串设置为这个元素的innerText(ie支持)或者textContent(火狐，google支持)
        (temp.textContent != undefined ) ? (temp.textContent = html) : (temp.innerText = html);
        //3.最后返回这个元素的innerHTML，即得到经过HTML编码转换的字符串了
        let output = temp.innerHTML;
        temp = null;
        return output;
    },
    /*2.用浏览器内部转换器实现html解码*/
    htmlDecode:function (text){
        //1.首先动态创建一个容器标签元素，如DIV
        let temp = document.createElement("div");
        //2.然后将要转换的字符串设置为这个元素的innerHTML(ie，火狐，google都支持)
        temp.innerHTML = text;
        //3.最后返回这个元素的innerText(ie支持)或者textContent(火狐，google支持)，即得到经过HTML解码的字符串了。
        let output = temp.innerText || temp.textContent;
        temp = null;
        return output;
    },
    /*3.用正则表达式实现html转码*/
    htmlEncodeByRegExp:function (str){
        let s = "";
        if(str.length == 0) {
            return s;
        }
        s = str.replace(/&/g,"&amp;");
        s = s.replace(/</g,"&lt;");
        s = s.replace(/>/g,"&gt;");
        s = s.replace(/ /g,"&nbsp;");
        s = s.replace(/'/g,"&#39;");
        s = s.replace(/"/g,"&quot;");
        return s;
    },
    /*4.用正则表达式实现html解码*/
    htmlDecodeByRegExp:function (str){
        let s = "";
        if(str.length == 0) {
            return s;
        }
        s = str.replace(/&amp;/g,"&");
        s = s.replace(/&lt;/g,"<");
        s = s.replace(/&gt;/g,">");
        s = s.replace(/&nbsp;/g," ");
        s = s.replace(/&#39;/g,"\'");
        s = s.replace(/&quot;/g,"\"");
        return s;
    }
};

/**
 * 日期格式化
 * @type {{format1: (function(*): (*|void|string)), format2: (function(*): (*|void|string)), format3: (function(*): (*|void|string)), format4: (function(*): (*|void|string))}}
 */
const dateUtil = {
    format1: function (dateString) {
        let pattern = /(\d{4})-(\d{2})-(\d{2}) (\d{2}):(\d{2}):(\d{2})/;
        return dateString.replace(pattern, '$1年$2月$3日 $4时$5分$6秒');
    },
    format2: function (dateString) {
        let pattern = /(\d{4})-(\d{2})-(\d{2}) (\d{2}):(\d{2}):(\d{2})/;
        return dateString.replace(pattern, '$1年$2月$3日');
    },
    format3: function (dateString) {
        let pattern = /(\d{4})(\d{2})(\d{2})(\d{2})(\d{2})(\d{2})/;
        return dateString.replace(pattern, '$1-$2-$3 $4:$5:$6');
    },
    format4: function (dateString) {
        let pattern = /(\d{4})年(\d{2})月(\d{2})日 (\d{2})时(\d{2})分(\d{2})秒/;
        return dateString.replace(pattern, '$1-$2-$3 $4:$5:$6');
    }

};

/**
 * jquery ajax 全局配置
 */
$.ajaxSetup({
    layerIndex: -1,
    cache: false,
    beforeSend: function(xhr) {
        this.layerIndex = layer.load(2, { shade: [0.5, '#393D49'] });
        let header = $("meta[name='_csrf_header']").attr("content");
        let token =$("meta[name='_csrf']").attr("content");
        xhr.setRequestHeader(header, token);
    },
    complete: function (xhr) {
        layer.close(this.layerIndex);
    },
    error : function(xhr, textStatus, errorThrown) {
        switch (xhr.status) {
            case (500):
                mwj.error('服务器异常!');
                break;
            case (401):
                mwj.error('未登录或Session失效!');
                // 刷新当前页面
                window.location.reload();
                break;
            case (403):
                mwj.error('无权限执行此操作!');
                break;
            case (408):
                mwj.error('请求超时!');
                break;
            default:
                mwj.error('未知错误,请联系管理员!');
                break;
        }
    }
});


