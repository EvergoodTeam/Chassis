package evergoodteam.chassis.util;

import net.minecraft.util.Identifier;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public class IdentifierUtils {

    /**
     * example_mod:block/example_block -> example_block
     * @param id
     * @return Id of the Object from the specified Identifier
     */
    public static String getIdFromIdentifier(@NotNull Identifier id){

        String result = id.toString().substring(id.toString().lastIndexOf("/") + 1);
        return result;
    }

    /**
     * example_mod:block/example_block -> block
     * @param id
     * @return Object Type from the specified Identifier
     */
    public static String getTypeFromIdentifier(@NotNull Identifier id){

        String result = StringUtils.substringBetween(id.toString(), ":", "/");
        return result;
    }
}
