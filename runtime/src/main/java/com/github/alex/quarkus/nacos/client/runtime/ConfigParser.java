package com.github.alex.quarkus.nacos.client.runtime;

import java.util.Optional;

public class ConfigParser {
    private final NacosConfig config;

    public ConfigParser(NacosConfig config) {
        this.config = config;
    }

    public String appId() {
        return config.appId().orElseThrow();
    }

    public String group() {
        return config.group().orElse("DEFAULT_GROUP");
    }

    public String namespace() {
        return config.namespace().orElse("");
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

    public NacosConfig.Format format() {
        return config.format().orElse(NacosConfig.Format.properties);
    }

    public String profile() {
        return config.profile().orElse("");
    }

    public boolean enabled() {
        return config.enabled();
    }

    public String dataId() {
        return appId() + "." + format();
    }
}
