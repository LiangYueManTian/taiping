package com.taiping.job;

import com.taiping.bean.TaskProcess;
import com.taiping.enums.job.JobGroupEnum;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * 任务的增删查改
 *
 * @author liyj
 * @date 2019/9/20
 */
public interface JobService {
    /**
     * 查询任务列表
     *
     * @return 返回任务列表
     */
    List<TaskProcess> queryTasklist();

    /**
     * 新增任务
     *
     * @param info 任务信息
     * @throws SchedulerException 任务异常
     */
    void addJob(TaskProcess info) throws SchedulerException;

    /**
     * 修改定时任务
     *
     * @param info 任务信息
     */
    void updateJob(TaskProcess info);

    /**
     * 删除任务
     *
     * @param jobName  任务名称
     * @param jobGroup 任务分组
     */
    void deleteJob(String jobName, JobGroupEnum jobGroup);

    /**
     * 暂停任务
     *
     * @param jobName  任务名称
     * @param jobGroup 任务分组
     */
    void pauseJob(String jobName, JobGroupEnum jobGroup);

    /**
     * 恢复任务
     *
     * @param jobName  任务名称
     * @param jobGroup 任务分组
     */
    void resumeJob(String jobName, JobGroupEnum jobGroup);

    /**
     * 检查任务是否存在
     *
     * @param jobName  任务名称
     * @param jobGroup 任务分组
     * @return
     * @throws SchedulerException
     */
    boolean checkExists(String jobName, JobGroupEnum jobGroup) throws SchedulerException;

}
