package evergoodteam.chassis.util;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class StringUtils {

    /**
     * Wraps a string into lines with the specified length
     *
     * @param string     your string
     * @param lineLength how long a line should be
     */
    public static List<String> wrapWords(String string, int lineLength) {
        return wrapWords(string, "", lineLength);
    }

    /**
     * Wraps a string into lines with the specified length
     *
     * @param string     your string
     * @param linePrefix prefix for each line e.g. "# "
     * @param lineLength how long a line should be
     */
    public static List<String> wrapWords(String string, String linePrefix, int lineLength) {
        Iterable<String> spaceSplit = Splitter.on(" ").split(string);
        List<String> wordList = List.of(Iterables.toArray(spaceSplit, String.class));

        int letters = 0;
        int words = 0;
        for (String word : wordList) {
            letters += word.length() + 1; // Account for space
            if (letters - 1 > lineLength) break; // Without last space
            words++;
        }

        List<String> result = new ArrayList<>();
        for (int i = 0; i < wordList.size(); i++) {
            String current = linePrefix;
            for (int j = 0; j < words && i < wordList.size(); j++) {
                current = current + wordList.get(i);
                if (j != words - 1) {
                    current = current + " ";
                    i++;
                }
            }
            result.add(current);
        }

        return result;
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
    public static @Nullable String between(String string, String open, String close) {
        int start = string.indexOf(open);
        if (start != -1) {
            int end = string.indexOf(close, start + open.length());
            if (end != -1) {
                return string.substring(start + open.length(), end);
            }
        }

        return null;
    }
}
