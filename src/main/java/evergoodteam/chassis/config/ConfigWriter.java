package evergoodteam.chassis.config;

import com.google.common.io.CharSink;
import evergoodteam.chassis.util.DirectoryUtils;
import evergoodteam.chassis.util.FileUtils;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static evergoodteam.chassis.util.Reference.CMI;
import static org.slf4j.LoggerFactory.getLogger;

@Log4j2
public class ConfigWriter {

    private static final Logger LOGGER = getLogger(CMI + "/C/Writer");
    private final ConfigBase config;
    private final ConfigSerializer serializer;
    private final ConfigCharSink charSink;
    private List<String> header = new ArrayList<>();
    private List<String> configLock = new ArrayList<>();
    private List<String> resourceLock = new ArrayList<>();
    private List<String> user = new ArrayList<>();

    public ConfigWriter(ConfigBase config) {
        this.config = config;
        this.serializer = new ConfigSerializer(config);
        this.charSink = new ConfigCharSink(config);
    }

    public ConfigSerializer getSerializer() {
        return this.serializer;
    }

    /**
     * Creates the parent dirs and the config file with the required default variables and enabled the config file lock.
     */
    public void createBase() {
        FileUtils.delete(config.propertiesPath);
        DirectoryUtils.create(config.dirPath);
        FileUtils.createFile(config.propertiesPath);

        config.getLock().setValue(true);

        updateInternalConfigLockFromStored();
        updateInternalResourceLockFromStored();
        updateInternalUserFromStored();
        overwriteWithStored();

        LOGGER.info("Generated and locked Configs for \"{}\"", config.getIdentifier());
    }

    /**
     * Sets the config lock's value to its written value and returns {@code true} if such written value is first
     * found in the config file; otherwise returns {@code false}.
     */
    public void updateStoredConfigLockFromWritten() {
        // Only contains keys that stored config lock cat also has
        Map<String, String> cfg = serializer.getMappedWrittenConfigLock();

        config.getOptionStorage().getConfigLockCat().getOptions()
                .forEach(option -> option.setValueFromString(cfg.get(option.getName())));

        //log.error("UPDATING WRITER CONFIG WITH STORED AFTER ASSIGNING");

        updateInternalConfigLockFromStored();
    }

    public void updateStoredResourceLockFromWritten() {
        // Only contains keys that stored config lock cat also has
        Map<String, String> written = serializer.getMappedWrittenResourceLock();

        config.getOptionStorage().getResourceLockCat().getOptions()
                .forEach(option -> option.setValueFromString(written.get(option.getName())));

        //log.error("UPDATING WRITER RESOURCE WITH STORED AFTER ASSIGNING");

        updateInternalResourceLockFromStored();
    }

    public void updateStoredUserFromWritten() {
        // Only contains keys that stored config lock cat also has
        Map<String, String> written = serializer.getMappedWrittenUserOptions();

        written.forEach((key, value) -> config.getOptionStorage().getOption(key).setValueFromString(value));

        //log.error("UPDATING WRITER USER WITH STORED AFTER ASSIGNING");

        updateInternalUserFromStored();
    }

    @ApiStatus.Internal
    public void updateInternalHeader() {
        header = new ArrayList<>();
        header.add(serializer.title());
        header.add(serializer.lastModified());
        //header.add("");
    }

    @ApiStatus.Internal
    public void updateInternalConfigLockFromStored() {
        configLock = new ArrayList<>(serializer.configLock());
        //configLock.add("");

        //log.error("UPDATING WRITER CONFIG WITH STORED");
    }

    @ApiStatus.Internal
    public void updateInternalResourceLockFromStored() {
        resourceLock = new ArrayList<>(serializer.resourceLock());
        //resourceLock.add("");

        //log.error("UPDATING WRITER RESOURCE WITH STORED");
    }

    @ApiStatus.Internal
    public void updateInternalUserFromStored() {
        user = new ArrayList<>();

        config.getOptionStorage().getCategories().stream().filter(category -> !category.isConfigLockCat() && !category.isResourceLockCat())
                .forEach(category -> {
                    user.addAll(serializer.category(category));
                    category.getOptions().forEach(option -> user.addAll(serializer.commentedProperty(option)));
                });

        //log.error("UPDATING WRITER USER WITH STORED");
    }

    public void overwriteWithStored() {
        if (serializer.getMappedWrittenOptions().equals(serializer.getMappedStoredOptions())) return;

        updateInternalHeader();
        try {
            overwriteFile(getAllLines());
            //log.error("OVERRIDING");
        } catch (IOException e) {
            LOGGER.error("There was an error writing to the config file: %s".formatted(e));
            //throw new RuntimeException(e);
        }
    }

    private List<String> getAllLinesWithoutHeader() {
        return Stream.of(configLock, resourceLock, user).flatMap(Collection::stream).collect(Collectors.toList());
    }

    private List<String> getAllLines() {
        return Stream.of(header, configLock, resourceLock, user).flatMap(Collection::stream).collect(Collectors.toList());
    }

    private void overwriteFile(List<String> lines) throws IOException {
        charSink.writeLines(lines);
    }

    public static class ConfigCharSink extends CharSink {

        private final ConfigBase config;

        public ConfigCharSink(ConfigBase config) {
            this.config = config;
        }

        @Override
        public Writer openStream() throws IOException {
            return Files.newBufferedWriter(config.propertiesPath);
        }
    }
}
