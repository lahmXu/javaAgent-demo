package org.apache.shenyu.client.spring.websocket;

import java.lang.instrument.Instrumentation;

public class AgentDemo {

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("-------------------agent start-------------------");
        System.out.println("-------------------agent end-------------------");
    }
}
