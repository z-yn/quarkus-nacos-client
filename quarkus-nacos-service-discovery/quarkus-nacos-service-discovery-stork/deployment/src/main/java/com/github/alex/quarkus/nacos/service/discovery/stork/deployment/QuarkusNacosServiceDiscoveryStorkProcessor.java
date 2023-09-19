package com.github.alex.quarkus.nacos.service.discovery.stork.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class QuarkusNacosServiceDiscoveryStorkProcessor {

    private static final String FEATURE = "quarkus-nacos-service-discovery-stork";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }
}
