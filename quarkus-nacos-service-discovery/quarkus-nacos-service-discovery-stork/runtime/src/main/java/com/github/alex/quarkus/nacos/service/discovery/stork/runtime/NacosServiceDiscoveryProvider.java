package com.github.alex.quarkus.nacos.service.discovery.stork.runtime;

import com.alibaba.nacos.api.exception.NacosException;
import io.smallrye.stork.api.ServiceDiscovery;
import io.smallrye.stork.api.config.ServiceConfig;
import io.smallrye.stork.api.config.ServiceDiscoveryAttribute;
import io.smallrye.stork.api.config.ServiceDiscoveryType;
import io.smallrye.stork.spi.ServiceDiscoveryProvider;
import io.smallrye.stork.spi.StorkInfrastructure;
import jakarta.enterprise.context.ApplicationScoped;

@ServiceDiscoveryType("nacos")
@ServiceDiscoveryAttribute(name = "server-addr",
        description = "Host name of the service discovery server.", required = true)
@ServiceDiscoveryAttribute(name = "username",
        description = "password")
@ServiceDiscoveryAttribute(name = "password",
        description = "username")
@ServiceDiscoveryAttribute(name = "group", description = "nacos group")
@ServiceDiscoveryAttribute(name = "namespace", description = "nacos namespace")
@ApplicationScoped
public class NacosServiceDiscoveryProvider implements ServiceDiscoveryProvider<NacosConfiguration> {
    @Override
    public ServiceDiscovery createServiceDiscovery(NacosConfiguration nacosConfig,
                                                   String serviceName,
                                                   ServiceConfig serviceConfig,
                                                   StorkInfrastructure storkInfrastructure) {
        try {
            return new NacosServiceDiscovery(nacosConfig, serviceName);
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }
}
