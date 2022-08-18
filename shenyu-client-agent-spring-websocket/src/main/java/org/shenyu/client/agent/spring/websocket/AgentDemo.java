package org.shenyu.client.agent.spring.websocket;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AgentDemo {

    static ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);

    static List<String> hashCached = new ArrayList<>();

    public static void premain(String agentArgs, Instrumentation inst) {
        init(agentArgs, inst);
    }

    public static void init(String args, Instrumentation inst) {
        System.out.println("-------------------agent start-------------------");

        scheduledExecutorService.scheduleAtFixedRate(() -> tryRedefine(inst, args), 0, 10, TimeUnit.SECONDS);

//        tryRedefine(inst, args);

        System.out.println("-------------------agent end-------------------");
    }

    private static void tryRedefine(Instrumentation instrumentation, String agentArgs) {
        System.out.println("-------------------tryRedefine-------------------");
        Class[] allLoadedClasses = instrumentation.getAllLoadedClasses();

        System.out.println("allLoadedClasses: " + allLoadedClasses.length);
        Map<String, Class> finupAllLoadedClasses = new HashMap<>();
        System.out.println(allLoadedClasses);

        try {
            for (Class loadedClass : allLoadedClasses) {
                System.out.println("loadedClass: " + loadedClass.getCanonicalName());

                if (loadedClass == null) {
                    continue;
                }
                if (loadedClass.getCanonicalName() == null) {
                    continue;
                }
                if (!loadedClass.getCanonicalName().startsWith("org.shenyu.client.agent")) {
                    continue;
                }
                if (hashCached.contains(loadedClass.getCanonicalName())) {
                    continue;
                }
                System.out.println("put loadedClass: " + loadedClass.getCanonicalName());
                finupAllLoadedClasses.put(loadedClass.getCanonicalName(), loadedClass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, byte[]> rewriteClasses = ClassesLoadUtil.getRewriteClasses(agentArgs);
        System.out.println("rewriteClasses数量:" + rewriteClasses.size());

        for (String className : hashCached) {
            rewriteClasses.remove(className);
        }
        System.out.println("rewriteClasses数量:" + rewriteClasses.size());
        System.out.println("finupAllLoadedClasses数量:" + finupAllLoadedClasses.size());

        if (finupAllLoadedClasses.size() == 0 || rewriteClasses.size() == 0) {
            return;
        }

        System.out.println("finupAllLoadedClasses数量:" + finupAllLoadedClasses.size());

        for (String className : rewriteClasses.keySet()) {
            byte[] classBytes = rewriteClasses.get(className);

            if (classBytes == null || classBytes.length == 0) {
                System.out.println("从 rewriteClasses 找不到class: " + className);
                continue;
            }

            Class redefineClass = finupAllLoadedClasses.get(className);
//            if (redefineClass == null) {
//                System.out.println("从 finupAllLoadedClasses 找不到class: " + className);
//                continue;
//            }

            System.out.println("开始redefineClasses: " + className);

            ClassDefinition classDefinition = new ClassDefinition(redefineClass, classBytes);

            try {
                instrumentation.redefineClasses(classDefinition);
                hashCached.add(className);

                System.out.println("结束redefineClasses: " + className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (UnmodifiableClassException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
