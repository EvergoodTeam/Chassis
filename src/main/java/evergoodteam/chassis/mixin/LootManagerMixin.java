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

        // Go through every namespace
        for(int i = 0; i < LOOT.size(); i++){

            Map <String, JsonObject> DEEP = LOOT.get(LOOT.keySet().toArray()[i]);

            LOGGER.info("Checking loot tables from \"{}\": found {} loot tables", LOOT.keySet().toArray()[i], DEEP.size());

            // Go through everything from that namespace
            for(int j = 0; j < DEEP.size(); j++){

                namespace = LOOT.keySet().toArray()[i].toString();
                path = DEEP.keySet().toArray()[j].toString();
                json = DEEP.get(DEEP.keySet().toArray()[j]);

                //LOGGER.info("Working on {} of {}: \"{}\"", (DEEP.size() - 1), namespace, path);

                if(json != null){
                    // Path is unique, using the same path will override
                    objectMap.put(new Identifier(namespace, path), json);
                }
            }
        }
    }
}
