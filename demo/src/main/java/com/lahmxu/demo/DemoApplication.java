package com.lahmxu.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.lahmxu.demo.TestUtil.hello;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("------ main ------");
        SpringApplication.run(DemoApplication.class, args);
        while (true) {
            hello();
            Thread.sleep(1000);
        }
    }
}
