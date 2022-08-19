package org.shenyu.client.agent.spring.websocket;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.jar.JarFile;

public class AgentDemo {

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("-------------------agent start-------------------");

        String packagePrefix = "org.apache.shenyu.client.spring.websocket";
        redefine(inst, agentArgs, packagePrefix);
    }


    private static void redefine(Instrumentation instrumentation, String agentArgs, String packagePrefix) {

        URL url = null;
        try {
            url = new URL("file:" + agentArgs);
            System.out.println(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        ClassLoader loader = new URLClassLoader(new URL[]{url});

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
            try {
                Class redefineClass = loader.loadClass(className);
                if (redefineClass == null) {
                    System.out.println("Can not find class from needRedefineClassMap: " + className + ". Skip...");
                    continue;
                }
                System.out.println("Redefine class: " + className);
                ClassDefinition classDefinition = new ClassDefinition(redefineClass, classBytes);

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
}
