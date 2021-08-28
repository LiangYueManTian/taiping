package com.taiping.biz.maintenanceplan.job;

import com.taiping.biz.maintenanceplan.service.IMaintenancePlanService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author zhangliangyu
 * @since 2019/11/6
 * 维保预提醒
 */
public class MaintenanceReminderJob extends QuartzJobBean {
    /**
     * 维护保养计划逻辑层
     */
    @Autowired
    private IMaintenancePlanService maintenancePlanService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        maintenancePlanService.maintenancePreReminder();
    }
}
