package evergoodteam.chassis.util;

import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;

public class GetInfo {


    // example_mod:block/example_block -> example_block
    public static String getIdFromIdentifier(Identifier id){

        String result = id.toString().substring(id.toString().indexOf("/") + 1);

        return result;
    }

    // example_mod:block/before_example_block -> block
    public static String getTypeFromIdentifier(Identifier id){

        String result = StringUtils.substringBetween(id.toString(), ":", "/");

        return result;
    }

    /*
    // modid:path
    public static Identifier getIdentifier(String modid, String path){

        Identifier result = new Identifier(modid, path);
        return result;
    }*/

}
