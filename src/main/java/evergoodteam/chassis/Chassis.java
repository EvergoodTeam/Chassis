package evergoodteam.chassis;

import evergoodteam.chassis.objects.recipes.RecipeBase;

import net.fabricmc.api.ModInitializer;
import java.util.ArrayList;
import java.util.Arrays;

import static evergoodteam.chassis.util.Reference.LOGGER;
import static evergoodteam.chassis.objects.assets.RecipeJson.createShapedRecipeJson;

public class Chassis implements ModInitializer {

    @Override
    public void onInitialize() {

        LOGGER.info("Starting up, should get a friend calling anytime now...");

        RecipeBase.addRecipe(
                "chassis", "logchest",
                createShapedRecipeJson(
                        new ArrayList<Character>(Arrays.asList('x')),
                        new ArrayList<String>(Arrays.asList("minecraft:oak_log")),
                        new ArrayList<String>(Arrays.asList("item")),
                        new ArrayList<String>(Arrays.asList("xxx", "x x", "xxx")),
                        "minecraft:chest", 4
                )
        );


        RecipeBase.addRecipe(
                "chassis", "stickchest",
                createShapedRecipeJson(
                        new ArrayList<Character>(Arrays.asList('x')),
                        new ArrayList<String>(Arrays.asList("minecraft:stick")),
                        new ArrayList<String>(Arrays.asList("item")),
                        new ArrayList<String>(Arrays.asList("xxx", "x x", "xxx")),
                        "minecraft:chest", 4
                )
        );

    }
}
