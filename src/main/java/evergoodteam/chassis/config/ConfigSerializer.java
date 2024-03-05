package evergoodteam.chassis.config;

import com.google.common.collect.ImmutableCollection;
import evergoodteam.chassis.config.option.AbstractOption;
import evergoodteam.chassis.config.option.CategoryOption;
import evergoodteam.chassis.util.CollectionUtils;
import evergoodteam.chassis.util.FileUtils;
import evergoodteam.chassis.util.StringUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.slf4j.Logger;

import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

import static evergoodteam.chassis.util.Reference.CMI;
import static org.slf4j.LoggerFactory.getLogger;

public class ConfigSerializer {

    private static final Logger LOGGER = getLogger(CMI + "/C/Serializer");
    private final ConfigBase config;

    public ConfigSerializer(ConfigBase config) {
        this.config = config;
    }

    public String title() {
        return "# %s (%s) Configs".formatted(StringUtils.capitalize(config.getIdentifier().toString()), getModVersion());
    }

    public String getModVersion() {
        Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(config.getIdentifier().getNamespace());
        if (modContainer.isPresent()) return modContainer.get().getMetadata().getVersion().getFriendlyString();
        return "";
    }

    public String lastModified() {
        return "# Last Modified: %s".formatted(new Date());
    }

    /**
     * Only lines, no newLines included! Builds a list with config and resource locks.
     */
    public List<String> configLock() {
        List<String> result = new ArrayList<>(category(config.getOptionStorage().getConfigLockCat()));
        result.addAll(commentedProperty(config.getLock()));
        return result;
    }

    public List<String> resourceLock() {
        List<String> result = new ArrayList<>(category(config.getOptionStorage().getResourceLockCat()));
        config.getOptionStorage().getResourceLockCat().getOptions().forEach(option -> result.addAll(commentedProperty(option)));
        return result;
    }

    /**
     * Only lines, no newLines included!
     */
    public List<String> category(CategoryOption categoryOption) {
        List<String> result = new ArrayList<>();
        if (categoryOption.isCommentHidden()) return result;

        result.add("");

        String line = "#".repeat(81);
        String separator = "#" + "-".repeat(79) + "#";

        result.add(line);
        result.add("# %s".formatted(categoryOption.getName()));
        if (!comment(categoryOption).isEmpty()) {
            result.add(separator);
            result.addAll(comment(categoryOption));
        }
        result.add(line);

        return result;
    }

    /**
     * Only lines, no newLines included!
     */
    public List<String> commentedProperty(AbstractOption<?> option) {
        List<String> result = new ArrayList<>(comment(option));
        result.add(property(option));
        return result;
    }

    /**
     * Only lines, no newLines included! Wraps any over-sized lines.
     */
    public List<String> comment(AbstractOption<?> option) {
        if (option.getComment().equals("") || option.isCommentHidden()) return List.of();
        if (option.isDefaultCommentHidden()) return wrappedComment(option);

        List<String> result = wrappedComment(option);
        result.set(result.size() - 1, "%s %s".formatted(result.get(result.size() - 1), defaultValue(option)));
        return result;
    }

    /**
     * Only lines, no newLines included! Wraps any over-sized lines.
     */
    public List<String> wrappedComment(AbstractOption<?> option) {
        return StringUtils.wrapString(option.getComment(), "# ", 81);
    }

    public String defaultValue(AbstractOption<?> option) {
        if (option instanceof AbstractOption.Interval<?> interval) {
            return "[%s, %s]".formatted(defaultValue(option.getDefaultValue()), range(interval.getMin(), interval.getMax()));
        } else if (option.getValues().size() > 1)
            return "[%s, %s]".formatted(defaultValue(option.getDefaultValue()), acceptedValues(option.getValues()));
        return "[%s]".formatted(defaultValue(option.getDefaultValue()));
    }

    public <T> String range(T min, T max) {
        return "range: %s ~ %s".formatted(min, max);
    }

    public <T> String acceptedValues(ImmutableCollection<T> values) {
        return "accepted: %s".formatted(values);
    }

    public <T> String defaultValue(T value) {
        return "default: %s".formatted(value);
    }

    public String property(AbstractOption<?> option) {
        return property(option.getName(), option.getValue());
    }

    public <T> String property(String name, T value) {
        return "%s = %s".formatted(name, value);
    }

    // -------------------------------------------------------------------------------

    public String getWrittenModVersion() {
        Optional<List<String>> contents = getWrittenContents();
        return contents.map(strings -> StringUtils.between(strings.get(0), "(", ")")).orElse("-1");
    }

    public Map<String, String> getMappedWrittenConfigLock() {
        return CollectionUtils.matchMapKeys(getMappedWrittenOptions(), getMappedStoredConfigLock());
    }

    public Map<String, String> getMappedWrittenResourceLock() {
        return CollectionUtils.matchMapKeys(getMappedWrittenOptions(), getMappedStoredResourceLock());
    }

    public Map<String, String> getMappedWrittenUserOptions() {
        return CollectionUtils.matchMapKeys(getMappedWrittenOptions(), getMappedStoredUserOptions());
        /*return getMappedWrittenOptions().keySet().stream().filter(toMatch::containsKey)
                .collect(Collectors.toMap(key -> key, key -> getMappedWrittenOptions().get(key), (a, b) -> b));*/
    }

    /**
     * If the config file exists, returns a map where written options' names are used as keys for their written values. <p>
     * Otherwise, returns an empty map.
     */
    public Map<String, String> getMappedWrittenOptions() {
        Map<String, String> result = new HashMap<>();
        getWrittenOptions().forEach(string -> result.put(string.split(" = ")[0], string.split(" = ")[1]));
        return result;
    }

    /**
     * If the config file exists, returns a list of the options written in the config file: these are obtained by excluding
     * empty lines and ones that start with "#". Otherwise, returns an empty list.
     */
    public List<String> getWrittenOptions() {
        List<String> result = new ArrayList<>();
        Optional<List<String>> contents = getWrittenContents();
        contents.ifPresent(strings -> strings.stream().filter(string -> !string.equals("") && string.charAt(0) != '#')
                .forEach(result::add));
        return result;
    }

    public Optional<List<String>> getWrittenContentsWithoutHeader() {
        if (Files.exists(config.propertiesPath)) {
            List<String> lines = FileUtils.toList(config.propertiesPath);
            return Optional.of(lines.subList(3, lines.size()));
        }
        //LOGGER.error("Config file doesn't exist, can't return a content list.");
        return Optional.empty();
    }

    public Optional<List<String>> getWrittenContents() {
        if (Files.exists(config.propertiesPath)) return Optional.of(FileUtils.toList(config.propertiesPath));
        //LOGGER.error("Config file doesn't exist, can't return a content list.");
        return Optional.empty();
    }

    // -------------------------------------------------------------------------------

    public Map<String, String> getMappedStoredUserOptions() {
        //log.error("SCRAPED STORED USER");
        Map<String, String> cfg = getMappedStoredConfigLock();
        Map<String, String> resource = getMappedStoredResourceLock();
        return getMappedStoredOptions().keySet().stream().filter(key -> !cfg.containsKey(key) && !resource.containsKey(key))
                .collect(Collectors.toMap(key -> key, key -> getMappedStoredOptions().get(key), (a, b) -> b));
    }

    public Map<String, String> getMappedStoredConfigLock() {
        //log.warn("SCRAPED STORED CONFIG LOCK");
        return config.getOptionStorage().getConfigLockCat().getOptions().stream()
                .collect(Collectors.toMap(AbstractOption::getName, option -> String.valueOf(option.getValue()), (a, b) -> b));
    }

    public Map<String, String> getMappedStoredResourceLock() {
        //log.warn("SCRAPED STORED RESOURCE LOCK");
        return config.getOptionStorage().getResourceLockCat().getOptions().stream()
                .collect(Collectors.toMap(AbstractOption::getName, option -> String.valueOf(option.getValue()), (a, b) -> b));
    }

    public Map<String, String> getMappedStoredOptions() {
        return config.getOptionStorage().getOptions().stream()
                .collect(Collectors.toMap(AbstractOption::getName, option -> String.valueOf(option.getValue()), (a, b) -> b));
    }
}