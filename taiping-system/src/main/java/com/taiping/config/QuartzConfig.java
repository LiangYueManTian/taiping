package com.taiping.config;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * quartz 配置类
 *
 * @author liyj
 * @date 2019/9/20
 */
@Configuration
public class QuartzConfig {

    @Autowired
    private DataSource dataSource;

    /**
     * 获取SchedulerFactoryBean
     *
     * @param jobFactory quartz 表单
     * @return
     * @throws IOException Io异常
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(QuartzJobFactory jobFactory) throws IOException {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();

        //使job实例支持spring 容器管理
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        // 此处设置了可以直接在job里注入spring对象
        schedulerFactoryBean.setJobFactory(jobFactory);

        //获取配置属性
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        //在quartz.properties中的属性被读取并注入后再初始化对象
        propertiesFactoryBean.afterPropertiesSet();
        Properties properties = propertiesFactoryBean.getObject();
        schedulerFactoryBean.setQuartzProperties(properties);

        schedulerFactoryBean.setDataSource(dataSource);

        // 延迟10s启动quartz
        schedulerFactoryBean.setStartupDelay(10);

        return schedulerFactoryBean;
    }

    /**
     * 获取Scheduler
     *
     * @param schedulerFactoryBean
     * @return
     * @throws IOException
     * @throws SchedulerException
     */
    @Bean("taipingScheduler")
    public Scheduler scheduler(SchedulerFactoryBean schedulerFactoryBean) throws IOException, SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.start();
        return scheduler;
    }
}
