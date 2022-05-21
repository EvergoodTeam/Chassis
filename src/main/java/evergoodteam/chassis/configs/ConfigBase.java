package evergoodteam.chassis.configs;

import evergoodteam.chassis.util.handlers.DirHandler;
import evergoodteam.chassis.util.handlers.FileHandler;
import lombok.extern.log4j.Log4j2;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Util;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

@Log4j2
public class ConfigBase {

    //public static final Logger LOGGER = getLogger("Config");

    public static final Map<String, ConfigBase> CONFIGURATIONS = new HashMap<>();
    private static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir();

    public String namespace;

    public Path dirPath;
    public Path propertiesPath;
    public File propertiesFile;

    public Boolean configLocked = false;        // Part of default set of options
    public Map<String, Object> resourcesLocked; // Part of default set of options

    public Map<String, Object> addonOptions; // Properties added by the user
    public List<String> addonComments;       // Properties comments

    public ConfigBuilder builder;

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
            this.createConfigRoot();
            this.configLocked = true;
        } else log.info("Configs for \"{}\" already exist, skipping generation", this.namespace);
    }

    /**
     * Get the Configs associated to a namespace
     *
     * @param namespace name of the Configs
     */
    public static @Nullable ConfigBase getConfig(String namespace) {
        return CONFIGURATIONS.containsKey(namespace) ? CONFIGURATIONS.get(namespace) : null;
    }

    //region Root Init

    /**
     * Creates all the needed dirs and the .properties Confile File
     */
    private ConfigBase createConfigRoot() {

        FileHandler.delete(this.propertiesPath);
        // Regenerate everything
        DirHandler.create(this.dirPath);
        FileHandler.createFile(this.propertiesPath);

        builder.setupDefaultProperties();

        log.info("Generated Configs for \"{}\"", this.namespace);

        return this;
    }

    //endregion

    //region Config Builder

    /**
     * Add a Property to the Config File
     *
     * @param name    name of your Property
     * @param value   what the Property is equal to
     * @param comment description of the property
     */
    public ConfigBase addProperty(String name, Object value, @NotNull String comment) {
        this.addonOptions.put(name, value);
        this.addonComments.add(comment);
        return this;
    }

    /**
     * Add a Property to the Config File
     *
     * @param name  name of your Property
     * @param value what the Property is equal to
     */
    public ConfigBase addProperty(String name, Object value) {
        this.addonOptions.put(name, value);
        this.addonComments.add("");
        return this;
    }

    /**
     * Write the Properties added with {@link #addProperty} to the Config File
     */
    public ConfigBase registerProperties() {
        builder.registerProperties();
        return this;
    }

    public ConfigBase overwrite(String name, String newValue) {
        builder.overwrite(name, newValue);
        return this;
    }

    //endregion

    public ConfigBase openConfigFile() {
        Util.getOperatingSystem().open(this.propertiesFile);
        return this;
    }
}
