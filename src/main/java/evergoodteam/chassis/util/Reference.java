package evergoodteam.chassis.util;

import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.*;

public class Reference {

    public static final String MODID = "chassis";

    // Used for model generation
    public static List<String> MODIDS = new ArrayList<String>();

    // Used for block/item registration
    public static List<Block> BLOCKS = new ArrayList<Block>();
    public static List<Item> ITEMS = new ArrayList<Item>();

    // Blocks with specifc texture layouts
    public static List<String> COLUMNS = new ArrayList<String>();

    // Associative array of mods and their recipes
    public static Map<Identifier, List<JsonObject>> RECIPES = new HashMap<Identifier, List<JsonObject>>();

}
