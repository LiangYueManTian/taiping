package com.taiping.biz.maintenanceplan.job;

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
 * @since 2019/11/18
 * 维保延期时间更新启动类
 */
@Component
@Order(value = 3)
public class MaintenanceDelayTimeRunner implements ApplicationRunner {
    /**
     * 任务管理
     */
    @Autowired
    private JobService jobService;

    /**
     * 定时任务名称
     */
    private static final String JOB_NAME = "MAINTENANCE_DELAY_TIME";

    /**
     * 定时任务触发器名称
     */
    private static final String TRIGGER_NAME = "maintenanceDelayTime";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //根据任务名称查询任务是否存在
        List<TaskProcess> taskInfoList = jobService.queryTasklist();
        if (!ObjectUtils.isEmpty(taskInfoList)) {
            for (TaskProcess taskInfoOne : taskInfoList) {
                if (MaintenanceDelayTimeRunner.JOB_NAME.equals(taskInfoOne.getJobName())) {
                    //已存在任务，删除任务
                    jobService.deleteJob(JOB_NAME, JobGroupEnum.MAINTENANCE_DELAY_TIME);
                }
            }
        }

        //新增任务
        TaskProcess taskInfo = new TaskProcess();
        taskInfo.setJobGroupEnum(JobGroupEnum.MAINTENANCE_DELAY_TIME);
        taskInfo.setJobName(MaintenanceDelayTimeRunner.JOB_NAME);
        taskInfo.setTriggerGroup(JobTriggerEnum.MAINTENANCE_DELAY_TIME);
        taskInfo.setTClass(MaintenanceDelayTimeJob.class);
        taskInfo.setCron(JobCronEnum.MONDAY_ZERO_CLOCK);
        taskInfo.setTriggerName(MaintenanceDelayTimeRunner.TRIGGER_NAME);
        taskInfo.setOnce(false);
        taskInfo.setCreateTime(System.currentTimeMillis());
        jobService.addJob(taskInfo);
    }
}
