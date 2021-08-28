package com.taiping.biz.manage.job;

import com.taiping.biz.manage.service.ManageActivityService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 运维管理活动提醒
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-27
 */
public class ManageReminderJob extends QuartzJobBean {
    /**
     * 维护保养计划逻辑层
     */
    @Autowired
    private ManageActivityService manageActivityService;

    /**
     * 任务执行逻辑
     * @param jobExecutionContext 任务参数
     * @throws JobExecutionException 异常信息
     */
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        manageActivityService.reminderManageActivity();
    }
}
