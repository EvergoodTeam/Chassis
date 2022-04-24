package evergoodteam.chassis.util;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import evergoodteam.chassis.configs.ConfigBase;
import evergoodteam.chassis.objects.resourcepacks.ResourcePackBase;

public class Reference {

    public static final String MODID = "chassis";
    public static final Logger LOGGER = LogManager.getLogger(StringUtils.capitalize(MODID));

    public static final ConfigBase CHASSIS_CONFIGS = new ConfigBase(MODID);
    public static final ResourcePackBase CHASSIS_RESOURCES = new ResourcePackBase(CHASSIS_CONFIGS, MODID, "https://evergoodteam.github.io/utils/icons/pack.png");

    // Used for model generation
    public static final List<String> MODEL_NAMESPACES = new ArrayList<>();
    // Blocks with specifc texture layouts (eg. Basalt)
    public static final List<String> COLUMNS = new ArrayList<>();


    // Use Table to easily add elements; use Map to "easily" access them
    public static final Table<String, String, JsonObject> RECIPESTABLE = HashBasedTable.create();
    // Map inside Map => {Test={son=something, son2=something2}, Test2={son=something}}
    public static Map<String, Map<String, JsonObject>> RECIPES = RECIPESTABLE.rowMap();

    public static final Table<String, String, JsonObject> LOOTTABLE = HashBasedTable.create();
    public static Map<String, Map<String, JsonObject>> LOOT = LOOTTABLE.rowMap();

    public static void init(){
    }
}
