package evergoodteam.chassis.util.handlers;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Log4j2
public class DirHandler {

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
            log.error("Error on cleaning {}", path, e);
        }
    }
}
