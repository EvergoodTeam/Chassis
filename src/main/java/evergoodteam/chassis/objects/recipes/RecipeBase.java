package evergoodteam.chassis.objects.recipes;

import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

@Deprecated
public class RecipeBase {

    private static final Map<String, RecipeBase> RECIPE_MAP = new HashMap<>();

    private String namespace;
    private String path;
    private JsonObject recipeJson;
    private Identifier identifier;

    /**
     * Recipe identifiable with namespace and path, linked to a recipe Json
     */
    public RecipeBase(String namespace, String path, JsonObject recipeJson) {
        this(new Identifier(namespace, path), recipeJson);
    }

    /**
     * Recipe identifiable with an Identifier, linked to a recipe Json
     */
    public RecipeBase(Identifier identifier, JsonObject recipeJson) {
        this.namespace = identifier.getNamespace();
        this.path = identifier.getPath();
        this.recipeJson = recipeJson;
        this.identifier = identifier;

        RECIPE_MAP.put(identifier.toString(), this);
    }

    public String getNamespace() {
        return this.namespace;
    }

    public String getPath() {
        return this.path;
    }

    public JsonObject getRecipe() {
        return this.recipeJson;
    }

    public Identifier getIdentifier() {
        return this.identifier;
    }

    public static Map<String, RecipeBase> getRecipeMap() {
        return RECIPE_MAP;
    }
}
