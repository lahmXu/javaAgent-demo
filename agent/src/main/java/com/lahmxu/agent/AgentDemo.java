package com.lahmxu.agent;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.List;

public class AgentDemo {

    public static void agentmain(String agentOps, Instrumentation inst) {
        System.out.println("-------------------agent main start-------------------");
        System.out.println("-------------------agent main end-------------------");


    }
}
