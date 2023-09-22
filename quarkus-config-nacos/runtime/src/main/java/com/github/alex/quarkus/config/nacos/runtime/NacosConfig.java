package com.github.alex.quarkus.config.nacos.runtime;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

import java.util.Optional;

@ConfigMapping(prefix = "quarkus.nacos-config")
@ConfigRoot(phase = ConfigPhase.BUILD_TIME)
public interface NacosConfig {
    /**
     * app-id
     *
     * @asciidoclet
     */
    Optional<String> appId();

    /**
     * group name
     */

    @WithDefault("DEFAULT_GROUP")
    Optional<String> group();

    /**
     * 命名空间
     */
    @WithDefault("")
    Optional<String> namespace();

    /**
     * 地址
     */
    Optional<String> serverAddr();

    /**
     * 用户名
     */
    Optional<String> username();

    /**
     * password
     */
    Optional<String> password();

    /**
     * format: properties, yaml, yml
     */
    @WithDefault("properties")
    Optional<ConfigFileFormat> format();

    /**
     * profile,eg: dev , master etc
     */
    Optional<String> profile();

}
