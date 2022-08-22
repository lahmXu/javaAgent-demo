package org.apache.shenyu.client.spring.websocket;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;

public class AgentDemo {

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("-------------------agent start-------------------");

        String packagePrefix = "org.apache.shenyu.client.spring.websocket.init.TestUtils";
        redefine(inst, agentArgs, packagePrefix);
    }


    private static void redefine(Instrumentation instrumentation, String jarPath, String packagePrefix) {

        URL url = null;
        try {
            url = new URL("file:" + jarPath);
            System.out.println(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        ClassLoader loader = new URLClassLoader(new URL[]{url});

        Map<String, byte[]> rewriteClasses = ClassesLoadUtil.getRewriteClasses(jarPath, packagePrefix);

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
                Class redefineClass = Class.forName(className);
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
