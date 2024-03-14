package evergoodteam.chassis.config;

import evergoodteam.chassis.config.networking.ConfigNetworking;
import evergoodteam.chassis.config.option.*;
import evergoodteam.chassis.util.StringUtils;
import evergoodteam.chassis.util.handlers.RegistryHandler;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

import static evergoodteam.chassis.util.Reference.CMI;
import static org.slf4j.LoggerFactory.getLogger;

// TODO: use getters
public class ConfigBase {

    private final Logger LOGGER = getLogger(CMI + "/Config");
    private final Path configDir = FabricLoader.getInstance().getConfigDir();
    private final Identifier identifier;
    public final Path dirPath;
    public final Path propertiesPath;
    public final File propertiesFile;
    private Text displayName;
    private final BooleanOption configLocked; // Part of default set of options
    private OptionStorage optionStorage;
    private ConfigHandler handler;
    private final ConfigWriter configWriter;
    private final ConfigNetworking networking;
    private boolean strictVersion = true;

    /**
     * Gets the configs associated to the provided modid
     */
    public static @Nullable ConfigBase getConfig(Identifier identifier) {
        return RegistryHandler.getConfigurations().getOrDefault(identifier, null);
    }

    public ConfigBase(Identifier identifier) {
        this.identifier = identifier;
        this.dirPath = !identifier.getNamespace().equals(identifier.getPath()) ?
                configDir.resolve("%s/%s".formatted(identifier.getNamespace(), identifier.getPath())) :
                configDir.resolve(identifier.getPath());
        this.propertiesPath = this.dirPath.resolve(identifier.getPath() + ".properties");
        this.propertiesFile = new File(this.propertiesPath.toString());
        this.displayName = Text.literal(StringUtils.capitalize(identifier.toString()));

        String configFriendlyName = StringUtils.replaceWithCapital(identifier.toString(), ":+(.)?", "/+(.)?");

        this.configLocked = new BooleanOption(configFriendlyName + "ConfigLocked", false).getBuilder()
                .setComment("Lock " + configFriendlyName + " configs from being regenerated").build();
        this.optionStorage = new OptionStorage(this);
        optionStorage.getConfigLockCat().addBooleanOption(configLocked);

        this.configWriter = new ConfigWriter(this);
        this.handler = new ConfigHandler(this);
        this.networking = new ConfigNetworking(this);

        handler.setup(); // Reads, updates stored, resets if lock off
        RegistryHandler.registerConfiguration(this);
    }

    public Identifier getIdentifier() {
        return this.identifier;
    }

    /**
     * Returns a boolean that specifies if version checks are done for this {@link ConfigBase}; these checks look for mismatches between the mod version
     * and the written version in the config file. If a mismatch is found, the config file will be regenerated with the default values.
     *
     * @see ConfigHandler#setup()
     */
    // TODO: version mismatch doesnt take into account the possibility that mod was just updated, aka nothing is broken
    public boolean isStrictVersioningEnabled() {
        return this.strictVersion;
    }

    public ConfigBase setStrictVersioning(boolean strictVersion) {
        this.strictVersion = strictVersion;
        return this;
    }

    public Text getDisplayTitle() {
        return this.displayName;
    }

    public OptionStorage getOptionStorage() {
        return optionStorage;
    }

    public ConfigHandler getHandler() {
        return handler;
    }

    public ConfigNetworking getNetworkHandler() {
        return this.networking;
    }

    public ConfigWriter getWriter() {
        return configWriter;
    }

    public BooleanOption getLock() {
        return configLocked;
    }

    public boolean isLocked() {
        return configLocked.getValue();
    }

    public Optional<String> getWrittenValue(AbstractOption<?> option) {
        if (configWriter.getSerializer().getMappedWrittenOptions().get(option.getName()) == null)
            return Optional.empty();
        return Optional.of(configWriter.getSerializer().getMappedWrittenOptions().get(option.getName()));
    }


    public ConfigBase setDisplayTitle(Text title) {
        this.displayName = title;
        return this;
    }

    //region User content

    /**
     * Adds a config category, a group of properties with a common purpose, from the provided {@link CategoryOption}
     */
    public ConfigBase addCategory(CategoryOption category) {
        this.getOptionStorage().getCategories().add(category);
        return this;
    }

    /**
     * Adds a boolean option to the generic category (nameless).
     */
    public ConfigBase addBooleanProperty(BooleanOption booleanOption) {
        this.getOptionStorage().getGenericCategory().addBooleanOption(booleanOption);
        return this;
    }

    /**
     * Adds a double option to the generic category (nameless).
     */
    public ConfigBase addDoubleProperty(DoubleSliderOption doubleOption) {
        this.getOptionStorage().getGenericCategory().addDoubleOption(doubleOption);
        return this;
    }

    /**
     * Adds an integer option to the generic category (nameless).
     */
    public ConfigBase addIntegerSliderProperty(IntegerSliderOption option) {
        this.getOptionStorage().getGenericCategory().addIntegerOption(option);
        return this;
    }

    /**
     * Adds a string option to the generic category (nameless).
     */
    public ConfigBase addStringProperty(StringSetOption option) {
        this.getOptionStorage().getGenericCategory().addStringSetOption(option);
        return this;
    }

    /**
     * FILE SHOULD ALREADY EXIST. Writes user options to the config file.
     */
    public void registerProperties() {
        //log.info("REGISTERING PROPERTIES ----------------------------------------------------------");
        configWriter.updateStoredUserFromWritten();
        configWriter.overwriteWithStored();
    }
    //endregion

    /**
     * Opens the .properties file with the system's default text editor for the ".properties" file extension
     */
    public void openConfigFile() {
        Util.getOperatingSystem().open(propertiesFile);
    }
}
