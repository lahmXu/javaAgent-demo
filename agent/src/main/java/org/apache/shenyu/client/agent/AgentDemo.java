package org.apache.shenyu.client.agent;

import java.lang.instrument.Instrumentation;

public class AgentDemo {

    public static void premain(String agentArgs, Instrumentation inst) throws Throwable {
        System.out.println("-------------------agent main start-------------------");

        System.out.println("-------------------agent main end-------------------");
    }
}
