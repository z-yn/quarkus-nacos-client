package com.github.alex.quarkus.stork.service.discovery.nacos.deployment;

import com.github.alex.quarkus.stork.service.discovery.nacos.runtime.NacosServiceDiscoveryProvider;
import com.github.alex.quarkus.stork.service.discovery.nacos.runtime.NacosServiceDiscoveryProviderLoader;
import com.github.alex.quarkus.stork.service.discovery.nacos.runtime.ServiceName;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.BytecodeTransformerBuildItem;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.RunTimeConfigurationDefaultBuildItem;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.IndexView;

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

    //    @BuildStep
//    尝试使用重写class实现，发现这个重写应该是类加载的时候重写
    void transform(CombinedIndexBuildItem indexBuildItem,
                   BuildProducer<BytecodeTransformerBuildItem> transformCode) {
        IndexView index = indexBuildItem.getIndex();
        for (AnnotationInstance annotation : index.getAnnotations(DotName.createSimple(ServiceName.class))) {
            ClassInfo target = annotation.target().asClass();
            if (target.isInterface()) {
                transformCode.produce(
                        new BytecodeTransformerBuildItem(true, target.name().toString(), (s, cv) -> new AddStorkRegistryRestClient(cv, annotation.value().asString()))
                );

            }
        }
    }

    @BuildStep
    void defaultConfig(CombinedIndexBuildItem indexBuildItem,
                       BuildProducer<RunTimeConfigurationDefaultBuildItem> runTimeConfig) {
        IndexView index = indexBuildItem.getIndex();
        for (AnnotationInstance annotation : index.getAnnotations(DotName.createSimple(ServiceName.class))) {
            ClassInfo target = annotation.target().asClass();
            String serviceName = annotation.value().asString();
            if (target.isInterface()) {
                runTimeConfig.produce(new RunTimeConfigurationDefaultBuildItem(
                        "quarkus.rest-client.\"" + target.name().toString() + "\".url",
                        "stork://" + serviceName
                ));
                runTimeConfig.produce(new RunTimeConfigurationDefaultBuildItem(
                        "quarkus.stork." + serviceName + ".service-discovery.type",
                        "nacos"
                ));

            }
        }
    }
}
