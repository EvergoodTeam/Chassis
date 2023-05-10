package evergoodteam.chassis.configs;

import evergoodteam.chassis.configs.options.AbstractOption;
import evergoodteam.chassis.configs.options.CategoryOption;
import evergoodteam.chassis.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static evergoodteam.chassis.util.Reference.CMI;
import static org.slf4j.LoggerFactory.getLogger;

// TODO: Update name
public class ConfigBuilder {

    private static final Logger LOGGER = getLogger(CMI + "/C/Builder");
    private final String NL = System.lineSeparator();

    private ConfigBase config;
    private Path path;
    private File file;
    public List<String> lines = new ArrayList<>();

    // TODO: use OptionStorage
    public ConfigBuilder(@NotNull ConfigBase config) {
        this.config = config;
        this.path = config.propertiesPath;
        this.file = config.propertiesFile;
    }

    public ConfigBase getConfig() {
        return this.config;
    }

    /*
    public boolean getLine(String propertyName){
        return lines.contains(property(propertyName, ));
    }*/

    public void empty() {
        this.lines = new ArrayList<>();
    }

    /**
     * Writes the default header and options bundled with every .properties file
     */
    public void writeDefaults() {
        empty();
        addLine(title()).addLine(date()).addLine("");
        addLine(defaultOptions());
    }

    public void writeResources() {
        addLine(resourceOptions());
    }

    /**
     * Writes the properties added by the user to the .properties config file
     */
    public void writeAddons() {
        /*
        List<String> contents = ConfigHandler.getContents(this.config);
        contents.subList(0, contents.indexOf(resourceOptions().get(resourceOptions().size() - 1)));
        */

        List<String> addons = addons();

        if (!addons.isEmpty()) {
            //overwrite(contents);
            addLine("");
            addLine(addons);
        }
    }

    private List<String> addons() {
        List<String> result = new ArrayList<>();

        for (CategoryOption categoryOption : config.getOptionStorage().getCategories()) {
            if (!categoryOption.equals(config.getOptionStorage().getGenericCategory())) {
                result.add("");
                result.addAll(category(categoryOption));
                result.add("");
            }
            for (AbstractOption option : categoryOption.getOptions()) {
                if (!comment(option).equals("")) result.add(comment(option));
                result.add(property(option));
            }
        }

        return result;
    }

    public void updateHeader() {
        if (Files.exists(this.path)) {
            lines.set(1, date());
        }
    }

    //region Writing handler
    public void overwrite(String propertyName, String newValue) {
        //List<String> lines = ConfigHandler.getContents(config);
        Map<String, String> properties = ConfigHandler.getPropertyMap(config);
        String oldValue = properties.get(propertyName);

        if (oldValue != null && !oldValue.equals(newValue)) {
            lines.set(lines.indexOf(property(propertyName, oldValue)), property(propertyName, newValue));
            overwrite();
        }
    }

    public void overwrite() {
        updateHeader();
        overwrite(lines);
    }

    private void overwrite(List<String> newFileLines) {
        try {
            Files.write(this.path, newFileLines);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Writes the provided strings with line breaks
     */
    private ConfigBuilder addLine(List<String> lines) {
        //return write(NL, lines.toArray(new String[0]));
        this.lines.addAll(lines);
        return this;
    }

    /**
     * Writes the provided string with a line break
     */
    private ConfigBuilder addLine(String string) {
        //return write(NL, string);
        lines.add(string);
        return this;
    }

    /**
     * Writes the provided strings giving each the specified suffix
     */
    private ConfigBuilder writeAppend(String suffix, String... strings) {
        try (FileWriter fw = new FileWriter(this.file, true)) {
            BufferedWriter bw = new BufferedWriter(fw);

            for (String str : strings) {
                bw.write(str + suffix);
            }
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return this;
    }
    //endregion

    //region Components

    public String title() {
        return "# %s Configs".formatted(StringUtils.capitalize(config.namespace));
    }

    public String date() {
        return "# " + new Date();
    }

    public List<String> category(CategoryOption categoryOption) {
        List<String> category = new ArrayList<>();
        String line = "#".repeat(81);
        String separator = "#" + "-".repeat(79) + "#";
        category.add(line);
        category.add("# " + categoryOption.getName());

        if (!"".equals(categoryOption.getComment())) {
            category.add(separator);
            category.addAll(StringUtils.wrapWords(categoryOption.getComment(), "# ", 79));
        }

        category.add(line);

        return category;
    }

    public List<String> defaultOptions() {
        List<String> defaults = new ArrayList<>();

        defaults.add(comment(config.configLocked));
        defaults.add(property(config.configLocked));

        return defaults;
    }

    public List<String> resourceOptions() {
        List<String> resources = new ArrayList<>();

        config.resourcesLocked.forEach((name, booleanOption) -> {
            resources.add(comment(booleanOption));
            resources.add(property(booleanOption));
        });

        return resources;
    }

    /**
     * Already includes a newline
     */
    private String comment(AbstractOption<?> option) {
        if (!"".equals(option.getComment())) {
            String commentDesc = "# %s".formatted(option.getComment());
            if (!option.defaultHidden()) return "%s %s".formatted(commentDesc, defaultValue(option));
            else return commentDesc;
        }
        return "";
    }

    private String defaultValue(AbstractOption<?> option) {
        if (option instanceof AbstractOption.Interval<?> interval) {
            return defaultRange(interval.getMin(), interval.getMax(), option.getDefaultValue());
        }
        return defaultValue(option.getDefaultValue());
    }

    private <T> String defaultRange(T min, T max, T defaultValue) {
        return "[range: %s ~ %s, default: %s]".formatted(min, max, defaultValue);
    }

    private <T> String defaultValue(T value) {
        return "[default: %s]".formatted(value);
    }

    public String property(AbstractOption<?> option) {
        return property(option.getName(), option.getValue());
    }

    /**
     * Already includes a newline
     */
    public <T> String property(String name, T value) {
        return "%s = %s".formatted(name, value);
    }
    //endregion
}