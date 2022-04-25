package evergoodteam.chassis.mixin;

import evergoodteam.chassis.objects.assets.ModelJson;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static evergoodteam.chassis.objects.assets.ModelJson.createBlockModelJson;
import static evergoodteam.chassis.util.IdentifierUtils.getIdFromIdentifier;
import static evergoodteam.chassis.util.IdentifierUtils.getTypeFromIdentifier;
import static evergoodteam.chassis.util.Reference.COLUMNS;
import static evergoodteam.chassis.util.Reference.MODEL_NAMESPACES;

@Mixin(ModelLoader.class)
public class ModelLoaderMixin {

    @Inject(method = "loadModelFromJson", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourceManager;getResource(Lnet/minecraft/util/Identifier;)Lnet/minecraft/resource/Resource;"), cancellable = true)
    public void loadModelFromJson(Identifier id, CallbackInfoReturnable<JsonUnbakedModel> cir) {

        if (MODEL_NAMESPACES.isEmpty()) return;

        for (int i = 0; i < MODEL_NAMESPACES.size(); i++) {
            if (!MODEL_NAMESPACES.get(i).equals(id.getNamespace())) return;
        }

        String modId = id.getNamespace();
        String modelJson;

        if ("block".equals(getTypeFromIdentifier(id))) {

            if (COLUMNS.stream().anyMatch(getIdFromIdentifier(id)::contains)) {

                //LOGGER.info("Found Column \"{}\"", id);
                modelJson = createBlockModelJson("column", id.toString());
            } else {

                modelJson = createBlockModelJson("all", id.toString());
            }
        } else if ("item".equals(getTypeFromIdentifier(id))) {

            modelJson = ModelJson.createItemModelJson(modId, "block", getIdFromIdentifier(id));
        } else return;


        if ("".equals(modelJson)) return;

        JsonUnbakedModel model = JsonUnbakedModel.deserialize(modelJson);
        model.id = id.toString();
        cir.setReturnValue(model);
    }
}
