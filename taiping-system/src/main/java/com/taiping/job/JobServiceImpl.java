package com.taiping.job;

import com.taiping.bean.TaskProcess;
import com.taiping.constant.AppConstant;
import com.taiping.enums.job.JobCronEnum;
import com.taiping.enums.job.JobGroupEnum;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 任务帮助类
 * 参考filink Schedule
 *
 * @author liyj
 * @date 2019/9/20
 */
@Service
@Slf4j
public class JobServiceImpl implements JobService {

    /**
     * 自动注入 taipingScheduler
     */
    @Autowired
    private Scheduler taipingScheduler;

    /**
     * 查询任务并分组
     *
     * @return
     */
    @Override
    public List<TaskProcess> queryTasklist() {
        List<TaskProcess> list = new ArrayList<>();
        try {
            for (String groupJob : taipingScheduler.getJobGroupNames()) {
                for (JobKey jobKey : taipingScheduler.getJobKeys(GroupMatcher.<JobKey>groupEquals(groupJob))) {
                    List<? extends Trigger> triggers = taipingScheduler.getTriggersOfJob(jobKey);
                    for (Trigger trigger : triggers) {
                        JobDetail jobDetail = taipingScheduler.getJobDetail(jobKey);
                        String cronExpression = "";
                        if (trigger instanceof CronTrigger) {
                            CronTrigger cronTrigger = (CronTrigger) trigger;
                            cronExpression = cronTrigger.getCronExpression();
                        }
                        TaskProcess info = new TaskProcess();
                        info.setJobName(jobKey.getName());
                        info.setJobDesc(jobDetail.getDescription());
                        info.setJobGroupEnum(JobGroupEnum.getJobGroupEnumByStr(jobKey.getGroup()));
                        info.setCron(JobCronEnum.getEnumByStr(cronExpression));
                        list.add(info);
                    }
                }
            }
        } catch (SchedulerException e) {
            log.error("查询任务列表异常", e);
        }
        return list;
    }

    /**
     * 新增任务
     *
     * @throws SchedulerException
     */
    @Override
    public void addJob(TaskProcess process) throws SchedulerException {
        //新增任务
        this.addJobProcess(process);
    }

    /**
     * 修改任务
     *
     * @param process
     */
    @Override
    public void updateJob(TaskProcess process) {
        String jobName = process.getJobName();
        JobGroupEnum jobGroup = process.getJobGroupEnum();
        // TODO: 2019/9/20 这里还需要验证
        try {
            if (!checkExists(jobName, jobGroup)) {
                log.info("修改定时任务失败，", jobGroup, jobName);
            }
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup.getGroupName());
            JobKey jobKey = new JobKey(jobName, jobGroup.getGroupName());

            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder
                    .cronSchedule(process.getCron().getCron());

            CronTrigger cronTrigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity(triggerKey)
                    .withDescription(process.getJobDesc())
                    .withSchedule(cronScheduleBuilder).build();

            JobDetail jobDetail = taipingScheduler.getJobDetail(jobKey);
            // 放入任务中需要使用的数据
            jobDetail.getJobDataMap().put(AppConstant.QUARTZ_PARAM_KEY, process.getData());

            jobDetail.getJobBuilder().withDescription(process.getJobDesc());
            HashSet<Trigger> triggerSet = new HashSet<>();
            triggerSet.add(cronTrigger);

            taipingScheduler.scheduleJob(jobDetail, triggerSet, true);
            log.info("任务修改成功 ：" + jobName);
        } catch (SchedulerException e) {
            log.error("类名不存在或执行表达式错误,exception:{}", e.getMessage());
        }
    }

    /**
     * 删除任务
     *
     * @param jobName  任务名称
     * @param jobGroup 任务分组
     */
    @Override
    public void deleteJob(String jobName, JobGroupEnum jobGroup) {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup.getGroupName());
        try {
            if (checkExists(jobName, jobGroup)) {
                taipingScheduler.pauseJob(jobKey);
                taipingScheduler.deleteJob(jobKey);
                log.info("任务被删除, jobKey:{},jobGroup:{}, jobName:{}", jobKey, jobGroup, jobName);
            }
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 暂停任务
     *
     * @param jobName  任务名称
     * @param jobGroup 任务分组
     */
    @Override
    public void pauseJob(String jobName, JobGroupEnum jobGroup) {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup.getGroupName());
        try {
            boolean exists = checkExists(jobName, jobGroup);
            if (exists) {
                taipingScheduler.pauseJob(jobKey);
                log.info("任务已经被暂停, jobKey:{},jobGroup:{}, jobName:{}", jobKey, jobGroup, jobName);
            }
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 恢复任务
     *
     * @param jobName  任务名称
     * @param jobGroup 任务分组
     */
    @Override
    public void resumeJob(String jobName, JobGroupEnum jobGroup) {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup.getGroupName());
        try {
            if (checkExists(jobName, jobGroup)) {
                taipingScheduler.resumeJob(jobKey);
                log.info("任务已经被恢复,jobKey:{},jobGroup:{}, jobName:{}", jobKey, jobGroup, jobName);
            }
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 检测任务是否存在
     *
     * @param jobName  任务名称
     * @param jobGroup 任务分组
     * @return
     * @throws SchedulerException
     */
    @Override
    public boolean checkExists(String jobName, JobGroupEnum jobGroup) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup.getGroupName());
        return taipingScheduler.checkExists(jobKey);
    }

    /**
     * 新增任务
     *
     * @throws SchedulerException
     */
    private void addJobProcess(TaskProcess process) throws SchedulerException {
        //任务名称
        String jobName = process.getJobName();
        //工作组名称
        String groupName = process.getJobGroupEnum().getGroupName();
        //触发器组名称
        String triggerGroupName = process.getTriggerGroup().getGroupName();
        //任务备注
        String jobDesc = process.getJobDesc();
        //任务数据
        Object data = process.getData();

        if (checkExists(jobName, process.getJobGroupEnum())) {
            log.info("任务以及存在，不需要重复创建");
        }
        // 构建key值  以各自name + 分组名组成
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, triggerGroupName);
        JobKey jobKey = JobKey.jobKey(jobName, groupName);
        // 构建任务详情
        JobDetail jobDetail = JobBuilder.newJob(process.getTClass())
                .withIdentity(jobKey)
                .build();

        // 放入任务中需要使用的数据
        jobDetail.getJobDataMap().put(AppConstant.QUARTZ_PARAM_KEY, data);
        // 构建触发器
        Trigger trigger = null;
        if (process.getOnce()) {
            //单次执行 现在就执行
            trigger = TriggerBuilder.newTrigger()
                    .withDescription(jobDesc)
                    .withIdentity(triggerKey)
                    .startNow()
                    .build();
        } else {
            // 周期执行
            String cron = process.getCron().getCron();
            trigger = TriggerBuilder.newTrigger()
                    .withDescription(jobDesc)
                    .withIdentity(triggerKey)
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                    .build();
        }
        // 调度任务
        taipingScheduler.scheduleJob(jobDetail, trigger);
        taipingScheduler.start();

        log.info("任务启动 ：" + jobName);
        log.info("任务描述 ：" + jobDesc);
        log.info("启动时间 ：" + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
    }


}
