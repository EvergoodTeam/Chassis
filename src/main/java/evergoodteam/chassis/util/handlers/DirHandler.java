package evergoodteam.chassis.util.handlers;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@Log4j2
public class DirHandler {

    /**
     * Creates a Directory at the specified Path if one doesn't exist already <br>
     * NOTE: Also creates missing parent Dirs
     * @param path
     */
    public static void createDir(Path path){

        //LOGGER.info("Attempting to create Dir at {}", path);

        if(!Files.exists(path)){
            new File(path.toString()).mkdirs();
            //LOGGER.info("Created Dir at {}", path);
        }
        //else log.info("Dir {} exists already", path);
    }

    /**
     * Creates a Dir at the specified root for each String provided
     * @param root
     * @param children
     */
    public static void createDir(Path root, String @NotNull [] children){

        for(String child : children){
            createDir(root.resolve(child));
        }
    }
}
