package com.lahmxu.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

@SpringBootApplication
public class DemoApplication implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${demo.version}")
    private String version;

    public static void main(String[] args) {
        System.out.println("------ main ------");
        SpringApplication.run(DemoApplication.class, args);
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        test();
    }

    public void test() {
        System.out.println("version:" + version);
    }
}
