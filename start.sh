#!/bin/zsh

mvn clean install -DskipTests -Dcheckstyle.skip=true -Drat.skip=true

java -jar demo/target/demo-0.0.1-SNAPSHOT.jar