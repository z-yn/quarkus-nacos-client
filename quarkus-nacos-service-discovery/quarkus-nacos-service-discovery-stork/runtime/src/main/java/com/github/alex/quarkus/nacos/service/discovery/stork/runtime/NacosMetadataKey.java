package com.github.alex.quarkus.nacos.service.discovery.stork.runtime;

import io.smallrye.stork.api.MetadataKey;

public enum NacosMetadataKey implements MetadataKey {
    /**
     * The key for the consul service id.
     */
    META_SERVICE_ID("nacos-app-id"),
    /**
     * The key for the consul service node.
     */
    META_SERVICE_NODE("nacos-service-node"),
    /**
     * The key for the consul service node address.
     */
    META__SERVICE_NODE_ADDRESS("nacos-service-node-address");
    private final String name;

    /**
     * Creates a new ConsulMetadataKey
     *
     * @param name the name
     */
    NacosMetadataKey(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return null;
    }
}
