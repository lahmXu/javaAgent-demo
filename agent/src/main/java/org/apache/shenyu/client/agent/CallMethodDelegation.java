package org.apache.shenyu.client.agent;

import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.util.concurrent.Callable;

public class CallMethodDelegation {

    @RuntimeType
    public static Object call(@RuntimeType @SuperCall Callable<Object> callable) {
        Object result = null;
        try {
            result = callable.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
