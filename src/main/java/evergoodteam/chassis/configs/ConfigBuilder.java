package evergoodteam.chassis.configs;

import evergoodteam.chassis.util.StringUtil;
import lombok.extern.log4j.Log4j2;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

@Log4j2
public class ConfigBuilder {

    private final String N = System.lineSeparator();

    private ConfigBase config;

    public ConfigBuilder(ConfigBase configBase) {
        this.config = configBase;
    }

    public String header() {
        StringBuilder sb = new StringBuilder();

        sb.append("# %s Configs".formatted(StringUtil.capitalize(config.namespace))).append(N);
        sb.append("# " + new Date()).append(N + N);

        return sb.toString();
    }

    public String defaultOptions() {

        StringBuilder sb = new StringBuilder();

        sb.append("# Config-related Options").append(N);
        sb.append(config.namespace + "ConfigLocked = " + config.configLocked).append(N);

        return sb.toString();
    }

    public String resourceOptions() {

        StringBuilder sb = new StringBuilder();

        config.resourcesLocked.forEach((name, value) -> {
            sb.append(name + " = " + value).append(N);
            sb.append("test" + " = " + "false").append(N);
        });

        sb.append(N).append("# Mod-related Options").append(N);

        return sb.toString();
    }

    public String additionalOptions() {

        StringBuilder sb = new StringBuilder();

        Properties p = new Properties();

        try (InputStream input = new FileInputStream(config.propertiesPath)) {
            p.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        config.options.forEach((name, value) -> {

            if (p.getProperty(name) == null) { // Property is missing, add with default value
                log.info("Found missing property, adding to File");
                sb.append(name + " = " + value).append(N);
            } else { // Property exists, fetch the value and overwrite Map
                config.options.put(name, p.getProperty(name));
            }
        });

        return sb.toString();
    }
}
