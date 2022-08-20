package evergoodteam.chassis.util.handlers;

import com.google.gson.JsonObject;
import evergoodteam.chassis.objects.injected.ModelBundler;
import evergoodteam.chassis.objects.injected.RecipeBase;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class InjectionHandler {

    /**
     * Generates and injects models for all your mod's entries registered through {@link RegistryHandler},
     * treating the specified paths as columns texture-wise
     *
     * @param namespace your modId
     * @param paths     path of your entry
     * @see evergoodteam.chassis.mixin.ModelLoaderMixin
     */
    public static void addModelBundler(@NotNull String namespace, String[] paths) {
        new ModelBundler(namespace.toLowerCase(), paths);
    }

    /**
     * Generates and injects models for all your mod's entries registered through {@link RegistryHandler}
     *
     * @param namespace your modId
     * @see evergoodteam.chassis.mixin.ModelLoaderMixin
     */
    public static void addModelBundler(String namespace) {
        new ModelBundler(namespace.toLowerCase());
    }

    //region Deprecated

    @Deprecated
    static List<String> defaultColumns = new ArrayList<>();

    /**
     * Specifies which of your mod's entries should be treated as columns texture-wise
     *
     * @param paths path of your entry
     * @deprecated as of release 1.2.3, replaced by {@link #addModelBundler(String, String[])}
     */
    @Deprecated
    public static void addColumnType(@NotNull String[] paths) { // TODO: check other places where String... is misused
        defaultColumns.addAll(List.of(paths));
    }

    /**
     * Generates and injects models for all your mod's entries registered through {@link RegistryHandler}
     *
     * @param namespace your modId
     * @deprecated as of release 1.2.3, replaced by {@link #addModelBundler(String)}
     */
    @Deprecated
    public static void addModelInjection(@NotNull String namespace) {
        addModelBundler(namespace, defaultColumns.toArray(new String[0]));
        defaultColumns.clear();
    }
    //endregion

    /**
     * Sends {@link evergoodteam.chassis.mixin.RecipeManagerMixin} recipes to inject
     *
     * @param namespace  namespace of your recipe (your modId)
     * @param path       name to identify your recipe from other entries in the same namespace
     * @param jsonRecipe recipe in json format
     * @see evergoodteam.chassis.objects.assets.RecipeJson
     */
    public static void addRecipe(@NotNull String namespace, @NotNull String path, JsonObject jsonRecipe) {
        new RecipeBase(namespace, path, jsonRecipe);
    }
}
