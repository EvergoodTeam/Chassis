package evergoodteam.chassis.util;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IdentifierParser {

    /**
     * Returns the namespace of an identifier string
     */
    public static String getNamespace(String identifier) {
        return StringUtils.firstFromSplit(identifier, ":");
    }

    /**
     * Returns the path of an identifier string
     */
    public static String getPath(String identifier) {
        return StringUtils.lastFromSplit(identifier, ":");
    }

    /**
     * Returns a string of the form {@code <namespace>:<path>} <p>
     * e.g. {@code minecraft:iron_ingot})
     */
    public static String getString(String namespace, String path) {
        return namespace + ":" + path;
    }

    /**
     * Extracts an entry's name from its identifier <p>
     * e.g. {@code yourmodid:block/example_block -> example_block}
     */
    public static String getNameFromIdentifier(@NotNull Identifier identifier) {
        return StringUtils.lastFromSplit(identifier.getPath(), "/");
    }

    /**
     * Extracts an entry's type from its identifier <p>
     * e.g. {@code yourmodid:block/example_block -> block}
     */
    public static @Nullable String getTypeFromIdentifier(@NotNull Identifier identifier) {
        return StringUtils.between(identifier.toString(), ":", "/");
    }
}
