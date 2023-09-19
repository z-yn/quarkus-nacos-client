package com.github.alex.quarkus.nacos.config.runtime;

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
    private static final Integer NACOS_CONFIG_TIMEOUT = 30000;
    private final NacosConfigDelegation nacosConfig;
    private final ConfigService configService;
    private final Map<String, Configuration> configMap = new ConcurrentHashMap<>();
    private Map<String, String> mergedConfigs = new HashMap<>();

    NacosConfigSource(ConfigService configService, NacosConfigDelegation nacosConfig) {
        this.nacosConfig = nacosConfig;
        this.configService = configService;
        this.initNacosConfig();
        this.startConfigListener();
    }

    private void startConfigListener() {
        for (String dataId : nacosConfig.configIdListOrdered()) {
            listenConfig(dataId);
        }
    }

    private void listenConfig(String dataId) {
        try {
            configService.addListener(dataId, nacosConfig.group(), new AbstractListener() {
                @Override
                public void receiveConfigInfo(String configInfo) {
                    configMap.put(dataId, new Configuration(dataId, configInfo, nacosConfig.format()));
                    mergeConfig();
                }
            });
        } catch (NacosException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public int getOrdinal() {
        //https://github.com/eclipse/microprofile-config/blob/main/spec/src/main/asciidoc/configsources.asciidoc#custom-configsources-via-configsourceprovider
        return 120; // 配置加载优先级高于本机配置低于环境变量配置
    }

    private void initNacosConfig() {
        try {
            for (String dataId : nacosConfig.configIdListOrdered()) {
                String context = configService.getConfig(dataId, nacosConfig.group(), NACOS_CONFIG_TIMEOUT);
                configMap.put(dataId, new Configuration(dataId, context, nacosConfig.format()));
            }
        } catch (NacosException e) {
            log.error(e.getMessage(), e);
        }
        this.mergeConfig();
    }

    @Override
    public Map<String, String> getProperties() {
        return mergedConfigs;
    }

    @Override
    public Set<String> getPropertyNames() {
        return mergedConfigs.keySet();
    }

    public synchronized void mergeConfig() {
        Map<String, String> all = new HashMap<>();
        for (String s : nacosConfig.configIdListOrdered()) {
            Configuration configuration = this.configMap.get(s);
            if (configuration != null) {
                all.putAll(configuration.configs);
            }
        }
        mergedConfigs = all;
    }

    @Override
    public String getValue(String propertyName) {
        return mergedConfigs.get(propertyName);
    }

    @Override
    public String getName() {
        return String.format(NAME_FORMAT, nacosConfig.appId());
    }
}