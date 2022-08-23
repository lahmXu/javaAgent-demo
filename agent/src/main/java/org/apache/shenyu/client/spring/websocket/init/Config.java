package org.apache.shenyu.client.spring.websocket.init;

import org.apache.shenyu.client.core.disruptor.ShenyuClientRegisterEventPublisher;
import org.apache.shenyu.client.core.register.ShenyuClientRegisterRepositoryFactory;
import org.apache.shenyu.common.enums.RpcTypeEnum;
import org.apache.shenyu.register.client.api.ShenyuClientRegisterRepository;
import org.apache.shenyu.register.common.config.ShenyuClientConfig;
import org.apache.shenyu.register.common.config.ShenyuRegisterCenterConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "shenyu.register.enabled", matchIfMissing = true, havingValue = "true")
public class Config {

    /**
     * Register the register repository for http client bean post processor.
     *
     * @param config the config
     * @return the client register repository
     */
    @Bean
    public ShenyuClientRegisterRepository shenyuClientRegisterRepository(final ShenyuRegisterCenterConfig config) {
        return ShenyuClientRegisterRepositoryFactory.newInstance(config);
    }

    /**
     * Shenyu Register Center Config.
     *
     * @return the Register Center Config
     */
    @Bean
    @ConfigurationProperties(prefix = "shenyu.register")
    public ShenyuRegisterCenterConfig shenyuRegisterCenterConfig() {
        return new ShenyuRegisterCenterConfig();
    }

    /**
     * Shenyu client config.
     *
     * @return the shenyu client config
     */
    @Bean
    @ConfigurationProperties(prefix = "shenyu")
    public ShenyuClientConfig shenyuClientConfig() {
        return new ShenyuClientConfig();
    }

    /**
     * Spring web socket client event listener.
     *
     * @param clientConfig                   the client config
     * @param shenyuClientRegisterRepository the shenyu client register repository
     * @return the spring web socket client event listener
     */
    @Bean
    public SpringWebSocketClientEventListener springWebSocketClientEventListener(
            final ShenyuClientConfig clientConfig,
            final ShenyuClientRegisterRepository shenyuClientRegisterRepository) {
        return new SpringWebSocketClientEventListener(clientConfig.getClient().get(RpcTypeEnum.WEB_SOCKET.getName()), shenyuClientRegisterRepository);
    }

    /**
     * Context register listener.
     *
     * @param clientConfig the client config
     * @return the spring context register listener
     */
    @Bean
    public SpringContextRegisterListener webSocketContextRegisterListener(final ShenyuClientConfig clientConfig) {
        return new SpringContextRegisterListener(clientConfig.getClient().get(RpcTypeEnum.WEB_SOCKET.getName()));
    }

}
