package evergoodteam.chassis.mixin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

import static evergoodteam.chassis.util.Reference.RECIPES;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {

    @Inject(method = "apply", at = @At("HEAD"))
    public void interceptApply(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo info) {


        JsonObject jsonObject;
        String jsonString;

        // First we look at how many mods we have to go through
        for(int i=0; i<RECIPES.size(); i++){

            System.out.println("Checking recipe from " + RECIPES.keySet().toArray()[i]);

            // Now we go through everything from that mod
            for(int j=0; j<RECIPES.get(RECIPES.keySet().toArray()[i]).size(); j++){

                System.out.println("Working on recipe number " + j + " out of " + RECIPES.get(RECIPES.keySet().toArray()[i]).size());

                // Check
                if(RECIPES.get(RECIPES.keySet().toArray()[i]).toArray()[j] != null){

                    // Get json associated with that mod
                    jsonString = RECIPES.get(RECIPES.keySet().toArray()[i]).toArray()[j].toString();

                    jsonObject = (JsonObject) JsonParser.parseString(jsonString);

                    map.put(new Identifier(RECIPES.keySet().toArray()[i].toString()), jsonObject);

                    // map.put(new Identifier("examplemod", "copper_pickaxe"), ExampleMod.COPPER_PICKAXE_RECIPE);
                }
            }
        }


    }

}