package evergoodteam.chassis.configs;

import evergoodteam.chassis.objects.resourcepacks.ResourcePackBase;
import evergoodteam.chassis.util.handlers.DirHandler;
import evergoodteam.chassis.util.handlers.FileHandler;
import lombok.extern.log4j.Log4j2;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.util.StringUtil;
import net.minecraft.util.Util;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static evergoodteam.chassis.util.Reference.LOGGER;

@Log4j2
public class ConfigBase {

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


    private static PropertiesConfiguration properties(@NotNull ConfigBase config) {

        try {
            return new PropertiesConfiguration(config.propertiesPath);
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates all the needed dirs and the .properties Confile File
     */
    public ConfigBase createConfigRoot() {

        this.cleanConfigFiles();

        // Regenerate everything
        DirHandler.createDir(this.dirPath);
        FileHandler.createFile(Paths.get(this.propertiesPath));

        setupDefaultProperties();

        LOGGER.info("Generated Configs for \"{}\"", this.namespace);

        return this;
    }

    public ConfigBase cleanConfigFiles() {

        try {
            if (Files.exists(Paths.get(this.propertiesPath))) {
                FileUtils.delete(Paths.get(this.propertiesPath).toFile());
                //log.info("Deleted {}", this.propertiesPath);
            }

        } catch (IOException e) {
            log.error("Error on cleaning directory {}", this.dirPath, e);
        }

        return this;
    }

    public ConfigBase cleanConfigDir() {

        try {
            FileUtils.deleteDirectory(this.dirPath.toFile());
        } catch (IOException e) {
            log.error(e);
        }

        return this;
    }

    public ConfigBase cleanResources(@NotNull ResourcePackBase resourcePack) {

        try {
            FileUtils.deleteDirectory(resourcePack.path.toFile());
        } catch (IOException e) {
            log.error(e);
        }

        return this;
    }

    private String header(@NotNull ConfigBase config) {
        return "Config Options for " + StringUtil.capitalize(config.namespace) + System.lineSeparator() + new Date();
    }

    public ConfigBase setupDefaultProperties() {

        PropertiesConfiguration config = properties(this);

        config.getLayout().setHeaderComment(header(this));

        config.setProperty("configLocked", this.configLocked);
        //config.setProperty("resourceLocked", this.resourceLocked);

        this.resourcesLocked.forEach((name, value) -> {

            config.setProperty(name, value);
        });


        try {
            config.save();
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    public ConfigBase readProperties() {

        if (Files.exists(Paths.get(this.propertiesPath))) { // TODO: File can exist but can be empty at the same time

            PropertiesConfiguration config = properties(this);

            this.configLocked = Boolean.valueOf(config.getProperty("configLocked").toString());
            //this.resourceLocked = Boolean.valueOf(config.getProperty("resourceLocked").toString());

            this.resourcesLocked.forEach((name, value) -> {
                this.resourcesLocked.put(name, config.getProperty(name));
            });

            this.options.forEach((name, value) -> {
                this.options.put(name, config.getProperty(name));
            });
        }

        return this;
    }

    public ConfigBase addProperties() {

        if (Files.exists(Paths.get(this.propertiesPath))) {

            PropertiesConfiguration config = properties(this);

            config.getLayout().setBlancLinesBefore(options.keySet().toArray()[0].toString(), 1);

            this.options.forEach((name, value) -> {

                if (config.getProperty(name) == null) { // Property is missing, add with default value
                    config.getLayout().setHeaderComment(header(this));
                    config.setProperty(name, value);
                } else { // Property exists, fetch the value and overwrite Map
                    this.options.put(name, config.getProperty(name));
                }
            });

            try {
                config.save();
            } catch (ConfigurationException e) {
                throw new RuntimeException(e);
            }
        }

        return this;
    }

    public ConfigBase overwriteProperties() {

        if (Files.exists(Paths.get(this.propertiesPath))) {

            PropertiesConfiguration config = properties(this);

            this.options.forEach((name, value) -> {

                config.getLayout().setHeaderComment(header(this));
                config.setProperty(name, value);
            });

            try {
                config.save();
            } catch (ConfigurationException e) {
                throw new RuntimeException(e);
            }

            readProperties();
        }

        return this;
    }


    public ConfigBase openConfigFile() {
        Util.getOperatingSystem().open(Paths.get(this.propertiesPath).toFile());
        return this;
    }
}
