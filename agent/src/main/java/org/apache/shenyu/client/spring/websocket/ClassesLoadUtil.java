package org.apache.shenyu.client.spring.websocket;

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

    private static final Map<String, byte[]> className2Classes = new ConcurrentHashMap<>();

    private static boolean havaLoaded = false;

    private static String PACKAGE_PATH;

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

    private static void entryRead(String jarPath, ZipEntry entry) throws IOException {
        if (entry.getSize() <= 0) {
            return;
        }
        String fileName = entry.getName();
        if (!fileName.endsWith(".class")) {
            return;
        }
        String fileNameWithOutClass = fileName.replaceAll("\\.class", "");
        String fileNameWithPath = fileNameWithOutClass.replaceAll("/", ".");
        if (!fileNameWithPath.startsWith(PACKAGE_PATH)) {
            return;
        }

        try (ZipFile zf = new ZipFile(jarPath); InputStream input = zf.getInputStream(entry); ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            if (input == null) {
                return;
            }

            int b = 0;
            while ((b = input.read()) != -1) {
                byteArrayOutputStream.write(b);
            }
            byte[] bytes = byteArrayOutputStream.toByteArray();

            className2Classes.put(fileNameWithPath, bytes);
            System.out.println("load class, fileName: " + fileName + ", className:" + fileNameWithPath);
        }
    }


    public static Map<String, byte[]> getRewriteClasses(String agentArgs, String packagePath) {
        PACKAGE_PATH = packagePath;
        synchronized (className2Classes) {
            if (!havaLoaded) {
                loadFromZipFile(agentArgs);
                havaLoaded = true;
            }
        }
        return className2Classes;
    }
}
