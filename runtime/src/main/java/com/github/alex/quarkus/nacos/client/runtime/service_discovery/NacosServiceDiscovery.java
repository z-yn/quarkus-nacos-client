package com.github.alex.quarkus.nacos.client.runtime.service_discovery;

import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.github.alex.quarkus.nacos.client.runtime.config.NacosConfigSource;
import io.smallrye.mutiny.Uni;
import io.smallrye.stork.api.ServiceInstance;
import io.smallrye.stork.impl.CachingServiceDiscovery;
import io.smallrye.stork.impl.DefaultServiceInstance;
import org.jboss.logging.Logger;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class NacosServiceDiscovery extends CachingServiceDiscovery {
    private static final Logger log = Logger.getLogger(NacosConfigSource.class);
    private final NacosConfiguration nacosConfig;
    private final String serviceName;


    public NacosServiceDiscovery(NacosConfiguration nacosConfig, String serviceName) {
        super(nacosConfig.getRefreshPeriod());
        this.nacosConfig = nacosConfig;
        this.serviceName = serviceName;
    }

    @Override
    public Uni<List<ServiceInstance>> fetchNewServiceInstances(List<ServiceInstance> list) {
        try {
            Properties properties = new Properties();
            properties.setProperty(PropertyKeyConst.SERVER_ADDR, nacosConfig.getServerAddr());
            properties.setProperty(PropertyKeyConst.NAMESPACE, Optional.ofNullable(nacosConfig.getNacosNamespace())
                    .orElse(""));
            Optional.ofNullable(nacosConfig.getUsername())
                    .ifPresent(it -> properties.setProperty(PropertyKeyConst.USERNAME, it));
            Optional.ofNullable(nacosConfig.getPassword())
                    .ifPresent(it -> properties.setProperty(PropertyKeyConst.PASSWORD, it));
            NamingService naming = NamingFactory.createNamingService(properties);
            List<Instance> instances = naming.selectInstances(serviceName, true);
            List<ServiceInstance> serviceInstances = new ArrayList<>();
            AtomicLong id = new AtomicLong(1);
            instances.stream().sorted(Comparator.comparing(Instance::getInstanceId))
                    .forEachOrdered(it ->
                            serviceInstances.add(new DefaultServiceInstance(id.getAndIncrement(), it.getIp(), it.getPort(), false))
                    );
            return Uni.createFrom().item(
                    serviceInstances
            );
        } catch (NacosException e) {
            log.error(e.getMessage(), e);
        }
        return Uni.createFrom().item(Collections.emptyList());
    }

}
