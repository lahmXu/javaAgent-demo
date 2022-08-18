package org.shenyu.client.agent.spring.websocket;

import org.apache.shenyu.client.spring.websocket.init.SpringWebSocketClientEventListener;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AgentDemo {

    static ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("-------------------agent start-------------------");

        String packagePrefix = "org.apache.shenyu.client.spring.websocket";
        scheduledExecutorService.scheduleAtFixedRate(() -> redefine(inst, agentArgs, packagePrefix), 0, 10, TimeUnit.SECONDS);
    }


    private static void redefine(Instrumentation instrumentation, String agentArgs, String packagePrefix) {
        Map<String, Class> needRedefineClassMap = getNeedRedefineClass(instrumentation, packagePrefix);

        Map<String, byte[]> rewriteClasses = ClassesLoadUtil.getRewriteClasses(agentArgs, packagePrefix);

        if (rewriteClasses.size() == 0) {
            System.out.println("Can not find class from rewriteClasses. Skip...");
            return;
        }

        for (String className : rewriteClasses.keySet()) {
            byte[] classBytes = rewriteClasses.get(className);

            if (classBytes == null || classBytes.length == 0) {
                System.out.println("Can not find class from rewriteClasses: " + className + ". Skip...");
                continue;
            }

            Class redefineClass = needRedefineClassMap.get(className);
            if (redefineClass == null) {
                System.out.println("Can not find class from needRedefineClassMap: " + className + ". Skip...");
                continue;
            }
            System.out.println("Redefine class: " + className);
            ClassDefinition classDefinition = new ClassDefinition(redefineClass, classBytes);
            try {
                instrumentation.redefineClasses(classDefinition);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (UnmodifiableClassException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private static Map<String, Class> getNeedRedefineClass(Instrumentation instrumentation, String packagePrefix) {
        Map<String, Class> result = new HashMap<>();

        Class[] allLoadedClasses = instrumentation.getAllLoadedClasses();
        Map<String, Class> finupAllLoadedClasses = new HashMap<>();
        try {
            for (Class loadedClass : allLoadedClasses) {
                if (loadedClass == null) {
                    continue;
                }
                if (loadedClass.getCanonicalName() == null) {
                    continue;
                }
                if (!loadedClass.getCanonicalName().startsWith(packagePrefix)) {
                    continue;
                }
                System.out.println("===============================================================");
                System.out.println(loadedClass.getCanonicalName());
                result.put(loadedClass.getCanonicalName(), loadedClass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
