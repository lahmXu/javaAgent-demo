package com.lahmxu.agent;

import net.bytebuddy.implementation.bind.annotation.FieldValue;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.util.concurrent.Callable;

public class CallMethodDelegation {

    @RuntimeType
    public static Object call(@RuntimeType @FieldValue("version") String version,
                              @SuperCall Callable<Object> callable) {
        System.out.println("enter method call");
        Object result = null;
        try {
            // @SuperCall修饰的Callable<Object>为对原方法调用的封装，返回类型为void时为Runnable
            result = callable.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("[CallMethodDelegation] version:" + version);
        System.out.println("exit method get");
        return result;
    }
}
