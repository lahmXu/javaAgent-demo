package org.shenyu.client.agent.spring.websocket;

import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.util.concurrent.Callable;

public class CallMethodDelegation {
    @RuntimeType
    public static Object call(@RuntimeType @SuperCall Callable<Object> callable) {
        Object result = null;
        try {
            // 反射调用 jar 加载的方法
            result = callable.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
