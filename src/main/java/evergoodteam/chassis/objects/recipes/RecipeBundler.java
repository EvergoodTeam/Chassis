package evergoodteam.chassis.objects.recipes;

import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

/**
 * @deprecated use {@link evergoodteam.chassis.objects.resourcepacks.ResourcePackBase ResourcePackBase} instead
 */
@Deprecated
public class RecipeBundler {

    private String namespace;
    private List<RecipeBase> recipeList;

    public RecipeBundler(String namespace) {
        this.namespace = namespace;
        this.recipeList = new ArrayList<>();
    }

    public RecipeBundler addRecipe(String namespace, String path, JsonObject recipeJson) {
        this.recipeList.add(new RecipeBase(namespace, path, recipeJson));
        return this;
    }

    public RecipeBundler addRecipe(Identifier identifier, JsonObject recipeJson) {
        this.recipeList.add(new RecipeBase(identifier, recipeJson));
        return this;
    }

    public List<RecipeBase> getRecipeList() {
        return this.recipeList;
    }
}
