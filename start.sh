#!/bin/zsh

mvn clean install -DskipTests -Dcheckstyle.skip=true -Drat.skip=true

java -javaagent:./shenyu-client-agent-spring-websocket/target/shenyu-client-agent-spring-websocket-0.0.1-SNAPSHOT.jar="/Users/lahmxu/Documents/workspace/java/javaAgent-demo/demo/target/demo-0.0.1-SNAPSHOT.jar" -jar demo/target/demo-0.0.1-SNAPSHOT.jar
