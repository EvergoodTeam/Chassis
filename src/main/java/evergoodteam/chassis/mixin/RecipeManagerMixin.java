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

        if(RECIPES.isEmpty()) return;

        String namespace;
        String path;
        JsonObject json;

        // First we look at how many mods we have to go through
        for(int i=0; i<RECIPES.size(); i++){

            Map <String, JsonObject> DEEP = RECIPES.get(RECIPES.keySet().toArray()[i]);

            LOGGER.info("Checking recipes from \"" + RECIPES.keySet().toArray()[i] + "\": found " + DEEP.size() + " recipes");

            // Now we go through everything from that mod
            for(int j=0; j<DEEP.size(); j++){

                namespace = RECIPES.keySet().toArray()[i].toString();
                path = DEEP.keySet().toArray()[j].toString();
                json = DEEP.get(DEEP.keySet().toArray()[j]);

                //LOGGER.info("Working on " + j + " of " + (DEEP.size() - 1) + ": \"" + namespace + ":" + path + "\"");

                // Check
                if(json != null){

                    // Path is unique, having the same path will override previous
                    map.put(new Identifier(namespace, path), json);

                }
            }
        }


    }

}