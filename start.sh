#!/bin/zsh

mvn clean install -DskipTests -Dcheckstyle.skip=true -Drat.skip=true

java -javaagent:./shenyu-client-agent-spring-websocket/target/shenyu-client-agent-spring-websocket-0.0.1-SNAPSHOT.jar="/Users/lahmxu/Documents/workspace/java/javaAgent-demo/shenyu-client-agent-spring-websocket/src/main/resources/shenyu-client-spring-websocket-2.5.0-SNAPSHOT.jar" -jar demo/target/demo-0.0.1-SNAPSHOT.jar
