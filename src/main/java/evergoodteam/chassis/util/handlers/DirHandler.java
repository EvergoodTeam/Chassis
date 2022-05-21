package evergoodteam.chassis.util.handlers;

import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static evergoodteam.chassis.util.Reference.getLogger;

public class DirHandler {

    private static final Logger LOGGER = getLogger("Dir");

    /**
     * <p> Creates a Directory at the specified Path if one doesn't exist already </p>
     * <p> Also creates missing parent Dirs from the specified Path </p>
     *
     * @param path
     */
    public static void create(Path path) {

        //LOGGER.info("Attempting to create Dir at {}", path);

        if (!Files.exists(path)) {
            new File(path.toString()).mkdirs();
            //LOGGER.info("Created Dir at {}", path);
        }
        //else log.info("Dir {} exists already", path);
    }

    /**
     * Creates a Dir at the specified parent Path for each String provided
     *
     * @param parent
     * @param children
     */
    public static void create(Path parent, String @NotNull [] children) {

        for (String child : children) {
            create(parent.resolve(child));
        }
    }

    public static void clean(Path path) {
        try {
            if (Files.exists(path)) {
                FileUtils.cleanDirectory(path.toFile());
                //log.info("Deleted {}", this.propertiesPath);
            }
        } catch (IOException e) {
            LOGGER.error("Error on cleaning {}", path, e);
        }
    }
}
