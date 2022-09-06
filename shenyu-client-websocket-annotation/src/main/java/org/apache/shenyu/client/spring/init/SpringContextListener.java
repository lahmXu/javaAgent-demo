package org.apache.shenyu.client.spring.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;

/**
 * The type Context register listener for websocket.
 */
@Configuration
public class SpringContextListener implements ApplicationListener<ContextRefreshedEvent>,BeanFactoryAware {

    private static final Logger LOG = LoggerFactory.getLogger(SpringContextListener.class);

    private static ApplicationContext applicationContext;

    private static BeanFactory beanFactory;


    @Override
    public void onApplicationEvent(@NonNull final ContextRefreshedEvent contextRefreshedEvent) {
        this.applicationContext = contextRefreshedEvent.getApplicationContext();
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}