package org.apache.shenyu.client.spring.websocket.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface shenyu client.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ShenyuSpringWebSocketClient {

    /**
     * value string.
     * @return the string
     */
    @AliasFor(attribute = "path")
    String value() default "";

    /**
     * Path string.
     *
     * @return the string
     */
    @AliasFor(attribute = "value")
    String path() default "";

    /**
     * Rule name string.
     *
     * @return the string
     */
    String ruleName() default "";

    /**
     * Desc string.
     *
     * @return String string
     */
    String desc() default "";

    /**
     * Enabled boolean.
     *
     * @return the boolean
     */
    boolean enabled() default true;

    /**
     * Register meta data boolean.
     *
     * @return the boolean
     */
    boolean registerMetaData() default false;
}