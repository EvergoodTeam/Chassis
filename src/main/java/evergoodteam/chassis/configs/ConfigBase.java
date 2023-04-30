package evergoodteam.chassis.configs;

import evergoodteam.chassis.configs.options.*;
import evergoodteam.chassis.objects.resourcepacks.ResourcePackBase;
import evergoodteam.chassis.util.StringUtils;
import evergoodteam.chassis.util.handlers.DirHandler;
import evergoodteam.chassis.util.handlers.FileHandler;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static evergoodteam.chassis.util.Reference.CMI;
import static org.slf4j.LoggerFactory.getLogger;

public class ConfigBase {

    private static final Logger LOGGER = getLogger(CMI + "/Config");
    public static final Map<String, ConfigBase> CONFIGURATIONS = new HashMap<>();
    private static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir();

    // TODO: switch to getters
    public final String namespace;
    public final Path dirPath;
    public final Path propertiesPath;
    public final File propertiesFile;
    private Text title;
    public final BooleanOption configLocked; // Part of default set of options
    public final Map<String, BooleanOption> resourcesLocked; // Part of default set of options
    private OptionStorage optionStorage;
    private ConfigBuilder builder;
    public final ConfigNetworking networking;

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
        this.title = Text.literal(StringUtils.capitalize(namespace));
        this.configLocked = new BooleanOption(namespace + "ConfigLocked", false)
                .setComment("Lock " + StringUtils.capitalize(namespace) + " configs from being regenerated");
        this.resourcesLocked = new HashMap<>();
        this.optionStorage = new OptionStorage(this);
        this.builder = new ConfigBuilder(this);
        this.networking = new ConfigNetworking(this);

        CONFIGURATIONS.put(namespace, this);
        this.readProperties();  // Looks for existing values
        if (!this.configLocked.getValue()) {
            this.configLocked.setValue(true);
            this.createDefaults();
        } else LOGGER.info("Configs for \"{}\" already exist, skipping first generation", this.namespace);
    }

    //region Getters

    public Text getTitle() {
        return this.title;
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
    //endregion

    //region Setters

    public ConfigBase setDisplayTitle(Text title) {
        this.title = title;
        return this;
    }
    //endregion

    //region Root creation

    /**
     * Reads the variables present in the .properties config file and updates the linked variables <p>
     * NOTE: if the .properties config file is empty, it will be regenerated with the default values
     */
    public boolean readProperties() {
        return ConfigHandler.readOptions(this);
    }

    /**
     * Creates all the needed dirs and the .properties config file with the required default variables
     */
    private void createDefaults() {
        // Regenerate everything
        FileHandler.delete(this.propertiesPath);
        DirHandler.create(this.dirPath);
        FileHandler.createFile(this.propertiesPath);
        builder.empty();
        builder.writeDefaults();
        builder.overwrite();

        LOGGER.info("Generated Configs for \"{}\"", this.namespace);
    }
    //endregion

    //region User content

    /**
     * @deprecated as of release 1.2.3, replaced by {@link #addBooleanProperty(BooleanOption)}
     */
    @Deprecated
    public ConfigBase addProperty(String name, boolean defaultValue, String comment) {
        this.addBooleanProperty(new BooleanOption(name, defaultValue).setComment(comment));
        return this;
    }

    /**
     * Adds a config category, a group of properties with a common purpose, from the provided {@link CategoryOption}
     */
    public ConfigBase addCategory(CategoryOption category) {
        this.getOptionStorage().getCategories().add(category);
        return this;
    }

    /**
     * Adds a generic boolean property from the provided {@link BooleanOption} <p>
     * e.g. you can add {@link ResourcePackBase#getHideResourcePackProperty()} to let users hide your ResourcePack
     */
    public ConfigBase addBooleanProperty(BooleanOption booleanOption) {
        this.getOptionStorage().getGenericCategory().addBooleanProperty(booleanOption);
        return this;
    }

    /**
     * Adds a generic double property from the provided {@link DoubleSliderOption} <p>
     * e.g. {@code range: 3.0 ~ 8.0, default: 4.5}
     */
    public ConfigBase addDoubleProperty(DoubleSliderOption doubleOption) {
        this.getOptionStorage().getGenericCategory().addDoubleProperty(doubleOption);
        return this;
    }

    /**
     * Adds a generic integer property from the provided {@link IntegerSliderOption} <p>
     * e.g. {@code range: 3 ~ 8, default: 5}
     */
    public ConfigBase addIntegerSliderProperty(IntegerSliderOption option) {
        this.getOptionStorage().getGenericCategory().addIntegerSliderProperty(option);
        return this;
    }

    /**
     * Adds a generic string set property from the provided {@link StringSetOption} <p>
     * e.g. {@code values: apple, banana, orange, default: orange}
     */
    public ConfigBase addStringProperty(StringSetOption option) {
        this.getOptionStorage().getGenericCategory().addStringProperty(option);
        return this;
    }

    /**
     * Writes the added properties to the .properties config file <p>
     * NOTE: call only after you added all your properties!
     */
    public void registerProperties() {
        if (!readProperties()) {
            this.builder.writeAddons();
            builder.overwrite();
        }
    }

    /**
     * Returns the value of the property with the specified name from the .properties config file
     */
    public @Nullable String getWrittenValue(String name) {
        return ConfigHandler.getOption(this, name);
    }

    /**
     * Returns the value of the property with the specified name from the .properties config file
     */
    public <T> @Nullable String getWrittenValue(String name, T defaultValue) {
        return ConfigHandler.getOption(this, name);
    }

    /**
     * Sets a property's value to the specified one if it is different from the original
     *
     * @param name     name of your property
     * @param newValue what the property is equal to
     */
    public <T> void setWrittenValue(String name, T newValue) {
        overwrite(name, String.valueOf(newValue));
    }

    /**
     * Overwrites the property specified by the provided name with the provided value
     *
     * @param name     name of your property
     * @param newValue what the property is equal to
     */
    private void overwrite(String name, String newValue) {
        builder.overwrite(name, newValue);
    }
    //endregion

    /**
     * Opens the .properties file with the system's default text editor for the ".properties" file extension
     */
    public void openConfigFile() {
        Util.getOperatingSystem().open(this.propertiesFile);
    }
}
