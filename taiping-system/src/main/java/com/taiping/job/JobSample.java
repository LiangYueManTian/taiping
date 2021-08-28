package com.taiping.job;

import com.alibaba.fastjson.JSON;
import com.taiping.constant.AppConstant;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDateTime;

/**
 * test 定时任务demo
 *
 * @author liyj
 * @date 2019/9/20
 */
@Slf4j
public class JobSample extends QuartzJobBean {

    /**
     * 任务的执行类
     *
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Object param = jobExecutionContext.getMergedJobDataMap().get(AppConstant.QUARTZ_PARAM_KEY);
        log.info("获取的数据:" + JSON.toJSONString(param));
        log.info("执行时间：" + LocalDateTime.now());
    }
}
