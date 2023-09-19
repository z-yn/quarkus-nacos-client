package com.github.alex.quarkus.nacos.service.discovery.stork.runtime;

import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import io.smallrye.mutiny.Uni;
import io.smallrye.stork.api.ServiceDiscovery;
import io.smallrye.stork.api.ServiceInstance;
import io.smallrye.stork.impl.DefaultServiceInstance;
import org.jboss.logging.Logger;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class NacosServiceDiscovery implements ServiceDiscovery {
    private static final Logger log = Logger.getLogger(NacosServiceDiscovery.class);
    private final String serviceName;
    private final NamingService namingService;
    private Uni<List<ServiceInstance>> serviceInstances;


    public NacosServiceDiscovery(NacosConfiguration nacosConfig, String serviceName) throws NacosException {
        this.serviceName = serviceName;
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.SERVER_ADDR, nacosConfig.getServerAddr());
        properties.setProperty(PropertyKeyConst.NAMESPACE, Optional.ofNullable(nacosConfig.getNamespace())
                .orElse(""));
        Optional.ofNullable(nacosConfig.getUsername())
                .ifPresent(it -> properties.setProperty(PropertyKeyConst.USERNAME, it));
        Optional.ofNullable(nacosConfig.getPassword())
                .ifPresent(it -> properties.setProperty(PropertyKeyConst.PASSWORD, it));
        namingService = NamingFactory.createNamingService(properties);
        this.fetchNewServiceInstances();
        this.subscribeService();
    }

    private void subscribeService() throws NacosException {
        namingService.subscribe(serviceName, event -> fetchNewServiceInstances());
    }


    private void fetchNewServiceInstances() {
        this.serviceInstances = Uni.createFrom().emitter(emitter -> {
                    try {

                        List<Instance> instances = namingService.selectInstances(serviceName, true);
                        List<ServiceInstance> serviceInstances = new ArrayList<>();
                        AtomicLong id = new AtomicLong(1);
                        instances.stream().sorted(Comparator.comparing(Instance::getInstanceId))
                                .forEachOrdered(it ->
                                        serviceInstances.add(new DefaultServiceInstance(id.getAndIncrement(), it.getIp(), it.getPort(), false))
                                );
                        emitter.complete(serviceInstances);
                    } catch (NacosException e) {
                        emitter.complete(Collections.emptyList());
                        log.error(e.getMessage(), e);
                    }
                }
        );
    }

    @Override
    public Uni<List<ServiceInstance>> getServiceInstances() {
        return serviceInstances;
    }
}
