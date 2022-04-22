package evergoodteam.chassis.configs;

import lombok.extern.log4j.Log4j2;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.util.StringUtil;
import net.minecraft.util.Util;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import evergoodteam.chassis.util.handlers.FileHandler;
import evergoodteam.chassis.util.handlers.DirHandler;

import static evergoodteam.chassis.util.Reference.*;

@Log4j2
public class ConfigBase {

    private static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir();

    public static final Map<String, ConfigBase> CONFIGURATIONS = new HashMap<>();
    public Map<String, Object> OPTIONS;

    public String namespace;
    public Path dirPath;
    public String propertiesPath;

    public Boolean configLocked = false; // Handled here
    public Boolean resourceLocked = false; // Handled in ResourcePackBase

    /**
     * Object from which Configs will be generated
     * @param namespace Name of your Configs
     */
    public ConfigBase(String namespace) {
        this.namespace = namespace;
        this.dirPath = CONFIG_DIR.resolve(namespace);
        this.propertiesPath = this.dirPath.resolve(namespace + ".properties").toString();

        CONFIGURATIONS.put(namespace, this);

        this.OPTIONS = new HashMap<>();

        this.readProperties(); // Look for existing values

        if(!this.configLocked){
            this.configLocked = true;
            this.createConfigRoot(); // Create .properties
        }
        else LOGGER.info("Configs for \"{}\" already exist, skipping creation", this.namespace);
    }

    // TODO: General cleanup

    /**
     * Creates all the needed dirs and the .properties Confile File
     */
    public ConfigBase createConfigRoot(){

        this.cleanConfigFiles();

        // Regenerate everything
        DirHandler.createDir(this.dirPath);
        try {
            FileHandler.createFile(Paths.get(this.propertiesPath));
        } catch (IOException e) {
            log.error(e);
        }
        setupDefaultProperties();
        LOGGER.info("Created Configs for \"{}\"", this.namespace);

        return this;
    }

    public ConfigBase cleanConfigFiles(){

        try {
            if(Files.exists(Paths.get(this.propertiesPath))){
                FileUtils.delete(Paths.get(this.propertiesPath).toFile());
                //log.info("Deleted {}", this.propertiesPath);
            }

        } catch (IOException e) {
            log.error("Error on cleaning directory {}", this.dirPath, e);
        }

        return this;
    }

    public ConfigBase cleanConfigDir(){

        try {
            FileUtils.deleteDirectory(this.dirPath.toFile());
        } catch (IOException e) {
            log.error(e);
        }

        return this;
    }

    public ConfigBase cleanResources(){

        try {
            FileUtils.deleteDirectory(this.dirPath.resolve("resourcepacks").toFile());
        } catch (IOException e) {
            log.error(e);
        }

        return this;
    }


    public ConfigBase setupDefaultProperties(){

        try {
            PropertiesConfiguration config = new PropertiesConfiguration(this.propertiesPath);

            config.getLayout().setHeaderComment("Config Options for " + StringUtil.capitalize(this.namespace));
            config.getLayout().setComment("configLocked", new Date().toString());

            config.setProperty("configLocked", this.configLocked);
            config.setProperty("resourceLocked", this.resourceLocked);

            config.save();

            readProperties();

        } catch (ConfigurationException e) {
            log.error(e);
        }

        return this;
    }

    public ConfigBase readProperties(){

        if(Files.exists(Paths.get(this.propertiesPath))){

            try {
                PropertiesConfiguration config = new PropertiesConfiguration(this.propertiesPath);

                this.configLocked = Boolean.valueOf(config.getProperty("configLocked").toString());
                this.resourceLocked = Boolean.valueOf(config.getProperty("resourceLocked").toString());

                this.OPTIONS.forEach((name, value) -> {
                    this.OPTIONS.put(name, config.getProperty(name));
                });

            } catch (ConfigurationException e) {
                log.error(e);
            }
        }

        return this;
    }

    public ConfigBase addProperties(){

        if(Files.exists(Paths.get(this.propertiesPath))){

            try {
                PropertiesConfiguration config = new PropertiesConfiguration(this.propertiesPath);

                this.OPTIONS.forEach((name, value) -> {

                    if(config.getProperty(name) == null){ // Property is missing, add with default value
                        config.getLayout().setComment("configLocked", new Date().toString()); // Update Date
                        config.setProperty(name, value);
                    }
                    else{ // Property exists, fetch the value and overwrite Map
                        this.OPTIONS.put(name, config.getProperty(name));
                    }
                });

                config.save();

            } catch (ConfigurationException e) {
                log.error(e);
            }
        }

        return this;
    }


    public ConfigBase openConfigFile(){
        Util.getOperatingSystem().open(Paths.get(this.propertiesPath).toFile());
        return this;
    }
}
