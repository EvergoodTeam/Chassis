package evergoodteam.chassis.configs;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Paths;

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
     * @param defaultValue default value of your Object, used when the .properties File doesn't exist
     * @return
     */
    public static Object getOption(@NotNull ConfigBase config, String name, Object defaultValue) {

        Object result = null;

        if (Files.exists(Paths.get(config.propertiesPath))) {
            try {
                PropertiesConfiguration p = new PropertiesConfiguration(config.propertiesPath);
                //log.info(Boolean.valueOf(String.valueOf(p.getProperty(name))));
                result = p.getProperty(name);
            } catch (ConfigurationException e) {
                log.error(e);
            }
        } else result = defaultValue;

        return result;
    }
}
