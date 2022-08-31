package org.apache.shenyu.client.spring.websocket.init;

public class TestUtils {

    // 正确示例：只能新增静态私有方法
    private static String test2() {
        return "test2";
    }

    public static void test() {
        System.out.println("Agent TestUtils test(), name:" + test2());
    }
}
