package com.lahmxu.agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

public class AgentDemo {

    /**
     * 该方法在 main 方法之前运行, 与 main 方法运行在同一个 JVM 中
     */
    public static void premain(String agentArgs, Instrumentation inst) {
        init(agentArgs, inst);
    }

    public static void init(String args, Instrumentation inst) {
        System.out.println("-------------------agent start-------------------");

        // 拦截test方法执行，拦截之后执行 CallMethodDelegation 中的方法
        new AgentBuilder.Default()
                .type(ElementMatchers.nameStartsWith("com.lahmxu.demo.DemoApplication"))
                .transform((builder, typeDescription, classLoader, module) -> builder
                        .method(ElementMatchers.named("onApplicationEvent"))
                        .intercept(MethodDelegation.to(CallMethodDelegation.class))
                )
                .installOn(inst);

        System.out.println("-------------------agent end-------------------");
    }
}
