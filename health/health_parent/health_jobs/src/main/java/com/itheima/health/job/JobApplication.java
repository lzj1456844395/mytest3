package com.itheima.health.job;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Description: No Description
 * User: Eric
 */
public class JobApplication {
    public static void main(String[] args) throws IOException {
        new ClassPathXmlApplicationContext("classpath:applicationContext-jobs.xml","classpath:applicationContext-redis.xml");
        System.in.read();
    }
}
