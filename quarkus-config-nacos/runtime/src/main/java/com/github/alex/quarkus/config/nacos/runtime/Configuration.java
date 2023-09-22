package com.github.alex.quarkus.config.nacos.runtime;

import io.smallrye.config.common.utils.ConfigSourceUtil;
import org.jboss.logging.Logger;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;


public class Configuration {
    private static final Logger log = Logger.getLogger(Configuration.class);
    final Map<String, String> configs;
    final String id;
    final ConfigFileFormat type;

    public Configuration(String id, String content, ConfigFileFormat type) {
        this.id = id;
        this.type = type;
        this.configs = stringToMap(content, type);
    }

    public static Map<String, String> stringToMap(String str, ConfigFileFormat type) {
        Map<String, String> config = new HashMap<>();
        if (str == null) return config;
        try {
            if (type.equals(ConfigFileFormat.properties)) {
                Properties properties = new Properties();
                StringReader reader = new StringReader(str);
                properties.load(reader);
                config.putAll(ConfigSourceUtil.propertiesToMap(properties));
            }
            if (type.equals(ConfigFileFormat.yaml) || type.equals(ConfigFileFormat.yml)) {
                config.putAll(yamlStringToMap(str));
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return config;
    }

    private static Map<String, String> yamlStringToMap(String str) {
        if (str == null) return new HashMap<>();
        Map<String, Object> yamlInput = (new Yaml()).loadAs(str, HashMap.class);
        return yamlInputToMap(yamlInput);
    }

    private static Map<String, String> yamlInputToMap(Map<String, Object> yamlInput) {
        Map<String, String> properties = new TreeMap<>();
        if (yamlInput != null) {
            flattenYaml("", yamlInput, properties);
        }
        return properties;
    }

    private static void flattenYaml(String path, Map<String, Object> source, Map<String, String> target) {
        source.forEach((key, value) -> {
            if (key != null && key.indexOf(46) != -1) {
                key = "\"" + key + "\"";
            }

            if (key != null && !key.isEmpty() && path != null && !path.isEmpty()) {
                key = path + "." + key;
            } else if (path != null && !path.isEmpty()) {
                key = path;
            } else if (key == null || key.isEmpty()) {
                key = "";
            }

            if (value instanceof String) {
                target.put(key, (String) value);
            } else if (value instanceof Map) {
                flattenYaml(key, (Map) value, target);
            } else if (value instanceof List) {
                List<Object> list = (List) value;
                flattenList(key, list, target);

                for (int i = 0; i < list.size(); ++i) {
                    flattenYaml(key, Collections.singletonMap("[" + i + "]", list.get(i)), target);
                }
            } else {
                target.put(key, value != null ? value.toString() : "");
            }

        });
    }

    private static void flattenList(String key, List<Object> source, Map<String, String> target) {
        if (source.stream().allMatch((o) -> o instanceof String)) {
            target.put(key, source.stream().map((o) -> {
                StringBuilder sb = new StringBuilder();
                escapeCommas(sb, o.toString(), 1);
                return sb.toString();
            }).collect(Collectors.joining(",")));
        } else {
            DumperOptions dumperOptions = new DumperOptions();
            dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.FLOW);
            dumperOptions.setDefaultScalarStyle(DumperOptions.ScalarStyle.FOLDED);
            target.put(key, (new Yaml(dumperOptions)).dump(Collections.singletonMap(key.substring(key.lastIndexOf(".") + 1), source)));
        }

    }

    private static void escapeCommas(StringBuilder b, String src, int escapeLevel) {
        int cp;
        for (int i = 0; i < src.length(); i += Character.charCount(cp)) {
            cp = src.codePointAt(i);
            if (cp == 92 || cp == 44) {
                b.append("\\".repeat(Math.max(0, escapeLevel)));
            }
            b.appendCodePoint(cp);
        }
    }
}
