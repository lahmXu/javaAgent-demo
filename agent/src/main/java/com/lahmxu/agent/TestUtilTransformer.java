package com.lahmxu.agent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class TestUtilTransformer implements ClassFileTransformer {
    private String targetClassName;

    private String targetMethodName;

    public TestUtilTransformer(String targetClassName, String targetMethodName) {
        this.targetClassName = targetClassName;
        this.targetMethodName = targetMethodName;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        className = className.replace("/", ".");

        if (className.equals(targetClassName)) {
            try {
                // 得到类信息
                CtClass ctClass = ClassPool.getDefault().get(className);

//                // 错误示例：构建新的类的成员变量
//                CtField nameField = CtField.make("private static final String name = \"default\";", ctClass);
//                ctClass.addField(nameField);

                // 正确示例：增加静态方法，参考链接：https://www.cnblogs.com/rickiyang/p/11368932.html
                CtMethod ctmethod = CtNewMethod.make("private static void test2() { System.out.println(\"hello test2! \"); }", ctClass);
                ctClass.addMethod(ctmethod);

                // 正确示例：修改方法实现
                CtMethod ctOriginMethod = ctClass.getDeclaredMethod(targetMethodName);
                ctOriginMethod.setBody("System.out.println(\"hello Agent! \");");

                return ctClass.toBytecode();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
