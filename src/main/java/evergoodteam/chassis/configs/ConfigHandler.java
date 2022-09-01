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

    /**
     * Gets all the contents of a .properties config file
     *
     * @param config owner of the .properties config file
     * @return the lines from the file as a {@link List}
     */
    public static List<String> getContents(@NotNull ConfigBase config) {
        try {
            return Files.readAllLines(config.propertiesPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads the properties present in the .properties config file owned by the provided {@link ConfigBase}
     * and writes them to their respective variables <p>
     * NOTE: if the .properties config file is empty, it will be regenerated with the default values
     */
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
                config.getBuilder().setupDefaultProperties();
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
     * Gets a property's boolean value from the .properties config file of the specified Config
     *
     * @param config       owner of the property
     * @param name         name of the property
     * @param defaultValue default value of the property to use as fallback
     */
    public static @NotNull Boolean getBooleanOption(@NotNull ConfigBase config, String name, Boolean defaultValue) {
        return Boolean.valueOf(String.valueOf(getOption(config, name, defaultValue)));
    }

    /**
     * Gets a property's value from the .properties config file of the specified Config
     *
     * @param config       owner of the property
     * @param name         name of the property
     * @param defaultValue default value of the property to use as fallback
     */
    public static Object getOption(@NotNull ConfigBase config, String name, @NotNull Object defaultValue) {
        return getOption(config, name) != null ? getOption(config, name) : defaultValue;
    }

    /**
     * Gets a property's value from the .properties config file of the specified Config
     *
     * @param config owner of the property
     * @param name   name of the property
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
