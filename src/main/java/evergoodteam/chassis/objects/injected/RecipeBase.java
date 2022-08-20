package evergoodteam.chassis.objects.injected;

import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class RecipeBase {

    private static final List<RecipeBase> RECIPE_LIST = new ArrayList<>();

    private String namespace;
    private String path;
    private JsonObject recipeJson;
    private Identifier identifier;

    public RecipeBase(List<RecipeBase> recipeList, String namespace, String path, JsonObject recipeJson){
        this(namespace, path, recipeJson);
        recipeList.add(this);
    }

    /**
     * Recipe identifiable with namespace and path, linked to a recipe Json
     * @param namespace
     * @param path
     * @param recipeJson
     */
    public RecipeBase(String namespace, String path, JsonObject recipeJson) {
        this.namespace = namespace;
        this.path = path;
        this.recipeJson = recipeJson;
        this.identifier = new Identifier(namespace, path);

        RECIPE_LIST.add(this);
    }

    public String getNamespace(){
        return this.namespace;
    }

    public String getPath(){
        return this.path;
    }

    public JsonObject getRecipe(){
        return this.recipeJson;
    }

    public Identifier getIdentifier(){
        return this.identifier;
    }

    public static List<RecipeBase> getRecipeList(){
        return RECIPE_LIST;
    }
}
