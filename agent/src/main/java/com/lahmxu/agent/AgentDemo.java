package com.lahmxu.agent;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.List;

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

    public static void agentmain(String agentOps, Instrumentation inst) {
        System.out.println("-------------------agent main start-------------------");
        System.out.println("-------------------agent main end-------------------");
    }
}
