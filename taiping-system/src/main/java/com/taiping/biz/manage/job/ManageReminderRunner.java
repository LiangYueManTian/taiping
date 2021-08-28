package com.taiping.biz.manage.job;

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
 * 运维管理活动提醒启动类
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-27
 */
@Component
@Order(value = 4)
public class ManageReminderRunner implements ApplicationRunner {
    /**
     * 任务管理
     */
    @Autowired
    private JobService jobService;

    /**
     * 定时任务名称
     */
    private static final String JOB_NAME = "MANAGE_REMINDER";

    /**
     * 定时任务触发器名称
     */
    private static final String TRIGGER_NAME = "manageReminder";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //根据任务名称查询任务是否存在
        List<TaskProcess> taskInfoList = jobService.queryTasklist();
        if (!ObjectUtils.isEmpty(taskInfoList)) {
            for (TaskProcess taskInfoOne : taskInfoList) {
                if (ManageReminderRunner.JOB_NAME.equals(taskInfoOne.getJobName())) {
                    //已存在任务，删除任务
                    jobService.deleteJob(JOB_NAME, JobGroupEnum.MANAGE_REMINDER);
                }
            }
        }

        //新增任务
        TaskProcess taskInfo = new TaskProcess();
        taskInfo.setJobGroupEnum(JobGroupEnum.MANAGE_REMINDER);
        taskInfo.setJobName(ManageReminderRunner.JOB_NAME);
        taskInfo.setTriggerGroup(JobTriggerEnum.MANAGE_REMINDER);
        taskInfo.setTClass(ManageReminderJob.class);
        taskInfo.setCron(JobCronEnum.EVERY_DAY_AT_ONE_O_CLOCK);
        taskInfo.setTriggerName(ManageReminderRunner.TRIGGER_NAME);
        taskInfo.setOnce(false);
        taskInfo.setCreateTime(System.currentTimeMillis());
        jobService.addJob(taskInfo);
    }
}
