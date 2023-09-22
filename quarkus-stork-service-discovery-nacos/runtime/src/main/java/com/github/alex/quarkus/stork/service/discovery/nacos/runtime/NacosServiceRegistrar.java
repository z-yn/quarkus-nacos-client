package com.github.alex.quarkus.stork.service.discovery.nacos.runtime;

import io.smallrye.mutiny.Uni;
import io.smallrye.stork.api.Metadata;
import io.smallrye.stork.api.ServiceRegistrar;
import io.smallrye.stork.spi.StorkInfrastructure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NacosServiceRegistrar implements ServiceRegistrar<NacosMetadataKey> {

    private static final Logger log = LoggerFactory.getLogger(NacosServiceRegistrar.class);
    private final NacosConfigDelegation config;

    public NacosServiceRegistrar(NacosConfigDelegation config, String serviceName,
                                 StorkInfrastructure infrastructure) {
        this.config = config;
    }

    @Override
    public Uni<Void> registerServiceInstance(String serviceName, String ipAddress, int port) {
        return ServiceRegistrar.super.registerServiceInstance(serviceName, ipAddress, port);
    }

    @Override
    public Uni<Void> registerServiceInstance(String s, Metadata<NacosMetadataKey> metadata, String s1, int i) {
        return null;
    }


}
