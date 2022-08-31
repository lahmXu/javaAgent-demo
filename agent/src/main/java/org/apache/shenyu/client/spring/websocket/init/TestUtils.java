package org.apache.shenyu.client.spring.websocket.init;

public class TestUtils {

    public String name = "name2";

    // 正确示例：只能新增静态私有方法
    private String test2() {
        return "test2";
    }

    public void test() {
        System.out.println("Agent TestUtils test(), name:" + name);
    }
}
