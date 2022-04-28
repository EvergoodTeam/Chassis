package evergoodteam.chassis.util.handlers;

import evergoodteam.chassis.util.StringUtil;
import lombok.extern.log4j.Log4j2;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
public class FileHandler {

    /**
     * Writes provided String to the File identified with the Path
     *
     * @param text
     * @param path
     */
    public static void writeToFile(String text, Path path) {

        //LOGGER.info("Attempting to write to path {}", path);

        if (Files.exists(path)) {

            try (FileWriter fileWriter = new FileWriter(path.toFile())) {

                fileWriter.write(text);
            } catch (IOException e) {
                log.error("Error on writing to File", e);
            }
        } else {
            log.warn("Provided Path is not valid");
        }
    }

    public static void createJsonFile(Path path) {

        Path actual = StringUtil.checkDuplicateExtension(path, ".json");
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

        Path actual = StringUtil.checkDuplicateExtension(path, extension);

        createFile(Paths.get(actual + extension));
    }

    /**
     * Creates a File only if one with the same Path doesn't exist already
     *
     * @param path Must include the File extension
     * @throws IOException
     */
    public static void createFile(Path path) {

        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                log.error("Error on creating File", e);
            }
        }
        //else log.info("Unable to create file as it already exists");
    }
}
