package com.taiping.bean;

import com.taiping.enums.job.JobCronEnum;
import com.taiping.enums.job.JobGroupEnum;
import com.taiping.enums.job.JobTriggerEnum;
import com.taiping.utils.NineteenUUIDUtils;
import lombok.Data;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 任务实体
 *
 * @author liyj
 * @date 2019/9/20
 */
@Data
public class TaskProcess {
    /**
     * 任务id 默认为UUID
     */
    private String jobId = NineteenUUIDUtils.uuid();
    /**
     * 任务名称
     */
    private String jobName;
    /**
     * 任务描述
     */
    private String jobDesc;
    /**
     * 所属任务组
     */
    private JobGroupEnum jobGroupEnum;
    /**
     * 任务触发器名称
     */
    private String triggerName;

    /**
     * 任务触发器分组
     */
    private JobTriggerEnum triggerGroup;

    /**
     * 任务周期 cron 表达式转换
     */
    private JobCronEnum cron;

    /**
     * 间隔周期
     */
    private int intervalSecond;

    /**
     * 任务执行class
     */
    private Class<? extends QuartzJobBean> tClass;

    /**
     * 传入任务的对象
     */
    private Object data;

    /**
     * 任务创建时间
     */
    private Long createTime;
    /**
     * 优先级
     */
    private Integer period;
    /**
     * 是否单次执行
     */
    private Boolean once;

    /**
     * 传入class对象 构造对象
     *
     * @param tClass
     */
    public TaskProcess(Class<? extends QuartzJobBean> tClass) {
        this.tClass = tClass;
    }

    public TaskProcess() {
    }
}
