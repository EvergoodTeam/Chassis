package evergoodteam.chassis.util.handlers;

import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static evergoodteam.chassis.util.Reference.CMI;
import static org.slf4j.LoggerFactory.getLogger;

public class DirHandler {

    private static final Logger LOGGER = getLogger(CMI + "/H/Dir");

    /**
     * Creates a directory at the specified parent path for each string provided <p>
     * NOTE: this DOES NOT create missing parent dirs and will warn you about any
     */
    public static void create(Path parent, String @NotNull [] children) {
        if (Files.isDirectory(parent)) {
            for (String child : children) {
                create(parent.resolve(child));
            }
        } else
            LOGGER.warn("Cant create child directories at {} because parent isn't a directory or is missing!", parent);
    }

    /**
     * Creates a directory at the specified path if one doesn't exist already,
     * while also creating the missing parent dirs
     */
    public static void create(Path path) {
        //LOGGER.info("Attempting to create dir at {}", path);
        if (!Files.exists(path)) {
            boolean success = new File(path.toString()).mkdirs(); // TODO: [NU] debug logging?
            //if (success) LOGGER.info("Created dir at {}", path);
        }
        //else LOGGER.warn("Directory at {} already exists", path);
    }

    /**
     * Cleans the directory at the specified path
     */
    public static void clean(Path path) {
        try {
            if (Files.exists(path)) {
                FileUtils.cleanDirectory(path.toFile());
                //LOGGER.info("Deleted {}", this.propertiesPath);
            }
        } catch (IOException e) {
            LOGGER.error("Error on cleaning {}", path, e);
        }
    }
}
