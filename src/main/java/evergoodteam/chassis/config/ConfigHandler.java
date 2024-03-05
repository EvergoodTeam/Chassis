package evergoodteam.chassis.config;

import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;

import static evergoodteam.chassis.util.Reference.CMI;
import static org.slf4j.LoggerFactory.getLogger;

@Log4j2
public class ConfigHandler {

    private static final Logger LOGGER = getLogger(CMI + "/C/Handler");
    private final ConfigBase config;
    private final ConfigSerializer serializer;

    public ConfigHandler(ConfigBase config) {
        this.config = config;
        this.serializer = config.getWriter().getSerializer();
    }

    public void setup() {
        readAllOptions();  // Update stored options (which are CURRENTLY just initialized to default value) to the written values.
        if (!config.getLock().getValue() || versionUpdated()) { // Resets if lock is still off after read or versions are mismatched
            LOGGER.warn("Regenerating Configs \"{}\"", config.getIdentifier());
            config.getWriter().createBase();
        } else LOGGER.info("Configs \"{}\" already exist, skipping first generation", config.getIdentifier());
    }

    public void writeResourceLocks(){
        config.getWriter().updateInternalResourceLockFromStored();
        config.getWriter().overwriteWithStored();
    }

    /**
     * Writes to the config file the stored user options
     */
    public void writeUserOptions() {
        config.getWriter().updateInternalUserFromStored();
        config.getWriter().overwriteWithStored();
    }

    /**
     * Reads the written option values and assigns them to the stored options.
     */
    public void readAllOptions() {
        //log.error("READING ALL OPTIONS FOR: " + config.modid);
        readLocks();
        readUserOptions();
    }

    public void readLocks() {
        config.getWriter().updateStoredConfigLockFromWritten();
        config.getWriter().updateStoredResourceLockFromWritten();
    }

    public void readUserOptions() {
        config.getWriter().updateStoredUserFromWritten();
    }

    /**
     * Returns true if the version written in the config file isn't the same as the mod's and strict versioning is enabled for this config.
     */
    public boolean versionUpdated() {
        return config.isStrictVersioningEnabled() && !serializer.getWrittenModVersion().equals(serializer.getModVersion());
    }

    /**
     * Returns true if any stored user option from {@link evergoodteam.chassis.config.option.OptionStorage OptionStorage} has a different value
     * from the one written in the config file.
     */
    public boolean userOptionDesync() {
        return !serializer.getMappedWrittenUserOptions().equals(serializer.getMappedStoredUserOptions());
    }

    /**
     * Returns true if any stored option from {@link evergoodteam.chassis.config.option.OptionStorage OptionStorage} has a different value
     * from the one written in the config file.
     */
    public boolean optionDesync() {
        return !serializer.getMappedWrittenOptions().equals(serializer.getMappedStoredOptions());
    }
}
