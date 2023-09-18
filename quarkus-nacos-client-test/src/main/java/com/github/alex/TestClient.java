package com.github.alex;

import com.github.alex.quarkus.nacos.client.runtime.ServiceDiscovery;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@ServiceDiscovery
@RegisterRestClient(baseUri = "stork://test")
public interface TestClient {
}
