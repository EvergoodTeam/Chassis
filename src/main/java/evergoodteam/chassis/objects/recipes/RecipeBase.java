package evergoodteam.chassis.objects.recipes;

import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static evergoodteam.chassis.util.Reference.RECIPES;

public class RecipeBase {

    public static void declareModForRecipe(Identifier id){

        RECIPES.put(id, new ArrayList()); // This way we can add without replacing to the recipe list
    }

    public static void declareModForRecipe(String modid, String path){

        declareModForRecipe(new Identifier(modid, path));
    }

    public static void addRecipeToMod(String modid, JsonObject jsonRecipe){

        RECIPES.get("modid").add(jsonRecipe);
    }

    public static void addRecipe(String modid, String path, JsonObject jsonRecipe){

        if(RECIPES.get(modid) == null){

            declareModForRecipe(modid, path);
        }

        addRecipeToMod(modid, jsonRecipe);
    }

    public static Map<Identifier, List<JsonObject>> getRecipes(){

        return RECIPES;
    }

}
