package evergoodteam.chassis.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Reference {

    public static final String MODID = "chassis";

    // Used for model generation
    public static List<String> MODIDS = new ArrayList<String>();

    // Used for block/item registration
    public static List<Block> BLOCKS = new ArrayList<Block>();
    public static List<Item> ITEMS = new ArrayList<Item>();

    // Blocks with specifc texture layouts
    public static List<String> COLUMNS = new ArrayList<String>(Arrays.asList("basalt", "deepslate", "blackstone"));
}
