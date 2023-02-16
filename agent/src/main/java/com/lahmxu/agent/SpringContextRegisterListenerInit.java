package com.lahmxu.agent;

import org.apache.commons.lang3.StringUtils;
import org.apache.shenyu.client.core.constant.ShenyuClientConstants;
import org.apache.shenyu.client.core.disruptor.ShenyuClientRegisterEventPublisher;
import org.apache.shenyu.client.core.exception.ShenyuClientIllegalArgumentException;
import org.apache.shenyu.common.enums.RpcTypeEnum;
import org.apache.shenyu.common.exception.ShenyuException;
import org.apache.shenyu.common.utils.IpUtils;
import org.apache.shenyu.common.utils.PathUtils;
import org.apache.shenyu.common.utils.PortUtils;
import org.apache.shenyu.register.common.config.PropertiesConfig;
import org.apache.shenyu.register.common.dto.MetaDataRegisterDTO;
import org.apache.shenyu.register.common.dto.URIRegisterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;

import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class SpringContextRegisterListenerInit {

    private static final Logger LOG = LoggerFactory.getLogger(SpringContextRegisterListenerInit.class);

    private static final ShenyuClientRegisterEventPublisher publisher = ShenyuClientRegisterEventPublisher.getInstance();

    private static final AtomicBoolean registered = new AtomicBoolean(false);

    private String contextPath;

    private String appName;

    private String protocol;

    private String host;

    private Integer port;

    private Boolean isFull;

    private BeanFactory beanFactory;


    /**
     * Instantiates a new Context register listener for websocket.
     *
     * @param clientConfig the client config
     */
    public SpringContextRegisterListenerInit(final PropertiesConfig clientConfig, final BeanFactory beanFactory) throws Exception {
        final Properties props = clientConfig.getProps();
        this.isFull = Boolean.parseBoolean(props.getProperty(ShenyuClientConstants.IS_FULL, Boolean.FALSE.toString()));
        this.contextPath = props.getProperty(ShenyuClientConstants.CONTEXT_PATH);
        if (Boolean.TRUE.equals(isFull)) {
            if (StringUtils.isBlank(contextPath)) {
                final String errorMsg = "websocket register param must config the contextPath";
                LOG.error(errorMsg);
                throw new ShenyuClientIllegalArgumentException(errorMsg);
            }
        }
        this.port = Integer.parseInt(Optional.ofNullable(props.getProperty(ShenyuClientConstants.PORT)).orElseGet(() -> "-1"));
        this.appName = props.getProperty(ShenyuClientConstants.APP_NAME);
        this.protocol = props.getProperty(ShenyuClientConstants.PROTOCOL, ShenyuClientConstants.WS);

        this.host = props.getProperty(ShenyuClientConstants.HOST);
        this.beanFactory = beanFactory;
    }

    public void init() throws Throwable {
        if (!registered.compareAndSet(false, true)) {
            return;
        }
        if (Boolean.TRUE.equals(isFull)) {
            publisher.publishEvent(buildMetaDataDTO());
        }
        try {
            final int mergedPort = port <= 0 ? PortUtils.findPort(beanFactory) : port;
            publisher.publishEvent(buildURIRegisterDTO(mergedPort));
        } catch (ShenyuException e) {
            throw new ShenyuException(e.getMessage() + "please config ${shenyu.client.websocket.props.port} in xml/yml !");
        }
    }

    private URIRegisterDTO buildURIRegisterDTO(final int port) {
        return URIRegisterDTO.builder()
                .contextPath(this.contextPath)
                .appName(appName)
                .protocol(protocol)
                .host(IpUtils.isCompleteHost(this.host) ? this.host : IpUtils.getHost(this.host))
                .port(port)
                .rpcType(RpcTypeEnum.WEB_SOCKET.getName())
                .build();
    }

    private MetaDataRegisterDTO buildMetaDataDTO() {
        return MetaDataRegisterDTO.builder()
                .contextPath(contextPath)
                .appName(appName)
                .path(PathUtils.decoratorPathWithSlash(contextPath))
                .rpcType(RpcTypeEnum.WEB_SOCKET.getName())
                .enabled(true)
                .ruleName(contextPath)
                .build();
    }


}
