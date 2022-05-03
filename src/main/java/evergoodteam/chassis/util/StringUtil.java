package evergoodteam.chassis.util;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;

public class StringUtil {

    public static String capitalize(String input){
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    /**
     * Removes specified extension from the provided String if found
     *
     * @param input
     * @param extension
     * @return
     */
    public static @NotNull String checkDuplicateExtension(String input, String extension) {

        String result = input;

        if (!result.contains(extension)) {
            result = result.replace(extension, "");
        }

        return result;
    }

    public static @NotNull Path checkDuplicateExtension(@NotNull Path path, String extension) {

        Path result = Paths.get(checkDuplicateExtension(path.toString(), extension));
        return result;
    }

    /**
     * Adds specified extension to the provided String if missing
     *
     * @param input
     * @param extension
     * @return
     */
    public static @NotNull String checkMissingExtension(String input, String extension) {

        String result = input;

        if (!result.contains(extension)) {
            result = result + extension;
        }

        return result;
    }

    public static @NotNull Path checkMissingExtension(@NotNull Path path, String extension) {

        Path result = Paths.get(checkMissingExtension(path.toString(), extension));

        return result;
    }
}
