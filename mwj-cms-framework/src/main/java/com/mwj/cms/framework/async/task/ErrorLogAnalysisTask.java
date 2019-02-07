package com.mwj.cms.framework.async.task;

import com.mwj.cms.common.constant.Const;
import com.mwj.cms.common.util.P7zipUtils;
import com.mwj.cms.common.util.RegexUtils;
import com.mwj.cms.common.util.date.DateFormatUtil;
import com.mwj.cms.framework.mail.MailInfo;
import com.mwj.cms.framework.mail.MailService;
import com.mwj.cms.framework.util.MessageSourceUtils;
import lombok.Data;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

/**
 * @author Meng Wei Jin
 * @description
 **/
@Component
public class ErrorLogAnalysisTask {

    private final Log logger = LogFactory.getLog(this.getClass());

    public static final String LOG_PATH = Const.PROJECT_PATH + File.separator + "logs";

    public static final String LOG_FILE_NAME = "app.log";

    public static final String LOG_FILE_SUFFIX = ".gz";

    public static final String LOG_MAIL_TEMPLATE = "mail/errorLog";

    @Autowired
    private MailService mailService;

    @Autowired
    private MailProperties mailProperties;

    /** 正则匹配，如：2018-05-23 10:14:21.007 ERROR 13128 --- 会被匹配到 */
    private static String REGEX_ERROR = "^\\d{4}\\-\\d{1,2}\\-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}.\\d{1,3} ERROR \\d{1,10} \\-{3} ";

    private static String REGEX_ALL = "^\\d{4}\\-\\d{1,2}\\-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}.\\d{1,3} (ERROR|DEBUG|INFO|WARN) \\d{1,10} \\-{3} ";

    public void execute() throws Exception {

        Date yesterday = DateUtils.addDays(new Date(), -1);
        String yesterdayStr = DateFormatUtil.format(yesterday, DateFormatUtil.YYYY_MM_DD);

        File rootFile = new File(LOG_PATH);

        // lambda表达式，过滤符合日志要求的的日志文件
        File[] files = rootFile.listFiles((dir, name) -> {
            if(name.startsWith(LOG_FILE_NAME + Const.DOT + yesterdayStr) && name.endsWith(LOG_FILE_SUFFIX)){
                return true;
            }
            return false;
        });

        List<ErrorLog> errorLogList = new ArrayList<>();
        List<String> attachmentList = new ArrayList<>();
        List<String> decompressDirList = new ArrayList<>();
        if(files.length > 0 ){
            File decompressDir;
            File[] logFiles;
            for (File file: files){
                // 解压
                decompressDir = new File(P7zipUtils.decompress(file.getAbsolutePath()));
                decompressDirList.add(decompressDir.getAbsolutePath());
                logFiles = decompressDir.listFiles();
                if(logFiles.length > 0){
                    // 读取日志文件
                    errorLogList.addAll(readLogFile(logFiles[0]));
                    attachmentList.add(logFiles[0].getAbsolutePath());
                } else {
                    continue;
                }
            }
        }

        // 邮件推送给管理员
        MailInfo mailInfo = buildMailInfo(yesterdayStr, errorLogList, attachmentList);

        mailService.sendMailHtml(mailInfo);

        for (String decompressDirPath: decompressDirList){
            // 清理解压的临时文件
            FileUtils.forceDelete(FileUtils.getFile(decompressDirPath));
        }
    }

    /**
     * 读取日志文件，返回日志文件绝对路径，行号和简单的错误信息。
     * @param file
     * @return
     * @throws IOException
     */
    private List<ErrorLog> readLogFile(File file) throws IOException {
        List<ErrorLog> ErrorLogList = new ArrayList<>();

        // jdk1.7新特性 自动关闭流资源
        try(Reader in = new FileReader(file);
            BufferedReader br = new BufferedReader(in)) {

            String str;
            // 行号
            long lineNumber = 1;
            ErrorLog errorLog;
            while ((str = br.readLine()) != null) {
                if(RegexUtils.regexCheck(str, REGEX_ERROR)){
                    errorLog = new ErrorLog();
                    errorLog.setFileName(file.getName());
                    errorLog.setLineNumber(lineNumber);
                    errorLog.setErrorMsg(str);
                    ErrorLogList.add(errorLog);
                }
                lineNumber++;
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new IOException(e);
        }

        return ErrorLogList;
    }

    /**
     * 构建邮件信息
     * @param dateStr
     * @param errorLogList
     * @param attachmentList
     * @return
     */
    private MailInfo buildMailInfo(String dateStr,
                                   List<ErrorLog> errorLogList,
                                   List<String> attachmentList){
        MailInfo mailInfo = new MailInfo();
        mailInfo.setTitle(MessageSourceUtils.message("sysLog.system.error.analysis", dateStr));
        mailInfo.setToAddress(new String[]{mailProperties.getUsername()});
        if(errorLogList.isEmpty()){
            mailInfo.setContent(MessageSourceUtils.message("sysLog.system.error.not.found"));
        } else {
            mailInfo.setTemplate(LOG_MAIL_TEMPLATE);
        }
        Map<String, Object> dataMap = new HashMap<>(1);
        dataMap.put("errorLogList", errorLogList);
        mailInfo.setData(dataMap);
        mailInfo.setAttachment(attachmentList);

        return mailInfo;
    }

    /**
     * 私有内部类，错误日志记录对象
     */
    @Data
    private class ErrorLog{
        
        private String fileName;

        private Long lineNumber;

        private String errorMsg;
    }
}
