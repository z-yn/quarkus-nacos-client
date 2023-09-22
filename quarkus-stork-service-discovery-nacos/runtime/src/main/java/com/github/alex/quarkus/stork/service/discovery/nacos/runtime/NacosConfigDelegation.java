package com.github.alex.quarkus.stork.service.discovery.nacos.runtime;

import java.util.Optional;

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

}
