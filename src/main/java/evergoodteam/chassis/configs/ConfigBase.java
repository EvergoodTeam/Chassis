package evergoodteam.chassis.configs;

import evergoodteam.chassis.configs.options.*;
import evergoodteam.chassis.objects.resourcepacks.ResourcePackBase;
import evergoodteam.chassis.util.handlers.DirHandler;
import evergoodteam.chassis.util.handlers.FileHandler;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Language;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    public BooleanOption configLocked;        // Part of default set of options
    public Map<String, BooleanOption> resourcesLocked; // Part of default set of options
    private OptionStorage optionStorage;
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

        this.configLocked = new BooleanOption(namespace + "ConfigLocked", false);
        this.resourcesLocked = new HashMap<>();
        this.optionStorage = new OptionStorage(this);
        this.builder = new ConfigBuilder(this);

        this.readProperties();  // Look for existing values

        if (!this.configLocked.getValue()) {
            this.configLocked.setValue(true);
            this.createDefaults();
        } else LOGGER.info("Configs for \"{}\" already exist, skipping first generation", this.namespace);
    }

    /**
     * Reads the variables present in the .properties config file and updates the linked variables <p>
     * NOTE: if the .properties config file is empty, it will be regenerated with the default values
     */
    public void readProperties() {
        ConfigHandler.readOptions(this);
    }

    public OptionStorage getOptionStorage() {
        return optionStorage;
    }

    public ConfigBuilder getBuilder() {
        return builder;
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

    //region Root creation

    /**
     * Creates all the needed dirs and the .properties config file with the required default variables
     */
    private void createDefaults() {
        // Regenerate everything
        FileHandler.delete(this.propertiesPath);
        DirHandler.create(this.dirPath);
        FileHandler.createFile(this.propertiesPath);

        builder.setupDefaultProperties();

        LOGGER.info("Generated Configs for \"{}\"", this.namespace);
    }
    //endregion

    //region User content

    /**
     * Adds a boolean property from the provided {@link BooleanOption} <p>
     * e.g. you can add {@link ResourcePackBase#getHiddenBoolean()} to let users hide your ResourcePack
     */
    public ConfigBase addBooleanProperty(BooleanOption booleanOption) {
        optionStorage.storeBoolean(booleanOption);
        return this;
    }

    public ConfigBase addDoubleProperty(DoubleSliderOption doubleOption) {
        optionStorage.storeDouble(doubleOption);
        return this;
    }

    public ConfigBase addIntegerSliderProperty(String translationKey, int min, int max, int defaultValue) {
        return addIntegerSliderProperty(translationKey, min, max, defaultValue, Text.empty());
    }

    public ConfigBase addIntegerSliderProperty(IntegerSliderOption option) {
        optionStorage.storeInteger(option);
        return this;
    }

    public ConfigBase addIntegerSliderProperty(String translationKey, int min, int max, int defaultValue, MutableText comment) {
        optionStorage.storeInteger(new IntegerSliderOption(translationKey, min, max, defaultValue, comment));
        return this;
    }

    public ConfigBase addStringProperty(String translationKey, String defaultValue, Set<String> values, MutableText comment) {
        optionStorage.storeStringSet(new StringSetOption(translationKey, defaultValue, values, comment));
        return this;
    }

    /**
     * Writes the properties added to the .properties config file <p>
     * NOTE: call only after you added all your properties!
     */
    public ConfigBase registerProperties() {
        builder.registerProperties();
        return this;
    }

    public @Nullable String getWrittenValue(String name) {
        return ConfigHandler.getOption(this, name);
    }

    /**
     * Sets a property's value to the specified one if it is different from the original
     *
     * @param name     name of your property
     * @param newValue what the property is equal to
     */
    public <T> ConfigBase setWrittenValue(String name, T newValue) {
        return overwrite(name, String.valueOf(newValue));
    }

    /**
     * Overwrites the property specified by the provided name with the provided value
     *
     * @param name     name of your property
     * @param newValue what the property is equal to
     */
    private ConfigBase overwrite(String name, String newValue) {
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
