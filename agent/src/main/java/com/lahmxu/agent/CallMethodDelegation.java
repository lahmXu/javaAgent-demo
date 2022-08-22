package com.lahmxu.agent;

import net.bytebuddy.implementation.bind.annotation.FieldValue;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shenyu.client.core.disruptor.ShenyuClientRegisterEventPublisher;
import org.apache.shenyu.client.spring.websocket.annotation.ShenyuSpringWebSocketClient;
import org.apache.shenyu.common.enums.RpcTypeEnum;
import org.apache.shenyu.register.common.dto.MetaDataRegisterDTO;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;

public class CallMethodDelegation {

    private static final String PATH_SEPARATOR = "/";

    @RuntimeType
    public static Object call(@RuntimeType @FieldValue("contextRefreshedEvent") ContextRefreshedEvent contextRefreshedEvent
            , @FieldValue("isFull") Boolean isFull
            , @FieldValue("publisher") ShenyuClientRegisterEventPublisher publisher
            , @FieldValue("contextPath") String contextPath
            , @FieldValue("mappingAnnotation") List<Class<? extends Annotation>> mappingAnnotation
            , @FieldValue("pathAttributeNames") String[] pathAttributeNames
            , @FieldValue("appName") String appName
            , @SuperCall Callable<Object> callable) {
        System.out.println("enter method call");
        Object result = null;
        try {
            // @SuperCall修饰的Callable<Object>为对原方法调用的封装，返回类型为void时为Runnable
            result = callable.call();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Filter out is not controller out
        if (Boolean.TRUE.equals(isFull)) {
            return null;
        }
        Map<String, Object> beans = contextRefreshedEvent.getApplicationContext().getBeansWithAnnotation(ShenyuSpringWebSocketClient.class);
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            handler(entry.getValue(), publisher, contextPath, mappingAnnotation, pathAttributeNames, appName);
        }

        System.out.println("exit method get");
        return result;
    }


    private static void handler(final Object bean, ShenyuClientRegisterEventPublisher publisher, String contextPath, List<Class<? extends Annotation>> mappingAnnotation, String[] pathAttributeNames, String appName) {
        Class<?> clazz = bean.getClass();
        if (AopUtils.isAopProxy(bean)) {
            clazz = AopUtils.getTargetClass(bean);
        }
        final String superPath = buildApiSuperPath(clazz);
        final ShenyuSpringWebSocketClient beanShenyuClient = AnnotatedElementUtils.findMergedAnnotation(clazz, ShenyuSpringWebSocketClient.class);
        // Compatible with previous versions
        if (Objects.nonNull(beanShenyuClient) && superPath.contains("*")) {
            publisher.publishEvent(buildMetaDataDTO(beanShenyuClient, pathJoin(contextPath, superPath), contextPath, appName));
            return;
        }
        final Method[] methods = ReflectionUtils.getUniqueDeclaredMethods(clazz);
        for (Method method : methods) {
            ShenyuSpringWebSocketClient webSocketClient = AnnotatedElementUtils.findMergedAnnotation(method, ShenyuSpringWebSocketClient.class);
            webSocketClient = Objects.isNull(webSocketClient) ? beanShenyuClient : webSocketClient;
            if (Objects.nonNull(webSocketClient)) {
                publisher.publishEvent(buildMetaDataDTO(webSocketClient, buildApiPath(method, superPath, contextPath, mappingAnnotation, pathAttributeNames), contextPath, appName));
            }
        }
    }

    private static String buildApiPath(@NonNull final Method method, @NonNull final String superPath, String contextPath, List<Class<? extends Annotation>> mappingAnnotation, String[] pathAttributeNames) {
        ShenyuSpringWebSocketClient webSocketClient = AnnotationUtils.findAnnotation(method, ShenyuSpringWebSocketClient.class);
        if (Objects.nonNull(webSocketClient) && StringUtils.isNotBlank(webSocketClient.path())) {
            return pathJoin(contextPath, superPath, webSocketClient.path());
        }
        final String path = getPathByMethod(method, mappingAnnotation, pathAttributeNames);
        if (StringUtils.isNotBlank(path)) {
            return pathJoin(contextPath, superPath, path);
        }
        return pathJoin(contextPath, superPath);
    }

    private static String getPathByMethod(@NonNull final Method method, List<Class<? extends Annotation>> mappingAnnotation, String[] pathAttributeNames) {
        for (Class<? extends Annotation> mapping : mappingAnnotation) {
            final String pathByAnnotation = getPathByAnnotation(AnnotationUtils.findAnnotation(method, mapping), pathAttributeNames);
            if (StringUtils.isNotBlank(pathByAnnotation)) {
                return pathByAnnotation;
            }
        }
        return null;
    }

    private static String getPathByAnnotation(@Nullable final Annotation annotation,
                                              @NonNull final String... pathAttributeName) {
        if (Objects.isNull(annotation)) {
            return null;
        }
        for (String s : pathAttributeName) {
            final Object value = AnnotationUtils.getValue(annotation, s);
            if (value instanceof String && StringUtils.isNotBlank((String) value)) {
                return (String) value;
            }
            // Only the first path is supported temporarily
            if (value instanceof String[] && ArrayUtils.isNotEmpty((String[]) value) && StringUtils.isNotBlank(((String[]) value)[0])) {
                return ((String[]) value)[0];
            }
        }
        return null;
    }

    private static String buildApiSuperPath(@NonNull final Class<?> method) {
        ShenyuSpringWebSocketClient webSocketClient = AnnotatedElementUtils.findMergedAnnotation(method, ShenyuSpringWebSocketClient.class);
        if (Objects.nonNull(webSocketClient) && StringUtils.isNotBlank(webSocketClient.path())) {
            return webSocketClient.path();
        }
        return "";
    }

    private static String pathJoin(@NonNull final String... path) {
        StringBuilder result = new StringBuilder(PATH_SEPARATOR);
        for (String p : path) {
            if (!result.toString().endsWith(PATH_SEPARATOR)) {
                result.append(PATH_SEPARATOR);
            }
            result.append(p.startsWith(PATH_SEPARATOR) ? p.replaceFirst(PATH_SEPARATOR, "") : p);
        }
        return result.toString();
    }

    private static MetaDataRegisterDTO buildMetaDataDTO(@NonNull final ShenyuSpringWebSocketClient webSocketClient,
                                                        final String path, final String contextPath, String appName) {
        return MetaDataRegisterDTO.builder()
                .contextPath(contextPath)
                .appName(appName)
                .path(path)
                .pathDesc(webSocketClient.desc())
                .rpcType(RpcTypeEnum.WEB_SOCKET.getName())
                .enabled(webSocketClient.enabled())
                .ruleName(StringUtils.defaultIfBlank(webSocketClient.ruleName(), path))
                .registerMetaData(webSocketClient.registerMetaData())
                .build();
    }
}
