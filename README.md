## javaAgent demo
### 模块介绍
- agent 模块负责实现agent功能, 目前是简单的打印入参
- demo 模块是一个简单的 springboot 项目, 用于测试
  
### 执行步骤
1. 切换到 javaAgent-demo 目录下执行`mvn clean package -DskipTests`, 等待编译完成
2. 先启动 DemoApplication，再执行` TestAttach.java` 通过 attach 功能，将 agent 中的内容挂载到指定服务上
3. 控制台输出如下表示成功, agent中修改的方法成功执行
  ```log
  -------------------agent main start-------------------
  -------------------agent main end-------------------
  ```