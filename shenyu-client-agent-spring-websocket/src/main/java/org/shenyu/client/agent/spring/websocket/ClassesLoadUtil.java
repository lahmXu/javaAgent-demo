package org.shenyu.client.agent.spring.websocket;

import jdk.nashorn.internal.ir.ContinueNode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ClassesLoadUtil {

    private static final Map<String, byte[]> path2Classes = new ConcurrentHashMap<>();
    private static final Map<String, byte[]> className2Classes = new ConcurrentHashMap<>();

    private static boolean havaLoaded = false;

    private static void loadFromZipFile(String jarPath) {
        try {
            ZipFile zipFile = new ZipFile(jarPath);
            Enumeration<? extends ZipEntry> entrys = zipFile.entries();
            while (entrys.hasMoreElements()) {
                ZipEntry zipEntry = entrys.nextElement();
                entryRead(jarPath, zipEntry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static boolean entryRead(String jarPath, ZipEntry ze) throws IOException {
        if (ze.getSize() > 0) {
            String fileName = ze.getName();
            if (!fileName.endsWith(".class")) {
                return true;
            }

            try (ZipFile zf = new ZipFile(jarPath); InputStream input = zf.getInputStream(ze); ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                if (input == null) {
                    return true;
                }
                int b = 0;
                while ((b = input.read()) != -1) {
                    byteArrayOutputStream.write(b);
                }
                byte[] bytes = byteArrayOutputStream.toByteArray();

                path2Classes.put(fileName, bytes);

                String name1 = fileName.replaceAll("\\.class", "").replace("BOOT-INF/classes/","");
                String name2 = name1.replaceAll("/", ".");

                if (fileName.contains("org/shenyu/client/agent/spring/websocket/")){
                    className2Classes.put(name2, bytes);
                    System.out.println("加载文件: fileName : " + fileName + ".  className:" + name2);
                }
            }
        } else {
//          System.out.println(ze.getName() + " size is 0");
        }
        return false;
    }


    public static Map<String, byte[]> getRewriteClasses(String agentArgs) {
        synchronized (className2Classes) {
            if (!havaLoaded) {
                loadFromZipFile(agentArgs);
                havaLoaded = true;
            }
        }

        return className2Classes;
    }
}
