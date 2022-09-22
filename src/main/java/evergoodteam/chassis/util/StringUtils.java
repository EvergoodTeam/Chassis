package evergoodteam.chassis.util;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public class StringUtils {

    /**
     * Capitalizes the first letter from the provided string
     */
    public static String capitalize(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    /**
     * Removes the last occurrence of the first target that is found from the original string
     *
     * @param original    starting string
     * @param targets     strings to be removed
     * @param replacement substitute for the targets
     */
    public static String removeLastOccurrence(String original, String[] targets, String replacement) {
        for (String target : targets) {
            if (removeLastOccurrence(original, target, replacement).equals(original)) continue;
            return removeLastOccurrence(original, target, replacement);
        }
        return original;
    }

    /**
     * Removes the last occurrence of the target from the original string
     *
     * @param original    starting string
     * @param target      string to be removed
     * @param replacement substitute for the targets
     */
    public static String removeLastOccurrence(String original, String target, String replacement) {
        int index = original.lastIndexOf(target);
        if (index == -1) return original;
        return original.substring(0, index) + replacement + original.substring(index + target.length());
    }

    /**
     * Checks if the provided file path has the specified file extension
     *
     * @param path      file path
     * @param extension wanted extension
     */
    public static boolean hasExtension(@NotNull Path path, @NotNull String extension) {
        String ext = "." + lastFromSplit(path.getFileName().toString(), "\\.");
        return ext.equals(extension);
    }

    /**
     * Checks if the provided file path has the specified file extension
     *
     * @param path file path
     */
    public static boolean hasExtension(@NotNull Path path) {
        String[] split = path.getFileName().toString().split("\\.");
        return split.length > 1;
    }

    /**
     * Returns the last split of the string
     *
     * @param string starting string
     * @param regex  search pattern to follow when splitting
     */
    public static String lastFromSplit(String string, String regex) {
        String[] split = string.split(regex);
        return split[split.length - 1];
    }

    /**
     * Returns the first split of the string
     *
     * @param string starting string
     * @param regex  search pattern to follow when splitting
     */
    public static String firstFromSplit(String string, String regex) {
        String[] split = string.split(regex);
        return split[0];
    }

    /**
     * Extracts the file name and creates a path to the same parent directory with the wanted file extension,
     * removing any other that is present
     *
     * @param path      path to the file, with any extension
     * @param extension wanted extension
     */
    public static @NotNull Path addExtension(@NotNull Path path, @NotNull String extension) {
        String fileName = path.getFileName().toString();
        return path.resolveSibling(addExtension(fileName, extension));
    }

    /**
     * Adds the specified file extension (e.g. ".json") to the provided string, removing any other that is present
     *
     * @param fileName  name of your file with any extension
     * @param extension wanted extension
     */
    public static @NotNull String addExtension(String fileName, String extension) {
        return fileName.split("\\.")[0] + extension;
    }
}
