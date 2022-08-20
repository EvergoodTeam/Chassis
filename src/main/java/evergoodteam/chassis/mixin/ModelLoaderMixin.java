package evergoodteam.chassis.mixin;

import com.google.gson.JsonObject;
import evergoodteam.chassis.objects.assets.ModelJson;
import evergoodteam.chassis.objects.injected.ModelBundler;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static evergoodteam.chassis.util.IdentifierParser.*;
import static evergoodteam.chassis.util.handlers.RegistryHandler.ITEM_TYPES;
import static evergoodteam.chassis.util.handlers.RegistryHandler.REGISTERED_BLOCKS;

@Mixin(ModelLoader.class)
public class ModelLoaderMixin {

    @Inject(method = "loadModelFromJson", at = @At("HEAD"), cancellable = true)
    private void loadModelFromJson(Identifier id, CallbackInfoReturnable<JsonUnbakedModel> cir) {

        List<ModelBundler> bundlerList = ModelBundler.getBundlerList();

        String entryNamespace = id.getNamespace();
        String entryPath = getIdFromIdentifier(id);

        if (!bundlerList.isEmpty()) {
            for (ModelBundler bundler : bundlerList) {

                if (bundler.getNamespace().equals(id.getNamespace())) {

                    List<String> columns = bundler.getColumns();
                    JsonObject modelJson = new JsonObject();

                    if ("block".equals(getTypeFromIdentifier(id))) {
                        if (columns.contains(entryPath)) {
                            //LOGGER.info("Found Column \"{}\"", id);
                            modelJson = ModelJson.createBlockModelJson("column", id.toString());
                        }
                        else {
                            modelJson = ModelJson.createBlockModelJson("all", id.toString());
                        }
                    }
                    else if ("item".equals(getTypeFromIdentifier(id))) {
                        if (REGISTERED_BLOCKS.get(id.getNamespace()).contains(entryPath)) {
                            modelJson = ModelJson.createItemModelJson(entryNamespace, "block", entryPath);
                        }
                        else {
                            if (ITEM_TYPES.get("generated").contains(getString(entryNamespace, entryPath))) {
                                modelJson = ModelJson.createItemModelJson(entryNamespace, "generated", entryPath);
                            }
                            else if (ITEM_TYPES.get("handheld").contains(getString(entryNamespace, entryPath))) {
                                modelJson = ModelJson.createItemModelJson(entryNamespace, "handheld", entryPath);
                            }
                        }
                    }

                    if (modelJson != null) {
                        JsonUnbakedModel model = JsonUnbakedModel.deserialize(modelJson.toString());
                        model.id = id.toString();
                        cir.setReturnValue(model);
                    }
                }
            }
        }
    }
}
