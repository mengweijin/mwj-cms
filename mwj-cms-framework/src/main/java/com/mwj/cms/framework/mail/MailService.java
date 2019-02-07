package com.mwj.cms.framework.mail;

import com.mwj.cms.common.constant.Const;
import com.mwj.cms.framework.AppSupport;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.Date;
import java.util.Map;

/**
 * @author Meng WEi Jin
 * @description
 **/
@Component
public class MailService {

    private static final Log logger = LogFactory.getLog(MailService.class);

    private static final boolean IS_MULTIPART = true;

    @Autowired
    private MailProperties mailProperties;

    @Autowired
    private JavaMailSender javaMailSender;

    @Resource
    private TemplateEngine templateEngine;

    @Autowired
    private AppSupport appSupport;

    public void sendMailHtml(MailInfo mailInfo) throws MessagingException {
        sendMail(mailInfo, true);
    }

    public void sendMailText(MailInfo mailInfo) throws MessagingException {
        sendMail(mailInfo, false);
    }

    /**
     * 发送文本/HTML邮件
     * @param mailInfo
     */
    private void sendMail(MailInfo mailInfo, boolean isHtml) throws MessagingException {
        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, IS_MULTIPART, Const.UTF_8);
            // 设置邮件发送信息
            composeMessageHeader(messageHelper, mailInfo);
            // 设置邮件正文
            if(!StringUtils.isEmpty(mailInfo.getTemplate())){
                // 使用thymeleaf模板引擎
                mailInfo.setContent(buildMessage(mailInfo.getTemplate(), mailInfo.getData()));
            }
            messageHelper.setText(mailInfo.getContent(), isHtml);

            // 非生产环境打开调试
            if(!appSupport.isProd()){
                mimeMessage.getSession().setDebug(true);
            }
        };

        logger.debug(mailInfo);
        logger.debug("准备发送邮件.....");

        javaMailSender.send(mimeMessagePreparator);

        logger.debug("邮件发送完成！");
    }

    /**
     * 设置邮件发送信息
     * @param messageHelper
     * @param mailInfo
     * @throws MessagingException
     */
    private void composeMessageHeader(MimeMessageHelper messageHelper, MailInfo mailInfo) throws MessagingException {
        // 发件人邮箱
        messageHelper.setFrom(mailProperties.getUsername());
        // 收件人
        messageHelper.setTo(mailInfo.getToAddress());
        // 抄送人
        if(mailInfo.getCcAddress() != null) {
            messageHelper.setCc(mailInfo.getCcAddress());
        }
        // 密送人
        if(mailInfo.getBccAddress() != null) {
            messageHelper.setBcc(mailInfo.getBccAddress());
        }
        // 设置邮件主题
        messageHelper.setSubject(mailInfo.getTitle());
        // 设置邮件发送时间
        messageHelper.setSentDate(new Date());
        // 设置附件
        if (mailInfo.getAttachment() != null) {
            FileSystemResource file;
            for (String filename : mailInfo.getAttachment()) {
                file = new FileSystemResource(filename);
                messageHelper.addAttachment(file.getFilename(), file);
            }
        }
    }

    /**
     * 使用thymeleaf邮件模板
     * @param templateName 模板名称
     * @param data 要替换的模板中的数据
     * @return
     */
    private String buildMessage(String templateName, Map<String, Object> data) {
        Context context = new Context();
        if(data != null){
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                context.setVariable(entry.getKey(), entry.getValue());
            }
        }
        return templateEngine.process(templateName, context);
    }

}
