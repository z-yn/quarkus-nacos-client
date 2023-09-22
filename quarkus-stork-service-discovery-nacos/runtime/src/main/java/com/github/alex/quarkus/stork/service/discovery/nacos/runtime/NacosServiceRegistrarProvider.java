package com.github.alex.quarkus.stork.service.discovery.nacos.runtime;

import io.smallrye.stork.api.ServiceRegistrar;
import io.smallrye.stork.api.config.ServiceRegistrarType;
import io.smallrye.stork.spi.ServiceRegistrarProvider;
import io.smallrye.stork.spi.StorkInfrastructure;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ServiceRegistrarType(value = "nacos", metadataKey = NacosMetadataKey.class)
@ApplicationScoped
public class NacosServiceRegistrarProvider
        implements ServiceRegistrarProvider<NacosRegistrarConfiguration, NacosMetadataKey> {
    private static final Logger log = LoggerFactory.getLogger(NacosServiceRegistrarProvider.class);
    @Inject
    NacosConfig config;

    @Override
    public ServiceRegistrar<NacosMetadataKey> createServiceRegistrar(NacosRegistrarConfiguration registrarConfiguration,
                                                                     String serviceRegistrarName, StorkInfrastructure infrastructure) {
        return new NacosServiceRegistrar(new NacosConfigDelegation(config), serviceRegistrarName, infrastructure);
    }

}