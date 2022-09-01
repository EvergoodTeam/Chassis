package evergoodteam.chassis.configs;

import evergoodteam.chassis.util.handlers.DirHandler;
import evergoodteam.chassis.util.handlers.FileHandler;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Util;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

import static evergoodteam.chassis.util.Reference.CMI;
import static org.slf4j.LoggerFactory.getLogger;

public class ConfigBase {

    private static final Logger LOGGER = getLogger(CMI + "/Config");

    private static final Map<String, ConfigBase> CONFIGURATIONS = new HashMap<>();
    private static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir();

    public String namespace;

    public Path dirPath;
    public Path propertiesPath;
    public File propertiesFile;

    public Boolean configLocked = false;        // Part of default set of options
    public Map<String, Object> resourcesLocked; // Part of default set of options
    public Map<String, Object> addonOptions; // Properties added by the user
    public List<String> addonComments;       // Properties comments

    private ConfigBuilder builder;

    /**
     * Object from which Configs will be generated
     *
     * @param namespace name of your Configs
     */
    public ConfigBase(String namespace) {
        this.namespace = namespace;
        this.dirPath = CONFIG_DIR.resolve(namespace);
        this.propertiesPath = this.dirPath.resolve(namespace + ".properties");
        this.propertiesFile = new File(this.propertiesPath.toString());

        CONFIGURATIONS.put(namespace, this);

        this.resourcesLocked = new HashMap<>();
        this.addonOptions = new LinkedHashMap<>(); // Avoid order trouble
        this.addonComments = new ArrayList<>();

        this.builder = new ConfigBuilder(this);

        ConfigHandler.readOptions(this); // Look for existing values

        if (!this.configLocked) {
            this.configLocked = true;
            this.createConfigRoot();
        } else LOGGER.info("Configs for \"{}\" already exist, skipping first generation", this.namespace);
    }

    public ConfigBuilder getBuilder() {
        return this.builder;
    }

    /**
     * Gets the configs associated to the provided namespace
     *
     * @param namespace name of the configs
     */
    public static @Nullable ConfigBase getConfig(String namespace) {
        return CONFIGURATIONS.getOrDefault(namespace, null);
    }

    /**
     * Gets all the existing Configs created through {@link ConfigBase}
     */
    public static Map<String, ConfigBase> getConfigurations() {
        return CONFIGURATIONS;
    }

    //region Root Init

    /**
     * Creates all the needed dirs and the .properties config file
     */
    private ConfigBase createConfigRoot() {
        FileHandler.delete(this.propertiesPath);
        // Regenerate everything
        DirHandler.create(this.dirPath);
        FileHandler.createFile(this.propertiesPath);

        builder.setupDefaultProperties();

        LOGGER.info("Generated Configs for \"{}\"", this.namespace);

        return this;
    }
    //endregion

    //region User content

    /**
     * Adds a property with a comment to the .properties config file
     *
     * @param name    name of your property
     * @param value   what the property is equal to
     * @param comment description of the property
     */
    public ConfigBase addProperty(String name, Object value, @NotNull String comment) {
        this.addonOptions.put(name, value);
        this.addonComments.add(comment);
        return this;
    }

    /**
     * Adds a property with no comment to the .properties config file
     *
     * @param name  name of your property
     * @param value what the property is equal to
     */
    public ConfigBase addProperty(String name, Object value) {
        this.addonOptions.put(name, value);
        this.addonComments.add("");
        return this;
    }

    /**
     * Writes the properties added with {@link #addProperty} to the .properties config file <p>
     * NOTE: call only after you added all your properties!
     */
    public ConfigBase registerProperties() {
        builder.registerProperties();
        return this;
    }

    public boolean getBooleanProperty(String name, boolean defaultValue) {
        return ConfigHandler.getBooleanOption(this, name, defaultValue);
    }

    public Object getProperty(String name, Object defaultValue) {
        return ConfigHandler.getOption(this, name, defaultValue);
    }

    /**
     * Overwrites the property specified by the provided name with the provided value
     *
     * @param name     name of your property
     * @param newValue what the property is equal to
     */
    public ConfigBase overwrite(String name, String newValue) {
        builder.overwrite(name, newValue);
        return this;
    }

    //endregion

    /**
     * Opens the .properties file with the system's default text editor for the ".properties" file extension
     */
    public void openConfigFile() {
        Util.getOperatingSystem().open(this.propertiesFile);
    }
}
