package com.github.alex.quarkus.nacos.client.runtime.service_discovery;

import io.smallrye.stork.api.ServiceRegistrar;
import io.smallrye.stork.api.config.ServiceRegistrarAttribute;
import io.smallrye.stork.api.config.ServiceRegistrarType;
import io.smallrye.stork.spi.ServiceRegistrarProvider;
import io.smallrye.stork.spi.StorkInfrastructure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ServiceRegistrarType(value = "nacos", metadataKey = NacosMetadataKey.class)
@ServiceRegistrarAttribute(name = "server-addr", description = "The Consul host.", defaultValue = "127.0.0.1:8848")
public class NacosServiceRegistrarProvider
        implements ServiceRegistrarProvider<NacosRegistrarConfiguration, NacosMetadataKey> {

    private static final Logger log = LoggerFactory.getLogger(NacosServiceRegistrarProvider.class);

    @Override
    public ServiceRegistrar<NacosMetadataKey> createServiceRegistrar(NacosRegistrarConfiguration config,
                                                                     String serviceRegistrarName, StorkInfrastructure infrastructure) {
        return new NacosServiceRegistrar(config, serviceRegistrarName, infrastructure);
    }

}