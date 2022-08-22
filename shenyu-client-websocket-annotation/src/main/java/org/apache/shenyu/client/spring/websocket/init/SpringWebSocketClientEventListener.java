/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shenyu.client.spring.websocket.init;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shenyu.client.core.constant.ShenyuClientConstants;
import org.apache.shenyu.client.core.disruptor.ShenyuClientRegisterEventPublisher;
import org.apache.shenyu.client.core.exception.ShenyuClientIllegalArgumentException;
import org.apache.shenyu.client.spring.websocket.annotation.ShenyuSpringWebSocketClient;
import org.apache.shenyu.common.enums.RpcTypeEnum;
import org.apache.shenyu.register.client.api.ShenyuClientRegisterRepository;
import org.apache.shenyu.register.common.config.PropertiesConfig;
import org.apache.shenyu.register.common.dto.MetaDataRegisterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * The type Shenyu websocket client event listener.
 */
public class SpringWebSocketClientEventListener implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * api path separator.
     */
    private static final String PATH_SEPARATOR = "/";

    private static final Logger LOG = LoggerFactory.getLogger(SpringWebSocketClientEventListener.class);

    private final ShenyuClientRegisterEventPublisher publisher = ShenyuClientRegisterEventPublisher.getInstance();

    private final String contextPath;

    private final String appName;

    private final Boolean isFull;

    private final List<Class<? extends Annotation>> mappingAnnotation = new ArrayList<>(7);

    private final String[] pathAttributeNames = new String[] {"path", "value"};

    /**
     * Instantiates a new Spring websocket client event listener.
     *
     * @param clientConfig                   the client config
     * @param shenyuClientRegisterRepository the shenyu client register repository
     */
    public SpringWebSocketClientEventListener(final PropertiesConfig clientConfig,
                                              final ShenyuClientRegisterRepository shenyuClientRegisterRepository) {

        Properties props = clientConfig.getProps();
        this.appName = props.getProperty(ShenyuClientConstants.APP_NAME);
        this.contextPath = props.getProperty(ShenyuClientConstants.CONTEXT_PATH, "");
        if (StringUtils.isBlank(appName) && StringUtils.isBlank(contextPath)) {
            String errorMsg = "websocket register param must config the appName or contextPath";
            LOG.error(errorMsg);
            throw new ShenyuClientIllegalArgumentException(errorMsg);
        }
        this.isFull = Boolean.parseBoolean(props.getProperty(ShenyuClientConstants.IS_FULL, Boolean.FALSE.toString()));
        mappingAnnotation.add(ShenyuSpringWebSocketClient.class);

        publisher.start(shenyuClientRegisterRepository);
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent contextRefreshedEvent) {
        LOG.info("======version: 2.5.0");

    }

    private void handler(final Object bean) {

    }

    private String buildApiPath(@NonNull final Method method, @NonNull final String superPath) {
        return null;
    }

    private String getPathByMethod(@NonNull final Method method) {
        return null;
    }

    private String getPathByAnnotation(@Nullable final Annotation annotation, @NonNull final String... pathAttributeName) {
        return null;
    }

    private String buildApiSuperPath(@NonNull final Class<?> method) {
        return null;
    }

    private String pathJoin(@NonNull final String... path) {
        return null;
    }

    private MetaDataRegisterDTO buildMetaDataDTO(@NonNull final ShenyuSpringWebSocketClient webSocketClient, final String path) {
        return null;
    }

}
