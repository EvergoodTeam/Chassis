package evergoodteam.chassis.util;

import org.slf4j.Logger;

import java.nio.file.Path;

import static evergoodteam.chassis.util.Reference.CMI;
import static org.slf4j.LoggerFactory.getLogger;

public class FileUtils {

    private static final Logger LOGGER = getLogger(CMI + "/U/File");

    /**
     * Checks whether the specified file exists and has the specified file extension
     */
    public static boolean ofExtension(Path path, String extension) {
        if (path.toFile().exists()) return StringUtils.hasExtension(path, extension);
        LOGGER.error("Specified file with extension \"{}\" at path {} doesn't exist", extension, path);
        return false;
    }
}
