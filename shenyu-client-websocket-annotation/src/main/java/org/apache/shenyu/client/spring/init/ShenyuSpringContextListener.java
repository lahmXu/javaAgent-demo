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

public class ShenyuSpringContextListener implements ApplicationListener<ContextRefreshedEvent>, BeanFactoryAware {

    private static final Logger LOG = LoggerFactory.getLogger(ShenyuSpringContextListener.class);

    private ApplicationContext applicationContext;

    private BeanFactory beanFactory;

    @Override
    public void onApplicationEvent(@NonNull final ContextRefreshedEvent contextRefreshedEvent) {
        this.applicationContext = contextRefreshedEvent.getApplicationContext();
        init();
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public void init() {
        LOG.info("origin init...");
    }
}