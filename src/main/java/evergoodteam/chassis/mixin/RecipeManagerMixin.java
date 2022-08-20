package evergoodteam.chassis.mixin;

import com.google.gson.JsonElement;
import evergoodteam.chassis.objects.injected.RecipeBase;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;


import static evergoodteam.chassis.util.Reference.CMI;
import static org.slf4j.LoggerFactory.getLogger;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {

    private static final Logger LOG = getLogger(CMI + "/Recipe");

    @Inject(method = "apply*", at = @At("HEAD"))
    public void interceptApply(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo info) {

        List<RecipeBase> recipeList = RecipeBase.getRecipeList();

        if (!recipeList.isEmpty()) {

            // TODO: missing: - countRecipes - how many entries per namespace
            for (RecipeBase recipe : recipeList) {
                if (recipe.getRecipe() != null) {
                    map.put(recipe.getIdentifier(), recipe.getRecipe());
                } else LOG.error("Recipe \"{}:{}\" has an invalid Json", recipe.getNamespace(), recipe.getPath());
            }
        }
    }
}