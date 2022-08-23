## javaAgent demo
### 模块介绍
- agent 模块负责实现agent功能
- demo 模块是一个简单的 springboot 项目, 用于测试
  
### 执行步骤
1. 执行 `mvn clean install -DskipTests -Dcheckstyle.skip=true`

2. Idea中配置 TestAnnotationWebsocketApplication.java启动参数 VMOptions `-javaagent:./agent/target/agent-0.0.1-SNAPSHOT.jar` ，并启动，可以正常启动。

3. 执行`java -javaagent:./agent/target/agent-0.0.1-SNAPSHOT.jar -jar demo/target/demo-0.0.1-SNAPSHOT.jar`，报错如下：

   ```
   Caused by: java.lang.ArrayStoreException: sun.reflect.annotation.TypeNotPresentExceptionProxy
           at sun.reflect.annotation.AnnotationParser.parseClassArray(AnnotationParser.java:724) ~[na:1.8.0_345]
           at sun.reflect.annotation.AnnotationParser.parseArray(AnnotationParser.java:531) ~[na:1.8.0_345]
           at sun.reflect.annotation.AnnotationParser.parseMemberValue(AnnotationParser.java:355) ~[na:1.8.0_345]
           at sun.reflect.annotation.AnnotationParser.parseAnnotation2(AnnotationParser.java:286) ~[na:1.8.0_345]
           at sun.reflect.annotation.AnnotationParser.parseAnnotations2(AnnotationParser.java:120) ~[na:1.8.0_345]
           at sun.reflect.annotation.AnnotationParser.parseAnnotations(AnnotationParser.java:72) ~[na:1.8.0_345]
           at java.lang.Class.createAnnotationData(Class.java:3521) ~[na:1.8.0_345]
           at java.lang.Class.annotationData(Class.java:3510) ~[na:1.8.0_345]
           at java.lang.Class.getDeclaredAnnotations(Class.java:3477) ~[na:1.8.0_345]
           at org.springframework.core.annotation.AnnotationsScanner.getDeclaredAnnotations(AnnotationsScanner.java:454) ~[agent-0.0.1-SNAPSHOT.jar:na]
           at org.springframework.core.annotation.AnnotationsScanner.isKnownEmpty(AnnotationsScanner.java:492) ~[agent-0.0.1-SNAPSHOT.jar:na]
           at org.springframework.core.annotation.TypeMappedAnnotations.from(TypeMappedAnnotations.java:251) ~[agent-0.0.1-SNAPSHOT.jar:na]
           at org.springframework.core.annotation.MergedAnnotations.from(MergedAnnotations.java:351) ~[agent-0.0.1-SNAPSHOT.jar:na]
           at org.springframework.core.annotation.MergedAnnotations.from(MergedAnnotations.java:330) ~[agent-0.0.1-SNAPSHOT.jar:na]
           at org.springframework.core.annotation.MergedAnnotations.from(MergedAnnotations.java:313) ~[agent-0.0.1-SNAPSHOT.jar:na]
           at org.springframework.beans.factory.support.DefaultListableBeanFactory.findMergedAnnotationOnBean(DefaultListableBeanFactory.java:743) ~[agent-0.0.1-SNAPSHOT.jar:na]
           at org.springframework.beans.factory.support.DefaultListableBeanFactory.findAnnotationOnBean(DefaultListableBeanFactory.java:733) ~[agent-0.0.1-SNAPSHOT.jar:na]
           at org.springframework.beans.factory.support.DefaultListableBeanFactory.getBeanNamesForAnnotation(DefaultListableBeanFactory.java:703) ~[agent-0.0.1-SNAPSHOT.jar:na]
           at org.springframework.beans.factory.support.DefaultListableBeanFactory.getBeansWithAnnotation(DefaultListableBeanFactory.java:717) ~[agent-0.0.1-SNAPSHOT.jar:na]
           at org.springframework.boot.jackson.JsonComponentModule.addJsonBeans(JsonComponentModule.java:79) ~[agent-0.0.1-SNAPSHOT.jar:na]
           at org.springframework.boot.jackson.JsonComponentModule.registerJsonComponents(JsonComponentModule.java:71) ~[agent-0.0.1-SNAPSHOT.jar:na]
           at org.springframework.boot.jackson.JsonComponentModule.afterPropertiesSet(JsonComponentModule.java:64) ~[agent-0.0.1-SNAPSHOT.jar:na]
           at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.invokeInitMethods(AbstractAutowireCapableBeanFactory.java:1863) ~[agent-0.0.1-SNAPSHOT.jar:na]
           at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1800) ~[agent-0.0.1-SNAPSHOT.jar:na]
           ... 122 common frames omitted
   
   
   ```

   

