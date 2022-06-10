package evergoodteam.chassis.util;

import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public class IdentifierParser {

    public static String getString(String namespace, String path) {
        return namespace + ":" + path;
    }

    /**
     * example_mod:block/example_block -> example_block
     *
     * @param identifier
     * @return Id of the Object from the specified Identifier
     */
    public static String getIdFromIdentifier(@NotNull Identifier identifier) {
        return identifier.toString().substring(identifier.toString().lastIndexOf("/") + 1);
    }

    /**
     * example_mod:block/example_block -> block
     *
     * @param identifier
     * @return Object Type of the Object from the specified Identifier
     */
    public static String getTypeFromIdentifier(@NotNull Identifier identifier) {
        return StringUtils.substringBetween(identifier.toString(), ":", "/");
    }
}
