package com.lahmxu.demo;

import org.apache.shenyu.client.spring.websocket.init.SpringWebSocketClientEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

@SpringBootApplication
public class DemoApplication implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${demo.version:}")
    private String version;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        SpringWebSocketClientEventListener.test();
    }

}
