package evergoodteam.chassis.configs;

import evergoodteam.chassis.util.SetUtils;
import evergoodteam.chassis.util.StringUtils;
import evergoodteam.chassis.util.handlers.FileHandler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static evergoodteam.chassis.util.Reference.CMI;
import static org.slf4j.LoggerFactory.getLogger;

public class ConfigBuilder {

    private static final Logger LOGGER = getLogger(CMI + "/C/B");

    private final String NL = System.lineSeparator();

    private ConfigBase config;
    private Path path;
    private File file;

    public ConfigBuilder(@NotNull ConfigBase config) {
        this.config = config;
        this.path = config.propertiesPath;
        this.file = config.propertiesFile;
    }

    public void setupDefaultProperties() {

        FileOutputStream pos;
        try {
            pos = new FileOutputStream(this.file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(pos));

        try {
            bw.write(header());
            bw.write(defaultOptions());
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setupResourceProperties() {

        try {
            FileWriter fw = new FileWriter(this.file, true);

            //BufferedWriter writer give better performance
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(resourceOptions());
            bw.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Write the Properties added with {@link ConfigBase#addProperty} to the Config File
     */
    public void registerProperties() {

        if (Files.exists(this.path)) {
            try {

                String additional = additionalOptions();

                if (!additional.isEmpty()) { // Avoid rewriting when nothing is missing

                    FileWriter fw = new FileWriter(this.file, true);
                    BufferedWriter bw = new BufferedWriter(fw);

                    String original = Files.readString(this.path).strip();

                    new FileWriter(this.file, false).close();

                    bw.write(original);
                    bw.write(System.lineSeparator());
                    bw.write(additional);

                    bw.close();

                    updateHeader();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void overwrite(String name, String newValue) {

        Properties config = new Properties();

        try (InputStream input = new FileInputStream(this.file)) {
            config.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String oldValue = config.getProperty(name);

        if (oldValue != null) {
            try {
                String file = Files.readString(this.path);
                file = file.replace(name + " = " + oldValue, name + " = " + newValue);
                FileHandler.writeToFile(file, this.path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void updateHeader() {

        if (Files.exists(this.path)) {

            List<String> contents = ConfigHandler.getContents(this.config);

            FileHandler.emptyFile(this.file);

            try {

                FileWriter fw = new FileWriter(this.file, true);

                BufferedWriter bw = new BufferedWriter(fw);

                //LOGGER.info("Attempting to update Header");

                bw.write(header());

                for (int i = 2; i < contents.size(); i++) {
                    if (i == 2) {
                        bw.write(contents.get(i).strip());
                    } else {
                        bw.write(contents.get(i));
                        bw.write(System.lineSeparator());
                    }
                }
                bw.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String header() {

        String result = "# %s Configs".formatted(StringUtils.capitalize(config.namespace)) + NL
                + "# " + new Date() + NL + NL;

        return result;
    }

    public String header(String text) {
        String line = "#".repeat(81);
        String result = line + NL + "# " + text + NL + line + NL + NL;

        return result;
    }

    public String defaultOptions() {
        String result = "# Lock " + StringUtils.capitalize(config.namespace) + " configs from being regenerated" + NL
                + config.namespace + "ConfigLocked = " + config.configLocked + NL;

        return result;
    }

    public String resourceOptions() {

        StringBuilder sb = new StringBuilder();

        sb.append("# Lock " + StringUtils.capitalize(config.namespace) + " resources from being regenerated" + NL);
        config.resourcesLocked.forEach((name, value) -> {
            sb.append(name + " = " + value + NL);
        });

        sb.append(NL);

        return sb.toString();
    }

    public String additionalOptions() {

        StringBuilder sb = new StringBuilder();

        Properties p = new Properties();

        try (InputStream input = new FileInputStream(config.propertiesFile)) {
            p.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        config.addonOptions.forEach((name, value) -> {

            int index = SetUtils.getIndex(config.addonOptions.keySet(), name);

            if (p.getProperty(name) == null) { // Property is missing, add with default value
                //LOGGER.info("Found missing property \"{}\", adding to File", name);

                if (!"".equals(config.addonComments.get(index)))
                    sb.append(NL + "# " + config.addonComments.get(index) + NL);
                else sb.append(NL);

                sb.append(name + " = " + value);

            } else { // Property exists, fetch the value and overwrite Map
                config.addonOptions.put(name, p.getProperty(name));
            }
        });

        return sb.toString();
    }
}
