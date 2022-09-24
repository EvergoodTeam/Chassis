package evergoodteam.chassis.util.handlers;

import evergoodteam.chassis.util.StringUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

import static evergoodteam.chassis.util.Reference.CMI;
import static org.slf4j.LoggerFactory.getLogger;

public class FileHandler {

    private static final Logger LOGGER = getLogger(CMI + "/H/File");

    public static void emptyFile(Path path) {
        emptyFile(path.toFile());
    }

    public static void emptyFile(File file) {
        if (file.exists()) {
            try {
                new FileWriter(file, false).close();
                //LOGGER.info("Emptied file at {}", file.getPath());
            } catch (IOException e) {
                LOGGER.error("Error on emptying file at {}", file.getPath(), e);
            }
        } else {
            LOGGER.warn("Provided path is not valid: {}", file.getPath());
        }
    }

    /**
     * Writes the provided string to the file at the specified path
     */
    public static void writeToFile(String text, Path path) {
        //LOGGER.info("Attempting to write to path {}", path);
        if (Files.exists(path)) {
            try (Writer fileWriter = new FileWriter(path.toFile())) {
                fileWriter.write(text);
            } catch (IOException e) {
                LOGGER.error("Error on writing to file at {}", path, e);
            }
        } else {
            LOGGER.warn("Provided path is not valid: {}", path);
        }
    }

    /**
     * Creates a json file at the specified path
     *
     * @param path NOTE: ".json" extension is not required
     */
    public static void createJsonFile(Path path) {
        createFile(path, ".json");
    }

    /**
     * Creates a file only if one with the same Path doesn't exist already
     *
     * @param path      file path
     * @param extension e.g. ".txt", ".json"
     */
    public static void createFile(Path path, String extension) {
        createFile(StringUtils.addExtension(path, extension));
    }

    /**
     * Creates a file only if one with the same path doesn't exist already
     *
     * @param path NOTE: must include the file extension
     */
    public static void createFile(Path path) {
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                LOGGER.error("Error on creating File at {}", path, e);
            }
        }
        //else LOGGER.info("Unable to create file as it already exists at {}", path);
    }

    /**
     * Deletes a file at the specified path
     *
     * @param path path of your file
     */
    public static void delete(Path path) {
        try {
            if (Files.exists(path) && !Files.isDirectory(path)) {
                FileUtils.delete(path.toFile());
                //LOGGER.info("Deleted file at {}", path);
            }
        } catch (IOException e) {
            LOGGER.error("Error on deleting file at {}", path, e);
        }
    }
}
