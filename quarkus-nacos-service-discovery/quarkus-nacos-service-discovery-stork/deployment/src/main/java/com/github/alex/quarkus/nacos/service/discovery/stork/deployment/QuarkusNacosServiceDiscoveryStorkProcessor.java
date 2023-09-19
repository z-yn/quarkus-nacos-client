package com.github.alex.quarkus.nacos.service.discovery.stork.deployment;

import com.github.alex.quarkus.nacos.service.discovery.stork.runtime.NacosServiceDiscoveryProvider;
import com.github.alex.quarkus.nacos.service.discovery.stork.runtime.NacosServiceDiscoveryProviderLoader;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class QuarkusNacosServiceDiscoveryStorkProcessor {

    private static final String FEATURE = "quarkus-nacos-service-discovery-stork";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    AdditionalBeanBuildItem additionalBeans() {
        return new AdditionalBeanBuildItem(NacosServiceDiscoveryProvider.class,
                NacosServiceDiscoveryProviderLoader.class);
    }

}
