package evergoodteam.chassis.configs;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Properties;

import static evergoodteam.chassis.util.Reference.MODID;
import static org.slf4j.LoggerFactory.getLogger;

public class ConfigHandler {

    private static final Logger LOGGER = getLogger(MODID + "/C/H");

    public static List<String> getContents(@NotNull ConfigBase config) {
        try {
            return Files.readAllLines(config.propertiesPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void readOptions(@NotNull ConfigBase config) {

        if (Files.exists(config.propertiesPath)) {

            Properties c = new Properties();

            try (InputStream input = new FileInputStream(config.propertiesFile)) {
                c.load(input);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (c.isEmpty()) {   // File can exist AND be empty at the same time
                LOGGER.warn("Can't read as the Config File is empty, trying to regenerate");
                config.builder.setupDefaultProperties();
            }

            config.configLocked = getBooleanOption(config, config.namespace + "ConfigLocked", false);

            config.resourcesLocked.forEach((name, value) -> {
                config.resourcesLocked.put(name, getOption(config, name, value));
            });

            config.addonOptions.forEach((name, value) -> {
                config.addonOptions.put(name, getOption(config, name, value));
            });
        }
    }

    /**
     * Get a Property's Boolean value from the .properties file of the specified Config
     *
     * @param config       owner of the Boolean
     * @param name         name of your Boolean
     * @param defaultValue default value of your Boolean, used when the .properties File doesn't exist
     */
    public static @NotNull Boolean getBooleanOption(@NotNull ConfigBase config, String name, Boolean defaultValue) {
        return Boolean.valueOf(String.valueOf(getOption(config, name, defaultValue)));
    }

    /**
     * Get a Property's value from the .properties file of the specified Config
     *
     * @param config       owner of the Boolean
     * @param name         name of your Object
     * @param defaultValue default value of your Object, used when the .properties File or the Property doesn't exist
     */
    public static Object getOption(@NotNull ConfigBase config, String name, Object defaultValue) {
        return getOption(config, name) != null ? getOption(config, name) : defaultValue;
    }

    /**
     * Get a Property's value from the .properties file of the specified Config
     *
     * @param config owner of the Boolean
     * @param name   name of your Property
     */
    public static @Nullable Object getOption(@NotNull ConfigBase config, String name) {

        Properties p = new Properties();

        if (Files.exists(config.propertiesPath)) {
            try (InputStream input = new FileInputStream(config.propertiesFile)) {
                p.load(input);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return p.getProperty(name);
    }
}
