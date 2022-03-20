package evergoodteam.chassis.mixin;

import evergoodteam.chassis.objects.assets.JsonAssets;

import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static evergoodteam.chassis.objects.assets.JsonAssets.makeBlockModelJson;
import static evergoodteam.chassis.util.GetInfo.getIdFromIdentifier;
import static evergoodteam.chassis.util.GetInfo.getTypeFromIdentifier;
import static evergoodteam.chassis.util.Reference.COLUMNS;
import static evergoodteam.chassis.util.Reference.MODIDS;

@Mixin(ModelLoader.class)
public class ModelLoaderMixin {

    @Inject(method = "loadModelFromJson", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourceManager;getResource(Lnet/minecraft/util/Identifier;)Lnet/minecraft/resource/Resource;"), cancellable = true)
    public void loadModelFromJson(Identifier id, CallbackInfoReturnable<JsonUnbakedModel> cir) {

        if(MODIDS.isEmpty()) return;

        // Inject into selected mod
        for(int i = 0; i< MODIDS.size(); i++){
            if(!MODIDS.get(i).equals(id.getNamespace())) return;
        }

        String modId = id.getNamespace();
        String modelJson;

        if("block".equals(getTypeFromIdentifier(id))){

            if(COLUMNS.stream().anyMatch(getIdFromIdentifier(id)::contains)){

                System.out.println("Found column " + id);

                modelJson = makeBlockModelJson("column", id.toString());
            }
            else{

                modelJson = makeBlockModelJson("all", id.toString());
            }

        }

        else if("item".equals(getTypeFromIdentifier(id))){

            modelJson = JsonAssets.makeItemModelJson(modId, "block", getIdFromIdentifier(id));
        }

        else return;

        // Check if valid
        if ("".equals(modelJson)) return;

        JsonUnbakedModel model = JsonUnbakedModel.deserialize(modelJson);
        model.id = id.toString();
        cir.setReturnValue(model);
    }
}
