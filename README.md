## javaAgent demo
### 模块介绍
- agent 模块负责实现agent功能, 目前是简单的打印入参
- demo 模块是一个简单的 springboot 项目, 用于测试
- shenyu-client-websocket-annotation 模块是一个注解模块，用于提供注解
### 执行步骤
1. 切换到 javaAgent-demo 目录下执行`mvn clean package -DskipTests`, 等待编译完成

2. 先启动 DemoApplication，正常启动，不会向 shenyu 注册

3. 执行`demo/test/java/TestAttach.java` 

   - 调整为本机绝对路径

	```java
	virtualMachine.loadAgent("/Users/lahmxu/Documents/workspace/java/javaAgent-demo/agent/target/agent-0.0.1-SNAPSHOT.jar");
	```
	
	
	   - 通过 attach 功能，将 agent 中的内容挂载到指定服务上，向 shenyu 注册服务
