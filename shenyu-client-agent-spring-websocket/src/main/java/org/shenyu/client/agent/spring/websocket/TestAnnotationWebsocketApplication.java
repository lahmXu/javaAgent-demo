package org.shenyu.client.agent.spring.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class TestAnnotationWebsocketApplication {

    /**
     * Main Entrance.
     * @param args startup arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(TestAnnotationWebsocketApplication.class, args);
        System.out.println("helllllll");
    }
}
