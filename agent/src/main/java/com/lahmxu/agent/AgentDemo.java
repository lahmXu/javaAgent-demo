package com.lahmxu.agent;

import java.lang.instrument.Instrumentation;

public class AgentDemo {

    /**
     * 该方法在 main 方法之前运行, 与 main 方法运行在同一个 JVM 中
     */
    public static void premain(String agentArgs, Instrumentation inst) {
        /**
         * 此处可以通过 javassist 修改字节码操作, 完成修改编码的功能
         */
        System.out.println("-------------------agent start-------------------");
        System.out.println("------ premain 方法两个入参 ------ agentArgs:" + agentArgs + " inst:" + inst.toString());
        System.out.println("------ 此处可以进行字节码操作 ------ ");
        System.out.println("-------------------agent end-------------------");
    }

    /**
     * 如果不存在 {@link AgentDemo#premain(String, Instrumentation)}, 则会执行本方法
     */
    public static void premain(String agentArgs) {
        System.out.println("------ premain 方法一个参数 ------ agentArgs:" + agentArgs);
    }
}
