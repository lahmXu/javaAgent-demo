## javaAgent demo
### 模块介绍
- agent 模块负责实现agent功能
- demo 模块是一个简单的 springboot 项目, 用于测试
  
### 执行步骤
1. 执行 `mvn clean install -DskipTests -Dcheckstyle.skip=true`
2. 执行`java -javaagent:./agent/target/agent-0.0.1-SNAPSHOT.jar -jar demo/target/demo-0.0.1-SNAPSHOT.jar`

