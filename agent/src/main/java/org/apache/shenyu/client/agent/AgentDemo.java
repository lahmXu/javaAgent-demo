package org.apache.shenyu.client.agent;

import java.lang.instrument.Instrumentation;

public class AgentDemo {

    public static void premain(String agentArgs, Instrumentation instrumentation) throws Throwable {
        System.out.println("-------------------agent main start-------------------");
//        new AgentBuilder.Default()
//                .type(ElementMatchers.nameStartsWith("org.apache.shenyu.client.agent.init.ShenyuSpringContextListener"))
//                .transform(Transformer.transformer())
//                .installOn(instrumentation);
        System.out.println("-------------------agent main end-------------------");
    }
}
