package org.apache.shenyu.client.agent;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.modifier.FieldManifestation;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class AgentDemo {

    public static void premain(String agentArgs, Instrumentation instrumentation) throws Throwable {
        System.out.println("-------------------agent main start-------------------");
        new AgentBuilder.Default()
                .type(ElementMatchers.nameStartsWith("org.apache.shenyu.client.agent.init.ShenyuSpringContextListener"))
                .transform(Transformer.transformer())
                .installOn(instrumentation);

        System.out.println("-------------------agent main end-------------------");
    }
}
