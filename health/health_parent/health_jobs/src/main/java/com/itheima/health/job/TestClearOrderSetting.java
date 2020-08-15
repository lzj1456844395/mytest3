package com.itheima.health.job;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * @Author Tian Qing
 * @Daate: Created in 9:48 2020/7/6
 */
public class TestClearOrderSetting {
    public static void main(String[] args) throws IOException {
        new ClassPathXmlApplicationContext("classpath:applicationContext-OrderSettingJobs.xml");
        System.in.read();
    }
}
