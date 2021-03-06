package evergoodteam.chassis.mixin;

import com.google.gson.JsonObject;
import evergoodteam.chassis.objects.assets.ModelJson;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static evergoodteam.chassis.objects.assets.ModelJson.createBlockModelJson;
import static evergoodteam.chassis.util.IdentifierParser.*;
import static evergoodteam.chassis.util.Reference.COLUMNS;
import static evergoodteam.chassis.util.Reference.MODEL_INJECTION;
import static evergoodteam.chassis.util.handlers.RegistryHandler.ITEM_TYPES;
import static evergoodteam.chassis.util.handlers.RegistryHandler.REGISTERED_BLOCKS;

@Mixin(ModelLoader.class)
public class ModelLoaderMixin {

    @Inject(method = "loadModelFromJson", at = @At("HEAD"), cancellable = true)
    private void loadModelFromJson(Identifier id, CallbackInfoReturnable<JsonUnbakedModel> cir) {

        if (MODEL_INJECTION.isEmpty() || !MODEL_INJECTION.contains(id.getNamespace())) return;

        String entryNamespace = id.getNamespace();
        String entryPath = getIdFromIdentifier(id);
        JsonObject modelJson = new JsonObject();

        if ("block".equals(getTypeFromIdentifier(id))) {

            if (COLUMNS.stream().anyMatch(entryPath::contains)) {
                //LOGGER.info("Found Column \"{}\"", id);
                modelJson = createBlockModelJson("column", id.toString());
            } else {
                modelJson = createBlockModelJson("all", id.toString());
            }
        } else if ("item".equals(getTypeFromIdentifier(id))) {

            if (REGISTERED_BLOCKS.get(id.getNamespace()).contains(entryPath)) {
                modelJson = ModelJson.createItemModelJson(entryNamespace, "block", entryPath);
            } else {
                if (ITEM_TYPES.get("generated").contains(getString(entryNamespace, entryPath))) {
                    modelJson = ModelJson.createItemModelJson(entryNamespace, "generated", entryPath);
                } else if (ITEM_TYPES.get("handheld").contains(getString(entryNamespace, entryPath))) {
                    modelJson = ModelJson.createItemModelJson(entryNamespace, "handheld", entryPath);
                }
            }
        } else return;

        if (modelJson == null) return;

        JsonUnbakedModel model = JsonUnbakedModel.deserialize(modelJson.toString());
        model.id = id.toString();
        cir.setReturnValue(model);
    }
}
