## javaAgent demo
### 模块介绍
- agent 模块负责实现agent功能, 目前是简单的打印入参
- demo 模块是一个简单的 springboot 项目, 用于测试
  
### 执行步骤
1. 切换到 javaAgent-demo 目录下执行 mvn clean package -DskipTests
2. 执行`java -javaagent:./agent/target/agent-0.0.1-SNAPSHOT.jar="hello lahmxu" -jar demo/target/demo-0.0.1-SNAPSHOT.jar`
3. 控制台输出如下表示成功, agent中修改的方法成功执行
  ```log
    -------------------agent start-------------------
    ------ premain 方法两个入参 ------ agentArgs:hello lahmxu inst:sun.instrument.InstrumentationImpl@33909752
    ------ 此处可以进行字节码操作 ------ 
    -------------------agent end-------------------
    ------ main ------
  ```