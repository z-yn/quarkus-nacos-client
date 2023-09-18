package com.github.alex.quarkus.nacos.client.runtime;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Stereotype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Stereotype
@Target({ElementType.TYPE})
@Dependent
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ServiceDiscoveryNacos {
}
