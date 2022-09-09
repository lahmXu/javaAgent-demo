package com.lahmxu.agent;

import org.apache.shenyu.client.core.register.ShenyuClientRegisterRepositoryFactory;
import org.apache.shenyu.client.spring.init.SpringContextListener;
import org.apache.shenyu.register.client.api.ShenyuClientRegisterRepository;
import org.apache.shenyu.register.common.config.PropertiesConfig;
import org.apache.shenyu.register.common.config.ShenyuClientConfig;
import org.apache.shenyu.register.common.config.ShenyuRegisterCenterConfig;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.util.ReflectionUtils;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AgentDemo {

    private static final ScheduledExecutorService scheduleExecutor = new ScheduledThreadPoolExecutor(1);

    public static void premain(String agentArgs, Instrumentation inst) throws Throwable {
        System.out.println("-------------------agent main start-------------------");

        scheduleExecutor.schedule(() -> init(inst), 10, TimeUnit.SECONDS);



        System.out.println("-------------------agent main end-------------------");
    }

    private static void init(Instrumentation instrumentation) {
        // Get applicationContext
        Field applicationContextField = ReflectionUtils.findField(SpringContextListener.class, "applicationContext");
        ReflectionUtils.makeAccessible(applicationContextField);
        ApplicationContext applicationContext = (ApplicationContext) ReflectionUtils.getField(applicationContextField, SpringContextListener.class);

        // Get yaml config
        Environment envs = applicationContext.getBean(Environment.class);
        Field beanFactoryField = ReflectionUtils.findField(SpringContextListener.class, "beanFactory");
        ReflectionUtils.makeAccessible(beanFactoryField);
        BeanFactory beanFactory = (BeanFactory) ReflectionUtils.getField(beanFactoryField, SpringContextListener.class);
        PropertiesConfig clientConfig = fetchShenyuClientConfig(envs);
        ShenyuRegisterCenterConfig shenyuRegisterCenterConfig = fetchShenyuRegisterCenterConfig(envs);
        ShenyuClientRegisterRepository shenyuClientRegisterRepository = ShenyuClientRegisterRepositoryFactory.newInstance(shenyuRegisterCenterConfig);

        // Active Listener
        SpringWebSocketCLientEventListenerInit springWebSocketClientEventListener = new SpringWebSocketCLientEventListenerInit(clientConfig, shenyuClientRegisterRepository, applicationContext);
        springWebSocketClientEventListener.init();
        try {
            SpringContextRegisterListenerInit registerListener = new SpringContextRegisterListenerInit(clientConfig, beanFactory);
            registerListener.init();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static ShenyuRegisterCenterConfig fetchShenyuRegisterCenterConfig(Environment environment){
        ShenyuRegisterCenterConfig propertiesConfig = new ShenyuRegisterCenterConfig();
        String registerType = environment.getProperty("shenyu.register.registerType");
        String serverLists = environment.getProperty("shenyu.register.serverLists");
        String username = environment.getProperty("shenyu.register.props.username");
        String password = environment.getProperty("shenyu.register.props.password");
        Properties props = new Properties();
        props.setProperty("username", username);
        props.setProperty("password", password);
        propertiesConfig.setRegisterType(registerType);
        propertiesConfig.setServerLists(serverLists);
        propertiesConfig.setProps(props);
        return propertiesConfig;
    }

    private static PropertiesConfig fetchShenyuClientConfig(Environment environment){
        PropertiesConfig propertiesConfig = new PropertiesConfig();
        Properties props = new Properties();
        props.setProperty("contextPath", environment.getProperty("shenyu.client.websocket.props.contextPath"));
        props.setProperty("appName", environment.getProperty("shenyu.client.websocket.props.appName"));
        props.setProperty("port", environment.getProperty("shenyu.client.websocket.props.port"));
        propertiesConfig.setProps(props);
        return propertiesConfig;
    }
}
