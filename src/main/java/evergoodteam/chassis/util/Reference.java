package evergoodteam.chassis.util;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class Reference {

    public static final String MODID = "chassis";
    public static final Logger LOGGER = LogManager.getLogger("Chassis");


    // Used for model generation
    public static List<String> MODIDS = new ArrayList<String>();


    // Used for block/item registration
    public static List<Block> BLOCKS = new ArrayList<Block>();
    public static List<Item> ITEMS = new ArrayList<Item>();

    // Blocks with specifc texture layouts
    public static List<String> COLUMNS = new ArrayList<String>();


    // Use Table to easily add elements, use Map to "easily" access them
    public static Table<String, String, JsonObject> RECIPESTABLE = HashBasedTable.create();
    // Map inside Map => {Test={son=something, son2=something2}, Test2={son=something}}
    public static Map<String, Map<String, JsonObject>> RECIPES = RECIPESTABLE.rowMap();

}
