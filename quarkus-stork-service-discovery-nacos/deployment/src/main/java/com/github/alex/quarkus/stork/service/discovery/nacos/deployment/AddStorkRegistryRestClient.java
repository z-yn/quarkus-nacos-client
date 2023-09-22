package com.github.alex.quarkus.stork.service.discovery.nacos.deployment;

import io.quarkus.gizmo.Gizmo;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;

class AddStorkRegistryRestClient extends ClassVisitor {

    private final String serviceName;

    public AddStorkRegistryRestClient(ClassVisitor visitor, String serviceName) {
        super(Gizmo.ASM_API_VERSION, visitor);
        this.serviceName = serviceName;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        AnnotationVisitor annotationVisitor = super.visitAnnotation(descriptor, visible);
        String annoRestClient = "Lorg/eclipse/microprofile/rest/client/inject/RegisterRestClient;";
        if (annoRestClient.equals(descriptor)) {
            annotationVisitor.visit("baseUri", "stork://" + serviceName);
            annotationVisitor.visitEnd();
        }
        return annotationVisitor;
    }

}
