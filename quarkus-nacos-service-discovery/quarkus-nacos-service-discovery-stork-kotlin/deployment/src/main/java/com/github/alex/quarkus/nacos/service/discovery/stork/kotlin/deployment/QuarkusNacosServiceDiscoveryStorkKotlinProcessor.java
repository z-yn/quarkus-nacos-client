package com.github.alex.quarkus.nacos.service.discovery.stork.kotlin.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class QuarkusNacosServiceDiscoveryStorkKotlinProcessor {

    private static final String FEATURE = "quarkus-nacos-service-discovery-stork-kotlin";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }
}
