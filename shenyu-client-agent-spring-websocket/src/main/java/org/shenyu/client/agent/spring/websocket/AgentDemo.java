package org.shenyu.client.agent.spring.websocket;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

public class AgentDemo {

    public static void premain(String agentArgs, Instrumentation instrumentation) throws UnmodifiableClassException, ClassNotFoundException {
        System.out.println("-----------------------------------agent-----------------------------------");

        new AgentBuilder.Default()
                .type(ElementMatchers.nameStartsWith("org.apache.shenyu.client.agent"))
                .transform((builder, typeDescription, classLoader, module) -> builder
                        .method(ElementMatchers.any())
                        .intercept(MethodDelegation.to(CallMethodDelegation.class)))
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .with(new TransformListener())
                .installOn(instrumentation);
    }
}
