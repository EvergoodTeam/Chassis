package evergoodteam.chassis.util.handlers;

import lombok.extern.log4j.Log4j2;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static evergoodteam.chassis.util.Reference.LOGGER;

@Log4j2
public class FileHandler {

    /**
     * Writes provided String to the File identified with the Path
     * @param text
     * @param path
     */
    public static void writeToFile(String text, Path path){

        //LOGGER.info("Attempting to write to path {}", path);

        if(Files.exists(path)){

            try (FileWriter fileWriter = new FileWriter(path.toFile())) {

                fileWriter.write(text);
            } catch (IOException e) {
                log.error("Error on writing to File", e);
            }
        }

        else{
            log.warn("Provided Path is not valid");
        }
    }

    public static void createJsonFile(Path path){

        Path actual = path;

        if(path.toString().contains(".json")) actual = Paths.get(path.toString().replace(".json", ""));

        //log.info(actual);

        try {
            createFile(actual, ".json");
        } catch (IOException e) {
            LOGGER.error("Error on creating Json file", e);
        }
    }

    /**
     * Creates a File only if one with the same Path doesn't exist already
     * @param path
     * @param extension .txt .json etc.
     * @throws IOException
     */
    public static void createFile(Path path, String extension) throws IOException {

        Path actual = Paths.get(path + extension);
        createFile(actual);
        //else log.info("Unable to create file as it already exists");
    }

    /**
     * Creates a File only if one with the same Path doesn't exist already
     * @param path Must include the File extension
     * @throws IOException
     */
    public static void createFile(Path path) throws IOException {

        if(!Files.exists(path)){
            Files.createFile(path);
        }
        //else log.info("Unable to create file as it already exists");
    }
}
