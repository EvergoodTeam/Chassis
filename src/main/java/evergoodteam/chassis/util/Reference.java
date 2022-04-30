package evergoodteam.chassis.util;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.gson.JsonObject;
import evergoodteam.chassis.configs.ConfigBase;
import evergoodteam.chassis.objects.resourcepacks.ResourcePackBase;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Reference {

    public static final String MODID = "chassis";
    public static final Logger LOGGER = LogManager.getLogger(StringUtils.capitalize(MODID));

    //public static final ConfigBase CHASSIS_CONFIGS = new ConfigBase(MODID);
    //public static final ResourcePackBase CHASSIS_RESOURCES = new ResourcePackBase(CHASSIS_CONFIGS, MODID, "https://evergoodteam.github.io/utils/icons/chassisIcon.png", "53a074");

    //public static final ConfigBase TEST_CONFIGS = new ConfigBase("test");
    //public static final ResourcePackBase TEST_RESOURCES = new ResourcePackBase(TEST_CONFIGS, "test");

    public static final List<String> MODEL_INJECTION = new ArrayList<>(); // Used for model generation
    public static final List<String> COLUMNS = new ArrayList<>(); // Blocks with specifc texture layouts (eg. Basalt)

    // Use Table to easily add elements; use Map to "easily" access them
    public static final Table<String, String, JsonObject> RECIPESTABLE = HashBasedTable.create();
    public static final Table<String, String, JsonObject> LOOTTABLE = HashBasedTable.create();

    // Map inside Map -> {Test={son=something, son2=something2}, Test2={son=something}}
    public static Map<String, Map<String, JsonObject>> RECIPES = RECIPESTABLE.rowMap();
    public static Map<String, Map<String, JsonObject>> LOOT = LOOTTABLE.rowMap();
}
