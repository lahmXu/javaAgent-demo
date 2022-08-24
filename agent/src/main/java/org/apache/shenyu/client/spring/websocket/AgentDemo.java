package org.apache.shenyu.client.spring.websocket;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;
import org.apache.shenyu.client.spring.websocket.agent.ComponentScanAnnotationInterceptor;
import org.apache.shenyu.client.spring.websocket.agent.TransformListener;

import java.lang.instrument.Instrumentation;

public class AgentDemo {

    public static String componentScanPath;

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("-------------------agent start-------------------");
        componentScanPath = agentArgs;
        new AgentBuilder.Default()
                .type(ElementMatchers.nameStartsWith("org.springframework.context.annotation.ComponentScanAnnotationParser"))
                .transform((builder, typeDescription, classLoader, module, protectionDomain) -> builder
                        .method(ElementMatchers.named("parse"))
                        .intercept(Advice.to(ComponentScanAnnotationInterceptor.class)))
                .with(new TransformListener())
                .installOn(inst);
        System.out.println("-------------------agent end-------------------");
    }
}
