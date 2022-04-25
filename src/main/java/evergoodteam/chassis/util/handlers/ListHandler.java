package evergoodteam.chassis.util.handlers;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static evergoodteam.chassis.util.Reference.*;

public class ListHandler {

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
    public static void addAssetInjection(@NotNull String namespace) {

        MODEL_NAMESPACES.add(namespace.toLowerCase());
    }

    /**
     * Sends {@link evergoodteam.chassis.mixin.LootManagerMixin} Loot Tables to inject
     * @see evergoodteam.chassis.objects.assets.LootJson
     *
     * @param namespace namespace of your loot table (your ModId)
     * @param path name to identify your loot table from other entries in the same namespace
     * @param jsonLoot {@link JsonObject} formatted for loot tables
     */
    public static void addLoot(@NotNull String namespace, @NotNull String path, JsonObject jsonLoot) {

        LOOTTABLE.put(namespace.toLowerCase(), path.toLowerCase(), jsonLoot); // Identifiers don't like uppercase
    }

    /**
     * Sends {@link evergoodteam.chassis.mixin.RecipeManagerMixin} Recipes to inject
     * @see evergoodteam.chassis.objects.assets.RecipeJson
     *
     * @param namespace namespace of your recipe (your ModId)
     * @param path name to identify your recipe from other entries in the same namespace
     * @param jsonRecipe {@link JsonObject} formatted for recipes
     */
    public static void addRecipe(@NotNull String namespace, @NotNull String path, JsonObject jsonRecipe) {

        RECIPESTABLE.put(namespace.toLowerCase(), path.toLowerCase(), jsonRecipe);
    }
}
