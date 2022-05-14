package evergoodteam.chassis.configs;

import evergoodteam.chassis.util.StringUtils;
import evergoodteam.chassis.util.handlers.DirHandler;
import evergoodteam.chassis.util.handlers.FileHandler;
import lombok.extern.log4j.Log4j2;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Util;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static evergoodteam.chassis.configs.ConfigHandler.getBooleanOption;
import static evergoodteam.chassis.configs.ConfigHandler.getOption;
import static evergoodteam.chassis.util.Reference.MODID;
//import static evergoodteam.chassis.util.Reference.LOGGER;

@Log4j2
public class ConfigBase {

    public static final Logger LOGGER = LoggerFactory.getLogger(StringUtils.capitalize(MODID) + "/ConfigBase");

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
        this.addonOptions = new LinkedHashMap<>(); // Avoid order troubles
        this.addonComments = new ArrayList<>();


        this.readProperties(); // Look for existing values

        if (!this.configLocked) {
            this.configLocked = true;
            this.createConfigRoot(); // Create .properties
        } else LOGGER.info("Configs for \"{}\" already exist, skipping generation", this.namespace);
    }

    /**
     * Get the Configs associated to a namespace
     *
     * @param namespace name of the Configs
     */
    public static ConfigBase getConfig(String namespace) {
        return CONFIGURATIONS.get(namespace);
    }

    //region Root Init

    /**
     * Creates all the needed dirs and the .properties Confile File
     */
    public ConfigBase createConfigRoot() {

        FileHandler.delete(this.propertiesPath);
        // Regenerate everything
        DirHandler.create(this.dirPath);
        FileHandler.createFile(this.propertiesPath);

        setupDefaultProperties();

        LOGGER.info("Generated Configs for \"{}\"", this.namespace);

        return this;
    }
    //endregion

    //region Config Build

    ConfigBuilder cb = new ConfigBuilder(this);

    public ConfigBase setupDefaultProperties() {

        FileOutputStream pos;
        try {
            pos = new FileOutputStream(this.propertiesFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(pos));

        try {
            bw.write(cb.header());
            bw.write(cb.defaultOptions());
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public ConfigBase setupResourceProperties() {

        try {

            FileWriter fw = new FileWriter(this.propertiesFile, true);

            //BufferedWriter writer give better performance
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(cb.resourceOptions());
            bw.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    public ConfigBase readProperties() { // TODO: Separation could be needed

        if (Files.exists(this.propertiesPath)) {

            Properties config = new Properties();

            try (InputStream input = new FileInputStream(this.propertiesFile)) {
                config.load(input);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (config.isEmpty()) {   // File can exist AND be empty at the same time
                LOGGER.warn("Can't read as the Config File is empty, trying to regenerate");
                setupDefaultProperties();
                return this;
            }

            this.configLocked = getBooleanOption(this, this.namespace + "ConfigLocked", false);
            //this.resourceLocked = Boolean.valueOf(config.getProperty("resourceLocked").toString());

            this.resourcesLocked.forEach((name, value) -> {
                this.resourcesLocked.put(name, getOption(this, name, value));
            });

            this.addonOptions.forEach((name, value) -> {
                this.addonOptions.put(name, getOption(this, name, value));
            });
        }

        return this;
    }

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
     * Write the Properties added with {@link #addProperty} to the Config File
     */
    public ConfigBase registerProperties() {

        if (Files.exists(this.propertiesPath)) {
            try {

                FileWriter fw = new FileWriter(this.propertiesFile, true);

                String original = Files.readString(this.propertiesPath).strip();
                String additional = cb.additionalOptions();

                new FileWriter(this.propertiesFile, false).close();

                BufferedWriter bw = new BufferedWriter(fw);

                bw.write(original);
                bw.write(System.lineSeparator());

                bw.write(additional); // TODO: Deal with extra newlines
                bw.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return this;
    }

    public ConfigBase overwrite(String name, String newValue) {

        Properties config = new Properties();

        try (InputStream input = new FileInputStream(this.propertiesFile)) {
            config.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String oldValue = config.getProperty(name);

        if (oldValue != null) {
            try {
                String file = Files.readString(this.propertiesPath);
                file = file.replace(name + " = " + oldValue, name + " = " + newValue);
                FileHandler.writeToFile(file, this.propertiesPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return this;
    }
    //endregion

    public ConfigBase openConfigFile() {
        Util.getOperatingSystem().open(this.propertiesFile);
        return this;
    }
}
