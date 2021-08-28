package com.taiping;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;


/**
 * @author liyj
 * @date 2019/9/16
 */
@SpringBootApplication
@MapperScan(basePackages = "com.taiping.biz.*.dao")
@ComponentScan(basePackages = "com.taiping")
@EnableAsync
public class SystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class, args);
    }

}
