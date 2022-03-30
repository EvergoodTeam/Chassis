package evergoodteam.chassis.mixin;

import com.google.gson.JsonObject;
import net.minecraft.loot.LootManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

import static evergoodteam.chassis.util.Reference.*;

@Mixin(LootManager.class)
public class LootManagerMixin {

    @Inject(method = "apply", at = @At("HEAD"))
    private void interceptApply(Map<Identifier, JsonObject> objectMap, ResourceManager manager, Profiler profiler, CallbackInfo info) {

        if(LOOT.isEmpty()) return;

        String namespace;
        String path;
        JsonObject json;

        // First we look at how many mods we have to go through
        for(int i=0; i<LOOT.size(); i++){

            Map <String, JsonObject> DEEP = LOOT.get(LOOT.keySet().toArray()[i]);

            LOGGER.info("Checking loot tables from \"" + RECIPES.keySet().toArray()[i] + "\": found " + DEEP.size() + " loot tables");


            // Now we go through everything from that mod
            for(int j=0; j<DEEP.size(); j++){

                namespace = LOOT.keySet().toArray()[i].toString();
                path = DEEP.keySet().toArray()[j].toString();
                json = DEEP.get(DEEP.keySet().toArray()[j]);

                //LOGGER.info("Working on " + j + " of " + (DEEP.size() - 1) + ": \"" + namespace + ":" + path + "\"");

                // Check
                if(json != null){

                    // Path is unique, having the same path will override previous
                    objectMap.put(new Identifier(namespace, path), json);
                    //LOGGER.info(objectMap);
                    //LOGGER.info("Success");

                }
            }
        }
    }
}
