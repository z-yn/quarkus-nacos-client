package com.github.alex.quarkus.nacos.client.runtime;

import java.util.*;

public class NacosConfigDelegation {
    private final NacosConfig config;

    public NacosConfigDelegation(NacosConfig config) {
        this.config = config;
    }

    public String appId() {
        return config.appId().orElseThrow();
    }

    public String group() {
        return config.group().orElse("DEFAULT_GROUP");
    }

    public Optional<String> namespace() {
        return config.namespace();
    }

    public String serverAddr() {
        return config.serverAddr().orElseThrow();
    }

    public Optional<String> username() {
        return config.username();
    }

    public Optional<String> password() {
        return config.password();
    }

    public ConfigFileFormat format() {
        return config.format().orElse(ConfigFileFormat.properties);
    }

    public String profile() {
        return config.profile().orElse(null);
    }

    public boolean enabled() {
        return config.enabled();
    }

    public String dataId() {
        return appId() + "." + format();
    }

    public List<String> configIdListOrdered() {
        List<String> list = new ArrayList<>();
        list.add("application." + format());
        config.profile().ifPresent(it -> list.add("application-" + it + "." + format()));
        list.add(appId() + "." + format());
        config.profile().ifPresent(it -> list.add(appId() + "-" + it + "." + format()));
        return list;
    }

    public Map<String, String> storkConfig() {
        Map<String, String> storkConfig = new HashMap<>();
        storkConfig.put("quarkus.stork.*.service-discovery.type", "nacos");
        storkConfig.put("quarkus.stork.*.service-discovery.server-addr", serverAddr());
        username().ifPresent(it -> storkConfig.put("quarkus.stork.*.service-discovery.username", it));
        password().ifPresent(it -> storkConfig.put("quarkus.stork.*.service-discovery.password", it));
        namespace().ifPresent(it -> storkConfig.put("quarkus.stork.*.service-discovery.nacos-namespace", it));
        storkConfig.put("quarkus.stork.*.service-discovery.nacos-group", group());
        return storkConfig;
    }
}
