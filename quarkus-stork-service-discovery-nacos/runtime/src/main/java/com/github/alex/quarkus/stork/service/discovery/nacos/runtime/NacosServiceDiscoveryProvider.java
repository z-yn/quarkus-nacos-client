package com.github.alex.quarkus.stork.service.discovery.nacos.runtime;

import com.alibaba.nacos.api.exception.NacosException;
import io.smallrye.stork.api.ServiceDiscovery;
import io.smallrye.stork.api.config.ConfigWithType;
import io.smallrye.stork.api.config.ServiceConfig;
import io.smallrye.stork.api.config.ServiceDiscoveryType;
import io.smallrye.stork.spi.ServiceDiscoveryProvider;
import io.smallrye.stork.spi.StorkInfrastructure;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ServiceDiscoveryType("nacos")
@ApplicationScoped
public class NacosServiceDiscoveryProvider implements ServiceDiscoveryProvider<ConfigWithType> {
    @Inject
    NacosConfig config;

    @Override
    public ServiceDiscovery createServiceDiscovery(ConfigWithType nacosConfig,
                                                   String serviceName,
                                                   ServiceConfig serviceConfig,
                                                   StorkInfrastructure storkInfrastructure) {
        try {
            return new NacosServiceDiscovery(new NacosConfigDelegation(config), serviceName);
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }
}
