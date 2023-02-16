package com.lahmxu.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

@SpringBootApplication
public class DemoApplication implements ApplicationListener<ApplicationReadyEvent> {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
//        ApplicationContext applicationContext = event.getApplicationContext();
//        ShenyuSpringContextListener listener = applicationContext.getBean(ShenyuSpringContextListener.class);
//        listener.init();

//        String[] names = applicationContext.getBeanDefinitionNames();
//        for (String name : names) {
//            System.out.println("bean name: " + name);
//        }
    }
}
