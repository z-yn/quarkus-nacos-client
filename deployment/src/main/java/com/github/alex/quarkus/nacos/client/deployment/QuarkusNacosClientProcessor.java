package com.github.alex.quarkus.nacos.client.deployment;

import com.github.alex.quarkus.nacos.client.runtime.config.NacosConfigSourceFactoryBuilder;
import com.github.alex.quarkus.nacos.client.runtime.service_discovery.NacosServiceDiscoveryProvider;
import com.github.alex.quarkus.nacos.client.runtime.service_discovery.NacosServiceDiscoveryProviderLoader;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.BeanDefiningAnnotationBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.ExtensionSslNativeSupportBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.RunTimeConfigBuilderBuildItem;
import org.jboss.jandex.DotName;

class QuarkusNacosClientProcessor {

    private static final String FEATURE = "quarkus-nacos-client";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    void enableSsl(BuildProducer<ExtensionSslNativeSupportBuildItem> extensionSslNativeSupport) {
        extensionSslNativeSupport.produce(new ExtensionSslNativeSupportBuildItem(FEATURE));
    }

    @BuildStep
    AdditionalBeanBuildItem additionalBeans() {
        return new AdditionalBeanBuildItem(NacosServiceDiscoveryProvider.class,
                NacosServiceDiscoveryProviderLoader.class);
    }

    BeanDefiningAnnotationBuildItem beanDefiningAnnotation() {
        return new BeanDefiningAnnotationBuildItem(
                DotName.createSimple("com.github.alex.quarkus.nacos.client.runtime.ServiceDiscoveryNacos")
        );
    }

    @BuildStep
    void nacosConfigFactory(BuildProducer<RunTimeConfigBuilderBuildItem> runTimeConfigBuilder) {
        runTimeConfigBuilder.produce(new RunTimeConfigBuilderBuildItem(NacosConfigSourceFactoryBuilder.class.getName()));
    }
}
