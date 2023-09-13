package com.github.alex.quarkus.nacos.client.runtime;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.AbstractListener;
import com.alibaba.nacos.api.exception.NacosException;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.jboss.logging.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class NacosConfigSource implements ConfigSource {
    private static final Logger log = Logger.getLogger(NacosConfigSource.class);

    private static final String NAME_FORMAT = "NacosConfigSource[%s]";
    private static final Integer GET_CONFIG_TIMEOUT = 1000 * 60; //60s
    private final ConfigParser nacosConfig;
    private final ConfigService configService;
    private final Map<String, String> configMap = new ConcurrentHashMap<>();

    NacosConfigSource(ConfigService configService, ConfigParser nacosConfig) {
        this.nacosConfig = nacosConfig;
        this.configService = configService;
        this.configMap.putAll(this.initNacosConfig());
        this.startConfigListener();
    }

    private void startConfigListener() {
        try {
            configService.addListener(nacosConfig.dataId(), nacosConfig.group(), new AbstractListener() {
                @Override
                public void receiveConfigInfo(String configInfo) {
                    configMap.putAll(NacosUtils.stringToMap(configInfo, nacosConfig.format()));
                }
            });
        } catch (NacosException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public int getOrdinal() {
        //https://github.com/eclipse/microprofile-config/blob/main/spec/src/main/asciidoc/configsources.asciidoc#custom-configsources-via-configsourceprovider
        return 200; // 配置加载优先级高于本机配置低于环境变量配置
    }

    private Map<String, String> initNacosConfig() {
        try {
            String context = configService.getConfig(nacosConfig.dataId(), nacosConfig.group(), GET_CONFIG_TIMEOUT);
            return NacosUtils.stringToMap(context, nacosConfig.format());
        } catch (NacosException e) {
            log.error(e.getMessage(), e);
        }
        return new HashMap<>();
    }

    @Override
    public Map<String, String> getProperties() {
        return configMap;
    }

    @Override
    public Set<String> getPropertyNames() {
        return configMap.keySet();
    }

    @Override
    public String getValue(String propertyName) {
        return configMap.get(propertyName);
    }

    @Override
    public String getName() {
        return String.format(NAME_FORMAT, nacosConfig.appId());
    }
}