package evergoodteam.chassis.configs;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import evergoodteam.chassis.configs.options.AbstractOption;
import lombok.extern.log4j.Log4j2;
import net.fabricmc.api.EnvType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;

import static evergoodteam.chassis.util.Reference.CMI;
import static org.slf4j.LoggerFactory.getLogger;

// TODO: convert to object
@Log4j2
public class ConfigHandler {

    private static final Logger LOGGER = getLogger(CMI + "/C/H");

    /**
     * Returns a single string with all the common properties and their value
     */
    public static String getCommonProperties(ConfigBase config) {
        Map<String, String> map = getPropertyMap(config);
        Map<String, String> copy = new LinkedHashMap<>(map);

        map.keySet().stream().filter(name -> config.getOptionStorage().getOption(name) != null && config.getOptionStorage().getOption(name).getType() == EnvType.CLIENT).forEach(copy::remove);

        StringBuilder result = new StringBuilder();
        copy.forEach((name, value) -> {
            result.append(name + "=" + value);
        });

        return result.toString();
    }

    /**
     * Returns a map that uses the property names as keys
     */
    public static Map<String, String> getPropertyMap(ConfigBase config) {
        Map<String, String> result = new LinkedHashMap<>();

        for (String var : getProperties(config)) {
            result.put(var.split("=")[0].replaceAll(" ", ""), var.split("=")[1].replaceAll(" ", ""));
        }

        return result;
    }

    /**
     * Returns a list of the .properties config file's lines that contain hold a property
     */
    public static List<String> getProperties(ConfigBase config) {
        List<String> contents = new ArrayList<>(getContents(config));

        getContents(config).forEach(string -> {
            if (string.startsWith("#") || "\n".equals(string)) contents.remove(string);
        });

        List<String> result = new ArrayList<>();
        contents.forEach(string -> {
            if (string != null && !string.isEmpty()) result.add(string);
        });

        Collections.sort(result);

        return result;
    }

    /**
     * Reads the .properties config file and returns it as a string
     */
    public static String toString(ConfigBase config) {
        try {
            return Files.readString(config.propertiesPath, Charsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets all the contents of a .properties config file
     *
     * @param config owner of the .properties config file
     * @return the lines from the file as a {@link List}
     */
    public static List<String> getContents(@NotNull ConfigBase config) {
        try {
            return Lists.newArrayList(Files.readString(config.propertiesPath, Charsets.UTF_8).split("\\r?\\n"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads the properties present in the .properties config file owned by the provided {@link ConfigBase}
     * and writes them to their respective variables <p>
     * NOTE: if the .properties config file is empty, it will be regenerated with the default values
     */
    public static boolean readOptions(@NotNull ConfigBase config) {
        if (Files.exists(config.propertiesPath)) {
            Map<String, String> mapped = getPropertyMap(config);
            config.getBuilder().lines = getContents(config); // TODO: just a variable overwrite, have methods

            if (!mapped.containsKey(config.configLocked.getName())) {
                LOGGER.warn("Can't read as the Config File is empty, attempting to regenerate");
                config.getBuilder().writeDefaults();
                config.getBuilder().overwrite();
                return false;
            }

            config.configLocked.updateValueFromWritten(config);
            config.resourcesLocked.forEach((namespace, booleanOption) -> booleanOption.updateValueFromWritten(config));

            boolean rewriteAdditional = false;
            for (AbstractOption<?> option : config.getOptionStorage().getOptions()) {
                if (!mapped.containsKey(option.getName())) {
                    rewriteAdditional = true;
                }
                option.updateValueFromWritten(config);
            }
            return !rewriteAdditional;
        }
        return false;
    }

    /**
     * Returns true if any option from {@link evergoodteam.chassis.configs.options.OptionStorage OptionStorage} has a different value from the written one
     */
    // TODO: get better name
    public static boolean isntWritten(@NotNull ConfigBase config){
        if (Files.exists(config.propertiesPath)) {
            Map<String, String> mapped = getPropertyMap(config);

            if (!mapped.containsKey(config.configLocked.getName())) {
                LOGGER.error("Can't check if properties have changed since the file doesn't exist");
                return false;
            }

            for (AbstractOption<?> option : config.getOptionStorage().getOptions()) {
                if(!mapped.containsKey(option.getName())) return false;
                //log.info(option.getValue() + " - written: " + mapped.get(option.getName()) + " : equal -> " + String.valueOf(option.getValue()).equals(mapped.get(option.getName())));
                if (!String.valueOf(option.getValue()).equals(mapped.get(option.getName()))) return true;
            }
            return false;
        }
        return false;
    }

    /**
     * @deprecated as of release 1.2.3, use {@link AbstractOption#getWrittenValue(ConfigBase)} instead
     */
    @Deprecated
    public static @NotNull Boolean getBooleanOption(@NotNull ConfigBase config, String name, Boolean defaultValue) {
        return getOption(config, name) != null ? Boolean.valueOf(getOption(config, name)) : defaultValue;
    }

    /**
     * Gets a property's value from the .properties config file linked to the provided {@link ConfigBase}
     *
     * @param config property's owner
     * @param name   property's name
     */
    // TODO: unify in ConfigBase or create an object with the handler
    public static @Nullable String getOption(@NotNull ConfigBase config, String name) {
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
