package org.apache.shenyu.client.spring.websocket.agent;

import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import org.apache.commons.lang3.StringUtils;
import org.apache.shenyu.client.spring.websocket.AgentDemo;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ComponentScanAnnotationInterceptor {

    private static final String BASEPACKAGES = "basePackages";

    private static final String DEFAULT_SHENYU_CLIENT_PACKAGE = "org.apache.shenyu.client";

    private static final String PACKAGE_SEPARATOR = ".";

    @Advice.OnMethodEnter()
    public static void enter(@Advice.AllArguments(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object[] args) throws Throwable {
        List<Object> baseScan = null;
        String userPackage = null;
        for (int i = args.length-1; i >= 0 ; i--) {
            if(args[i] instanceof LinkedHashMap){
                Map maps = (Map) args[i];
                Object[] originArray = (Object[]) maps.get(BASEPACKAGES);

                baseScan = new ArrayList<Object>(originArray.length);
                Collections.addAll(baseScan, originArray);

                if ((CollectionUtils.isEmpty(baseScan) || originArray.length == 0) && StringUtils.isNotBlank(userPackage)) {
                    baseScan.add(userPackage);
                }
                if (StringUtils.isNotBlank(AgentDemo.componentScanPath)) {
                    baseScan.add(AgentDemo.componentScanPath);
                }
                baseScan.add(DEFAULT_SHENYU_CLIENT_PACKAGE);

                maps.put(BASEPACKAGES, baseScan.toArray(new String[baseScan.size()]));
            } else if (args[i] instanceof String) {
                String mainClass = (String) args[i];
                if (mainClass.contains(PACKAGE_SEPARATOR)){
                    userPackage = StringUtils.substringBeforeLast(mainClass, PACKAGE_SEPARATOR);
                }
            }
        }
    }
}
