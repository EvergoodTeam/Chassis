package evergoodteam.chassis.util.handlers;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static evergoodteam.chassis.util.Reference.*;

public class InjectionHandler {

    /**
     * Specify to {@link evergoodteam.chassis.mixin.ModelLoaderMixin} which Blocks need the column texture layout
     *
     * @param type eg. basalt
     */
    public static void addColumnType(String type) {
        COLUMNS.addAll(Arrays.asList(type));
    }

    /**
     * Add more than one column type at once
     *
     * @param types array of columns (eg. basalt, blackstone)
     */
    public static void addColumnType(String[] types) {
        COLUMNS.addAll(Arrays.stream(types).toList());
    }

    /**
     * Lets {@link evergoodteam.chassis.mixin.ModelLoaderMixin} know to generate/inject Models for your Mod's Blocks
     *
     * @param namespace your ModId
     */
    public static void addModelInjection(@NotNull String namespace) {
        MODEL_INJECTION.add(namespace.toLowerCase());
    }

    /**
     * Sends {@link evergoodteam.chassis.mixin.RecipeManagerMixin} Recipes to inject
     *
     * @param namespace  namespace of your recipe (your ModId)
     * @param path       name to identify your recipe from other entries in the same namespace
     * @param jsonRecipe {@link JsonObject} formatted for recipes
     * @see evergoodteam.chassis.objects.assets.RecipeJson
     */
    public static void addRecipe(@NotNull String namespace, @NotNull String path, JsonObject jsonRecipe) {
        RECIPESTABLE.put(namespace.toLowerCase(), path.toLowerCase(), jsonRecipe);
    }
}
