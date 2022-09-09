#!/bin/zsh

mvn clean install -DskipTests -Dcheckstyle.skip=true -Drat.skip=true

java -javaagent:./agent/target/agent-0.0.1-SNAPSHOT.jar \
-jar demo/target/demo-0.0.1-SNAPSHOT.jar