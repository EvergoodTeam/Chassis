package evergoodteam.chassis.objects.recipes;

import com.google.gson.JsonObject;

import static evergoodteam.chassis.util.Reference.RECIPESTABLE;

public class RecipeBase {

    // Identifiers don't like uppercase
    public static void addRecipe(String namespace, String path, JsonObject jsonRecipe){

        RECIPESTABLE.put(namespace.toLowerCase(), path.toLowerCase(), jsonRecipe);
    }

    /*
    public static Table<String, String, JsonObject> getRecipes(){

        return RECIPESTABLE;
    }
    */

}
