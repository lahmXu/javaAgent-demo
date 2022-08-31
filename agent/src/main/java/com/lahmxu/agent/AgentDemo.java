package com.lahmxu.agent;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

public class AgentDemo {

    public static void agentmain(String agentArgs, Instrumentation inst) throws UnmodifiableClassException {
        System.out.println("-------------------agent main start-------------------");

        String[] args = agentArgs.split("&");
        String className = args[0];
        String methodName = args[1];

        for(Class clazz : inst.getAllLoadedClasses()) {
            if(clazz.getName().equals(className)) {
                inst.addTransformer(new TestUtilTransformer(className, methodName), true);
                inst.retransformClasses(clazz);
            }
        }
        System.out.println("-------------------agent main end-------------------");
    }
}
