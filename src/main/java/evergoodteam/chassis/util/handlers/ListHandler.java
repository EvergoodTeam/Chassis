package evergoodteam.chassis.util.handlers;

import com.google.gson.JsonObject;

import java.util.Arrays;

import static evergoodteam.chassis.util.Reference.*;

public class ListHandler {

    /**
     * Specify to {@link evergoodteam.chassis.mixin.ModelLoaderMixin} which Blocks need the column texture layout
     * @param type eg. basalt
     */
    public static void addColumnType(String type){

        COLUMNS.addAll(Arrays.asList(type));
    }

    /**
     * Add more than one column type at once
     * @param types Array of columns (eg. basalt, blackstone)
     */
    public static void addColumnType(String[] types){

        COLUMNS.addAll(Arrays.stream(types).toList());
    }

    /**
     * Lets {@link evergoodteam.chassis.mixin.ModelLoaderMixin} know to generate/inject Models for your Mod's Blocks
     * @param namespace Your ModId
     */
    public static void addAssetInjection(String namespace){

        MODEL_NAMESPACES.add(namespace.toLowerCase());
    }

    /**
     * Lets {@link evergoodteam.chassis.mixin.LootManagerMixin} know to generate/inject Loot Tables for the specified Mod
     * @param namespace
     * @param path
     * @param jsonLoot {@link evergoodteam.chassis.objects.assets.LootJson}
     */
    public static void addLoot(String namespace, String path, JsonObject jsonLoot){

        LOOTTABLE.put(namespace.toLowerCase(), path.toLowerCase(), jsonLoot); // Identifiers don't like uppercase
    }

    /**
     * Lets {@link evergoodteam.chassis.mixin.LootManagerMixin} know to generate/inject Recipes for the specified Mod
     * @param namespace
     * @param path
     * @param jsonRecipe {@link evergoodteam.chassis.objects.assets.RecipeJson}
     */
    public static void addRecipe(String namespace, String path, JsonObject jsonRecipe){

        RECIPESTABLE.put(namespace.toLowerCase(), path.toLowerCase(), jsonRecipe);
    }
}
