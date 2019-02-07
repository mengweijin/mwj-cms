package com.mwj.cms.framework.quartz.task;

import com.mwj.cms.system.entity.Job;
import org.quartz.JobExecutionException;

import java.io.Serializable;

/**
 * @author mengweijin
 */
public interface QuartzTask<T> extends Serializable {

    /**
     * 执行方法
     * @param job 方法参数
     */
    void execute(Job<T> job) throws JobExecutionException;

    /**
     * 后处理方法，在execute方法之后执行
     * @param job 方法参数
     */
    void after(Job<T> job) throws JobExecutionException;
}
