package com.taiping.biz.riskmanage.job;

import com.taiping.biz.riskmanage.service.IRiskManageService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author zhangliangyu
 * @since 2019/10/30
 * 风险超时时间更新
 */
@Slf4j
public class RiskTimeoutTimeJob extends QuartzJobBean {

    @Autowired
    private IRiskManageService riskManageService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        riskManageService.updateRiskTimeoutTime();
    }
}
