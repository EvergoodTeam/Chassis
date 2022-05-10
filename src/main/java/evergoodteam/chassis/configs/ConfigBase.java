package evergoodteam.chassis.configs;

import evergoodteam.chassis.util.handlers.DirHandler;
import evergoodteam.chassis.util.handlers.FileHandler;
import lombok.extern.log4j.Log4j2;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Util;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static evergoodteam.chassis.configs.ConfigHandler.getBooleanOption;
import static evergoodteam.chassis.configs.ConfigHandler.getOption;
import static evergoodteam.chassis.util.Reference.LOGGER;

@Log4j2
public class ConfigBase {

    //public static final Logger LOGGER = LogManager.getLogger(StringUtils.capitalize(MODID) + "/ConfigBase");

    public static final Map<String, ConfigBase> CONFIGURATIONS = new HashMap<>();
    private static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir();

    public String namespace;
    public Path dirPath;
    public String propertiesPath;

    public Map<String, Object> resourcesLocked;
    public Map<String, Object> options;

    public Boolean configLocked = false;

    /**
     * Object from which Configs will be generated
     *
     * @param namespace Name of your Configs
     */
    public ConfigBase(String namespace) {
        this.namespace = namespace;
        this.dirPath = CONFIG_DIR.resolve(namespace);
        this.propertiesPath = this.dirPath.resolve(namespace + ".properties").toString();

        CONFIGURATIONS.put(namespace, this);

        this.resourcesLocked = new HashMap<>();
        this.options = new HashMap<>();

        this.readProperties(); // Look for existing values

        if (!this.configLocked) {
            this.configLocked = true;
            this.createConfigRoot(); // Create .properties
        } else LOGGER.info("Configs for \"{}\" already exist, skipping generation", this.namespace);
    }

    //region Root Init

    /**
     * Creates all the needed dirs and the .properties Confile File
     */
    public ConfigBase createConfigRoot() {

        FileHandler.clean(Path.of(this.propertiesPath));
        // Regenerate everything
        DirHandler.createDir(this.dirPath);
        FileHandler.createFile(Paths.get(this.propertiesPath));

        setupDefaultProperties();

        LOGGER.info("Generated Configs for \"{}\"", this.namespace);

        return this;
    }
    //endregion

    //region Config Build

    ConfigBuilder cb = new ConfigBuilder(this);

    public ConfigBase setupDefaultProperties() {

        File propFile = new File(this.propertiesPath);
        FileOutputStream pos;
        try {
            pos = new FileOutputStream(propFile);
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

            FileWriter fw = new FileWriter(this.propertiesPath, true);

            //BufferedWriter writer give better performance
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(cb.resourceOptions());
            bw.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    public ConfigBase readProperties() { // TODO: Separate for resources

        if (Files.exists(Paths.get(this.propertiesPath))) {

            Properties config = new Properties();

            try (InputStream input = new FileInputStream(this.propertiesPath)) {
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

            this.options.forEach((name, value) -> {
                this.options.put(name, getOption(this, name, value));
            });
        }

        return this;
    }

    /**
     * Add a Property to the Config File
     *
     * @param name name of your Property
     * @param value what the Property is equal to
     * @return
     */
    public ConfigBase addProperty(String name, Object value){
        this.options.put(name, value);
        return this;
    }

    /**
     * Write the Properties added with {@link #addProperty} to the Config File
     *
     * @return
     */
    public ConfigBase registerProperties() {

        if (Files.exists(Paths.get(this.propertiesPath))) {
            try {

                FileWriter fw = new FileWriter(this.propertiesPath, true);

                //BufferedWriter writer give better performance
                BufferedWriter bw = new BufferedWriter(fw);

                bw.write(cb.additionalOptions());
                bw.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return this;
    }

    public ConfigBase overwrite(String name, String newValue){

        Properties config = new Properties();

        try (InputStream input = new FileInputStream(this.propertiesPath)) {
            config.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String oldValue = config.getProperty(name);

        if(oldValue != null){
            try {
                String file = Files.readString(Path.of(this.propertiesPath));
                file = file.replace(name + " = " + oldValue, name + " = " + newValue);
                FileHandler.writeToFile(file, Path.of(this.propertiesPath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return this;
    }
    //endregion

    public ConfigBase openConfigFile() {
        Util.getOperatingSystem().open(Paths.get(this.propertiesPath).toFile());
        return this;
    }
}
