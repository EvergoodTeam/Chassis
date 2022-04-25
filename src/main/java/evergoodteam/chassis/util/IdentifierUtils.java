package evergoodteam.chassis.util;

import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public class IdentifierUtils {

    /**
     * example_mod:block/example_block -> example_block
     *
     * @param identifier
     * @return Id of the Object from the specified Identifier
     */
    public static String getIdFromIdentifier(@NotNull Identifier identifier) {

        String result = identifier.toString().substring(identifier.toString().lastIndexOf("/") + 1);
        return result;
    }

    /**
     * example_mod:block/example_block -> block
     *
     * @param identifier
     * @return Object Type of the Object from the specified Identifier
     */
    public static String getTypeFromIdentifier(@NotNull Identifier identifier) {

        String result = StringUtils.substringBetween(identifier.toString(), ":", "/");
        return result;
    }
}
