package evergoodteam.chassis;

import evergoodteam.chassis.objects.recipes.RecipeBase;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;

import static evergoodteam.chassis.objects.assets.RecipeJson.createShapedRecipeJson;

public class Chassis implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger("Chassis");

    @Override
    public void onInitialize() {

        LOGGER.info("Starting up, should get a friend calling anytime now...");

        //RecipeBase.addRecipe("example", "path", createShapedRecipeJson(new ArrayList<Character>(Arrays.asList('x')), new ArrayList<String>(Arrays.asList("minecraft:item/stick")), new ArrayList<String>(Arrays.asList("item")), new ArrayList<String>(Arrays.asList("XXX", "XXX", "XXX")), "minecraft:block/dirt", 2));


    }
}
