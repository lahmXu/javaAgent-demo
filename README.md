## javaAgent demo
### 模块介绍
- agent 模块负责实现agent功能
- demo 模块是一个简单的 springboot 项目, 用于测试
  
### 执行步骤
1. 切换到 javaAgent-demo 目录下执行`mvn clean package -DskipTests`, 等待编译完成
2. 执行`java -javaagent:./agent/target/agent-0.0.1-SNAPSHOT.jar="hello lahmxu" -jar demo/target/demo-0.0.1-SNAPSHOT.jar`
3. 控制台输出如下表示成功, agent中修改的方法成功执行
  ```yaml
  # 输出：
  enter method call
  version:1.0 // 执行方法的输出
  [CallMethodDelegation] version:1.0 // 拦截方法的输出
  exit method get
  ```

