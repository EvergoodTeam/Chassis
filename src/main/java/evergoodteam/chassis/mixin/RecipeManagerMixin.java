package evergoodteam.chassis.mixin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

import static evergoodteam.chassis.util.Reference.LOGGER;
import static evergoodteam.chassis.util.Reference.RECIPES;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {

    @Inject(method = "apply", at = @At("HEAD"))
    public void interceptApply(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo info) {

        if (RECIPES.isEmpty()) return;

        String namespace;
        String path;
        JsonObject json;

        // Go through every namespace
        for (int i = 0; i < RECIPES.size(); i++) {

            Map<String, JsonObject> DEEP = RECIPES.get(RECIPES.keySet().toArray()[i]);

            LOGGER.info("Checking recipes from \"{}\": found {} recipes", RECIPES.keySet().toArray()[i], DEEP.size());

            // Go through everything from that namespace
            for (int j = 0; j < DEEP.size(); j++) {

                namespace = RECIPES.keySet().toArray()[i].toString();
                path = DEEP.keySet().toArray()[j].toString();
                json = DEEP.get(DEEP.keySet().toArray()[j]);

                //LOGGER.info("Working on {} of {}: \"{}\"", (DEEP.size() - 1), namespace, path);

                if (json != null) {
                    // Path is unique, using the same path will override
                    map.put(new Identifier(namespace, path), json);
                } else LOGGER.info("Recipe on {} ( {}:{} ) has an invalid Json", (DEEP.size() - 1), namespace, path);
            }
        }
    }
}