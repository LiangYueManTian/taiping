package com.taiping.biz.riskmanage.job;

import com.taiping.bean.TaskProcess;
import com.taiping.enums.job.JobCronEnum;
import com.taiping.enums.job.JobGroupEnum;
import com.taiping.enums.job.JobTriggerEnum;
import com.taiping.job.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @author zhangliangyu
 * @since 2019/10/30
 * 风险超时时间更新启动类
 */
@Component
@Order(value = 1)
public class RiskTimeoutTimeRunner implements ApplicationRunner {

    @Autowired
    private JobService jobService;

    /**
     * 定时任务名称
     */
    private static final String JOB_NAME = "RISK_TIMEOUT_TIME";

    /**
     * 定时任务触发器名称
     */
    private static final String TRIGGER_NAME = "riskTimeoutTime";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //根据任务名称查询任务是否存在
        List<TaskProcess> taskInfoList = jobService.queryTasklist();
        if (!ObjectUtils.isEmpty(taskInfoList)) {
            for (TaskProcess taskInfoOne : taskInfoList) {
                if (RiskTimeoutTimeRunner.JOB_NAME.equals(taskInfoOne.getJobName())) {
                    //已存在任务，删除任务
                    jobService.deleteJob(JOB_NAME, JobGroupEnum.RISK_TIMEOUT_TIME);
                }
            }
        }

        //新增任务
        TaskProcess taskInfo = new TaskProcess();
        taskInfo.setJobGroupEnum(JobGroupEnum.RISK_TIMEOUT_TIME);
        taskInfo.setJobName(RiskTimeoutTimeRunner.JOB_NAME);
        taskInfo.setTriggerGroup(JobTriggerEnum.RISK_TIMEOUT_TIME);
        taskInfo.setTClass(RiskTimeoutTimeJob.class);
        taskInfo.setCron(JobCronEnum.EVERY_DAY_AT_O_CLOCK);
        taskInfo.setTriggerName(RiskTimeoutTimeRunner.TRIGGER_NAME);
        taskInfo.setOnce(false);
        taskInfo.setCreateTime(System.currentTimeMillis());
        jobService.addJob(taskInfo);
    }
}
