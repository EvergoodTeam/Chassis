package evergoodteam.chassis.util.handlers;

import com.google.gson.JsonObject;
import evergoodteam.chassis.client.models.ModelBundler;
import evergoodteam.chassis.objects.recipes.RecipeBase;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static evergoodteam.chassis.util.Reference.MODID;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Injection for models and recipes can now be done through {@link ModelBundler} and {@link RecipeBase}
 */
@Deprecated
public class InjectionHandler {

    private static final Logger LOGGER = getLogger(MODID + "/H/Inject");

    //region Deprecated

    @Deprecated
    static List<String> defaultColumns = new ArrayList<>();

    /**
     * Specifies which of your mod's entries should be treated as columns (e.g. quartz pillar) texture-wise
     *
     * @param paths path of your column
     * @deprecated as of release 1.2.3, replaced by {@link ModelBundler}
     */
    @Deprecated
    public static void addColumnType(@NotNull String[] paths) { // TODO: check other places where String... is misused
        defaultColumns = List.of(paths);
    }

    /**
     * Generates and injects models for all your mod's entries registered through {@link RegistryHandler}
     *
     * @param namespace your modId
     * @deprecated as of release 1.2.3, replaced by {@link ModelBundler}
     */
    @Deprecated
    public static void addModelInjection(@NotNull String namespace) {
        new ModelBundler(namespace.toLowerCase()).addColumn(defaultColumns.toArray(new String[0]));
    }
    //endregion

    /**
     * Sends {@link evergoodteam.chassis.mixin.RecipeManagerMixin} recipes to inject
     *
     * @param namespace  namespace of your recipe (your modId)
     * @param path       name to identify your recipe from other entries in the same namespace
     * @param jsonRecipe recipe in json format
     * @see evergoodteam.chassis.objects.assets.RecipeJson
     * @deprecated as of release 1.2.3, replaced by {@link RecipeBase}
     */
    @Deprecated
    public static void addRecipe(@NotNull String namespace, @NotNull String path, JsonObject jsonRecipe) {
        new RecipeBase(namespace, path, jsonRecipe);
    }

    /**
     * Sends {@link evergoodteam.chassis.mixin.RecipeManagerMixin} recipes to inject
     *
     * @param identifier label for your addition, composed of a namespace and a path
     * @param jsonRecipe recipe in json format
     * @see evergoodteam.chassis.objects.assets.RecipeJson
     * @deprecated as of release 1.2.3, replaced by {@link RecipeBase}
     */
    @Deprecated
    public static void addRecipe(@NotNull Identifier identifier, JsonObject jsonRecipe) {
        new RecipeBase(identifier, jsonRecipe);
    }
}
