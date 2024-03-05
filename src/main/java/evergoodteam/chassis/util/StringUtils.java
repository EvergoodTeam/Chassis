package evergoodteam.chassis.util;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class StringUtils {

    /**
     * Wraps a string into lines with the specified length
     *
     * @param string     your string
     * @param lineLength how long a line should be
     */
    public static List<String> wrapString(String string, int lineLength) {
        return wrapString(string, "", lineLength);
    }

    /**
     * Wraps a string into lines with the specified length
     *
     * @param string     your string
     * @param linePrefix prefix for each line, e.g. "# "
     * @param lineLength how long a line should be
     */
    public static List<String> wrapString(String string, String linePrefix, int lineLength) {
        List<String> result = new ArrayList<>();
        String nextLine = string;

        int index = 1;
        for(int i = 0; i < index; i++){
            if (nextLine.length() > lineLength) {
                String cut = nextLine.substring(0, nextLine.length() - (nextLine.length() - lineLength) - linePrefix.length() - 1);
                nextLine = nextLine.substring(lineLength - linePrefix.length() - 1);

                if(nextLine.charAt(0) == ' ') nextLine = nextLine.substring(1);
                else if(cut.charAt(cut.length() - 1) != ' ') cut += "-";

                result.add(linePrefix + cut);
                index++;
            }
            else result.add(linePrefix + nextLine);
        }

        return result;
    }

    public static String replaceWithCapital(String string, String... regex){
        String result = string;
        for(String reg : regex){
            result = replaceWithCapital(result, reg);
        }
        return result;
    }

    public static String replaceWithCapital(String string, String regex){
        return Pattern.compile(regex).matcher(string).replaceAll(result -> result.group(1).toUpperCase());
    }

    public static String replaceWith(String string, String newChar, String... regex){
        String result = string;
        for(String reg : regex){
            result = replaceWith(result, newChar, reg);
        }
        return result;
    }

    public static String replaceWith(String string, String newChar, String regex){
        return Pattern.compile(regex).matcher(string).replaceAll(result -> newChar);
    }

    /**
     * Capitalizes the first letter of the provided string
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
     * Checks if the provided file path has a file extension
     *
     * @param path file path
     */
    public static boolean hasExtension(@NotNull Path path) {
        String[] split = path.getFileName().toString().split("\\.");
        return split.length > 1;
    }

    /**
     * Extracts the file name and creates a path to the same parent directory with the wanted file extension,
     * removing any extension previously present
     *
     * @param path      path to the file (with any extension)
     * @param extension wanted extension
     */
    public static @NotNull Path addExtension(@NotNull Path path, @NotNull String extension) {
        String fileName = path.getFileName().toString();
        return path.resolveSibling(addExtension(fileName, extension));
    }

    /**
     * Adds the specified file extension (e.g. ".json") to the provided string, removing any extension previously present
     *
     * @param fileName  name of your file (with any extension)
     * @param extension wanted extension
     */
    public static @NotNull String addExtension(String fileName, String extension) {
        return firstFromSplit(fileName, "\\.") + extension;
    }

    public static String removeLastFromSplit(String string, String regex){
        return string.replace(lastFromSplit(string, regex), "");
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
     * Returns the string between the two provided strings
     *
     * @param string starting string
     * @param open   string before the target
     * @param close  string after the target
     */
    public static String between(String string, String open, String close) {
        int start = string.indexOf(open);
        if (start != -1) {
            int end = string.indexOf(close, start + open.length());
            if (end != -1) {
                return string.substring(start + open.length(), end);
            }
        }

        return "";
    }
}
