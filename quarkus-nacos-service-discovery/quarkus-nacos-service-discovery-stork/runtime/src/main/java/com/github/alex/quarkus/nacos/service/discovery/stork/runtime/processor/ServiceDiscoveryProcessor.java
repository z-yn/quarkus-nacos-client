package com.github.alex.quarkus.nacos.service.discovery.stork.runtime.processor;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;
import java.util.Set;

@SupportedAnnotationTypes("com.github.alex.quarkus.nacos.service.discovery.stork.runtime.ServiceDiscovery")
public class ServiceDiscoveryProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            return false;
        }
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ServiceDiscovery.class);
        for (Element element : elements) {
            if (element.getKind().isInterface()) {
                RegisterRestClient annotation = element.getAnnotation(RegisterRestClient.class);
                if (annotation != null) {
                    FileObject object;
                    try {
                        object = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "nacos_services",
                                element.getSimpleName().toString() + ".properties");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Properties properties = new Properties();
                    properties.setProperty("className", element.getSimpleName().toString());
                    properties.setProperty("baseUri", annotation.baseUri());
                    properties.setProperty("configKey", annotation.configKey());
                    try (Writer writer = object.openWriter()) {
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
