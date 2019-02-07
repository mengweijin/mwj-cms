package com.mwj.cms.framework.mail;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author Meng Wei Jin
 */
@Data
public class MailInfo {

    /** 邮箱标题，必填项 */
    private String title;
	
	/** 邮件内容，必填项 */
	private String content;
	
	/** 接收邮箱 必填项 */
	private String[] toAddress;

	/** 抄送邮箱 */
	private String[] ccAddress;
	
	/** 密送邮箱 */
	private String[] bccAddress;
	
	/** 附件绝对路径集合 */
	private List<String> attachment;

	/** thymeleaf邮件模板相对路径，配置示例："mail/logError" */
	private String template;

	/** thymeleaf邮件模板中变量的值 */
	private Map<String, Object> data;

}
