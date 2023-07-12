package evergoodteam.chassis.configs;

import evergoodteam.chassis.configs.options.AbstractOption;
import evergoodteam.chassis.util.FileUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
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


public class ConfigHandler {

    private static final Logger LOGGER = getLogger(CMI + "/C/Handler");
    private final ConfigBase config;

    public ConfigHandler(ConfigBase config) {
        this.config = config;
    }

    //region Versioning

    /**
     * Returns true if the version written in the properties file isn't the same as the mod's (after checking that strict
     * versioning is enabled for the specified config)
     */
    public Boolean versionUpdated() {
        return config.strictVersion && !getWrittenModVersion().equals(getModVersion());
    }

    public String getModVersion() {
        if (FabricLoader.getInstance().getModContainer(config.namespace).isPresent())
            return FabricLoader.getInstance().getModContainer(config.namespace).get().getMetadata().getVersion().getFriendlyString();
        return "";
    }

    public String getWrittenModVersion() {
        String result = getContents().get(0);
        return result.split(" ")[2];
    }

    //endregion

    //region .properties reading

    /**
     * Returns a single string with all the common options and their value
     */
    public String getCommonOptions() {
        Map<String, String> map = getOptionMap();
        Map<String, String> copy = new LinkedHashMap<>(map);

        map.keySet().stream().filter(name -> config.getOptionStorage().getOption(name) != null && config.getOptionStorage().getOption(name).getType() == EnvType.CLIENT).forEach(copy::remove);

        StringBuilder result = new StringBuilder();
        copy.forEach((name, value) -> {
            result.append(name + "=" + value);
        });

        return result.toString();
    }

    /**
     * Returns a map that uses the option names as keys
     */
    public Map<String, String> getOptionMap() {
        Map<String, String> result = new LinkedHashMap<>();

        for (String var : getOptions()) {
            result.put(var.split("=")[0].replaceAll(" ", ""), var.split("=")[1].replaceAll(" ", ""));
        }

        return result;
    }

    /**
     * Returns a list of the .properties config file's lines that contain an option
     */
    public List<String> getOptions() {
        List<String> contents = new ArrayList<>(getContents());

        getContents().forEach(string -> {
            if (string.startsWith("#") || "\n".equals(string)) contents.remove(string);
        });

        List<String> result = new ArrayList<>();
        contents.forEach(string -> {
            if (string != null && !string.isEmpty()) result.add(string);
        });

        Collections.sort(result);

        return result;
    }

    private @NotNull List<String> getContents() {
        return FileUtils.toList(config.propertiesPath);
    }
    //endregion

    /**
     * Reads the variables present in the .properties config file and updates the linked variables <p>
     * NOTE: if the .properties config file is empty, it will be regenerated with the default values
     */
    public Boolean readOptions() {
        if (Files.exists(config.propertiesPath)) {
            config.getWriter().replaceLines(getContents());

            Map<String, String> mapped = getOptionMap();
            if (!mapped.containsKey(config.configLocked.getName())) { // Lock is missing, regenerate
                LOGGER.warn("Can't read because {} is empty, attempting to regenerate", config.propertiesFile.getName());
                config.getWriter().writeDefaults();
                config.getWriter().overwrite();
                return false;
            }

            updateLocksFromWritten();

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

    public void updateLocksFromWritten() {
        config.configLocked.updateValueFromWritten(config);
        config.resourcesLocked.forEach((namespace, booleanOption) -> booleanOption.updateValueFromWritten(config));
    }

    /**
     * Returns true if any option from {@link evergoodteam.chassis.configs.options.OptionStorage OptionStorage} has a different value from the written one
     */
    public Boolean writtenNotUpdated() {
        if (Files.exists(config.propertiesPath)) {
            Map<String, String> mapped = getOptionMap();

            if (!mapped.containsKey(config.configLocked.getName())) {
                LOGGER.error("Can't check if properties have changed since the file doesn't exist");
                return false;
            }

            for (AbstractOption<?> option : config.getOptionStorage().getOptions()) {
                if (!mapped.containsKey(option.getName())) return false;
                //log.info(option.getValue() + " - written: " + mapped.get(option.getName()) + " : equal -> " + String.valueOf(option.getValue()).equals(mapped.get(option.getName())));
                if (!String.valueOf(option.getValue()).equals(mapped.get(option.getName()))) return true;
            }
            return false;
        }
        return false;
    }

    /**
     * Returns the value of the option with the specified name from the .properties config file
     */
    public @Nullable String getOptionValue(String name) {
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
