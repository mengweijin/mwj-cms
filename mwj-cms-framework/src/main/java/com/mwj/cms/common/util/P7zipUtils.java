package com.mwj.cms.common.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.system.OsInfo;
import com.mwj.cms.common.constant.Const;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * 压缩/解压缩工具类，使用7z
 * linux需要安装p7zip
 * windows需存在7z.exe
 *
 * @author mengweijin
 */
public class P7zipUtils {
	private static final Log logger = LogFactory.getLog(P7zipUtils.class);
	
	private final static String CMD_PREFIX_WINDOWS = Const.PROJECT_PATH + File.separator + "files/7z/7z.exe";
	
	private final static String CMD_PREFIX_LINUX = "7za";

	/**
	 * 压缩/解压缩命令前缀，根据操作系统类型决定
	 */
	private final static String commandPrefix = new OsInfo().isWindows() ? CMD_PREFIX_WINDOWS : CMD_PREFIX_LINUX;

	public static void compress(List<String> filePathList, String destPath) throws IOException, InterruptedException {
		compress(filePathList, null, destPath);
	}

	/**
	 * 压缩
	 * @param filePathList 需要压缩在一起的所有文件的绝对路径的集合
	 * @param password 压缩密码
	 */
	public static void compress(List<String> filePathList, String password, String destPath) throws IOException, InterruptedException {
        StringBuilder srcPath = new StringBuilder();
        for (String path : filePathList) {
        	srcPath.append(path).append(" ");
		}
        srcPath.deleteCharAt(srcPath.length() - 1);
        
        File dir = FileUtils.getFile(destPath);
        if(dir.exists()) {
        	FileUtils.forceDelete(dir);
        } 
        
        String command;
        if(password == null) {
			command = String.format("%s a -r %s %s",
				commandPrefix,
    			destPath,
    			srcPath.toString()); 
        } else {
			command = String.format("%s a -p%s -r %s %s",
				commandPrefix,
    			password,
    			destPath,
    			srcPath.toString()); 
        }

		Process process = Runtime.getRuntime().exec(command);

		// 读取子线程输入流和错误流
		ProcessUtils.getInstance().readCache(process);
	}

	public static String decompress(String filePath) throws IOException, InterruptedException {
		return decompress(filePath, null);
	}

	/**
	 * 解压
	 * @param filePath 压缩包绝对路径
	 * @param password 解压密码
	 * @return 解压到的目标路径
	 */
	public static String decompress(String filePath, String password) throws IOException, InterruptedException {
		Assert.notNull(filePath, "this argument is required; it must not be null!");
		File zipFile = new File(filePath);
        if (!zipFile.exists()) {  
            throw new FileNotFoundException(filePath);
        }
        String zipFileName = zipFile.getName();
		zipFileName = zipFileName.substring(0, zipFileName.lastIndexOf(Const.DOT));
        String destPath = zipFile.getParent() + File.separator + zipFileName;
        
        File dir = new File(destPath);
        if(dir.exists()) {
			FileUtils.cleanDirectory(dir);
        } else {
        	FileUtils.forceMkdir(dir);
        }
  
        String command;
        if(password == null) {
			command = String.format("%s x -aoa \"%s\" -o\"%s\"",
				commandPrefix,
    			zipFile.getAbsolutePath(), 
    			destPath);  
        } else {
			command = String.format("%s x -p%s -aoa \"%s\" -o\"%s\"",
				commandPrefix,
    			password,
    			zipFile.getAbsolutePath(), 
    			destPath);  
        }

		Process process = Runtime.getRuntime().exec(command);

		// 读取子线程输入流和错误流
		ProcessUtils.getInstance().readCache(process);
		return destPath;
	}

}
