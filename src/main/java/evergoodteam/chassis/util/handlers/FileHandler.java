package evergoodteam.chassis.util.handlers;

import evergoodteam.chassis.util.StringUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
public class FileHandler {

    //private static final Logger LOGGER = getLogger("File");

    public static void emptyFile(Path path) {
        emptyFile(path.toFile());
    }

    public static void emptyFile(File file) {

        if (file.exists()) {
            try {
                new FileWriter(file, false).close();
            } catch (IOException e) {
                log.error("Error on emptying File at {}", file.getPath(), e);
            }
        } else {
            log.warn("Provided Path is not valid: {}", file.getPath());
        }
    }

    /**
     * Writes provided String to the File identified with the Path
     *
     * @param text
     * @param path
     */
    public static void writeToFile(String text, Path path) {

        //log.info("Attempting to write to path {}", path);

        if (Files.exists(path)) {
            try (Writer fileWriter = new FileWriter(path.toFile())) {
                fileWriter.write(text);
            } catch (IOException e) {
                log.error("Error on writing to File at {}", path, e);
            }
        } else {
            log.warn("Provided Path is not valid: {}", path);
        }
    }

    public static void createJsonFile(Path path) {
        Path actual = StringUtils.checkDuplicateExtension(path, ".json");
        //log.info(actual);
        createFile(actual, ".json");
    }

    /**
     * Creates a File only if one with the same Path doesn't exist already
     *
     * @param path
     * @param extension .txt .json etc.
     */
    public static void createFile(Path path, String extension) {
        Path actual = StringUtils.checkDuplicateExtension(path, extension);
        createFile(Paths.get(actual + extension));
    }

    /**
     * Creates a File only if one with the same Path doesn't exist already
     *
     * @param path Note: must include the File extension
     * @throws IOException
     */
    public static void createFile(Path path) {
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                log.error("Error on creating File at {}", path, e);
            }
        }
        //else log.info("Unable to create file as it already exists at {}", path);
    }

    /**
     * Delete a File at the specified Path
     *
     * @param path path of your File
     * @throws java.nio.file.DirectoryNotEmptyException if used with Dirs
     */
    public static void delete(Path path) {
        try {
            if (Files.exists(path)) {
                FileUtils.delete(path.toFile());
                //log.info("Deleted File at {}", path);
            }
        } catch (IOException e) {
            log.error("Error on deleting File at {}", path, e);
        }
    }
}
