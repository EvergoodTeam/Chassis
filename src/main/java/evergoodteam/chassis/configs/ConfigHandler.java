package evergoodteam.chassis.configs;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

@Log4j2
public class ConfigHandler {

    /**
     * Gets a Boolean option from the .properties file of the specified Config
     *
     * @param config       owner of the Boolean
     * @param name         name of your Boolean
     * @param defaultValue default value of your Boolean, used when the .properties File doesn't exist
     * @return
     */
    public static @NotNull Boolean getBooleanOption(@NotNull ConfigBase config, String name, Boolean defaultValue) {
        return Boolean.valueOf(String.valueOf(getOption(config, name, defaultValue)));
    }

    /**
     * Gets an Object option from the .properties file of the specified Config
     *
     * @param config       owner of the Boolean
     * @param name         name of your Object
     * @param defaultValue default value of your Object, used when the .properties File or the Property doesn't exist
     * @return
     */
    public static Object getOption(@NotNull ConfigBase config, String name, Object defaultValue) {
        return getOption(config, name) != null ? getOption(config, name) : defaultValue;
    }

    public static @Nullable Object getOption(ConfigBase config, String name) {

        Properties p = new Properties();

        if (Files.exists(Paths.get(config.propertiesPath))) {
            try (InputStream input = new FileInputStream(config.propertiesPath)) {
                p.load(input);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return p.getProperty(name);
    }
}
