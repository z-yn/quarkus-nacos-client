package com.github.alex.quarkus.nacos.service.discovery.stork.runtime.processor;

import com.github.alex.quarkus.nacos.service.discovery.stork.runtime.NacosService;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;

@RegisterRestClient(baseUri = "")
@SupportedAnnotationTypes("com.github.alex.quarkus.nacos.service.discovery.stork.runtime.NacosService")
public class ServiceDiscoveryProcessor extends AbstractProcessor {

    private String getPackageName(Element element) {
        return ElementFilter.packagesIn(Collections.singleton(element.getEnclosingElement()))
                .stream().map(it -> it.getQualifiedName().toString()).findAny().orElse("");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            return false;
        }
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(NacosService.class);
        for (Element element : elements) {
            if (element.getKind().isInterface()) {
                String packageName = getPackageName(element);
                NacosService annotation = element.getAnnotation(NacosService.class);
                if (annotation != null) {
                    String elementName = element.getSimpleName().toString();
                    var className = elementName + "Impl";
                    String serviceName = annotation.value();
                    Properties properties = new Properties();
                    properties.setProperty("className", packageName + "." + className);
                    properties.setProperty("serviceName", serviceName);
                    FileObject propertiesFile;
                    try {
                        propertiesFile = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "nacos-service-discovery/" + packageName + "." + className + ".properties");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try (Writer writer = propertiesFile.openWriter()) {
                        properties.store(writer, "");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }
        return true;
    }
}
