package com.lahmxu.agent;

import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;

import java.io.File;
import java.io.IOException;

public enum WeavingClassLog {
    INSTANCE;
    private File weavingClassLogPath;
    public void log(TypeDescription typeDescription, DynamicType dynamicType) {
        synchronized (INSTANCE) {
            try {
                if (weavingClassLogPath == null) {
                    try {
                        weavingClassLogPath = new File("/Users/lahmxu/Documents/workspace/java/javaAgent-demo/weaving-class");
                        if (!weavingClassLogPath.exists()) {
                            weavingClassLogPath.mkdir();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                try {
                    dynamicType.saveIn(weavingClassLogPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}
