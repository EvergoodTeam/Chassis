package evergoodteam.chassis.mixin;

import com.google.gson.JsonObject;
import evergoodteam.chassis.client.models.BlockModelType;
import evergoodteam.chassis.client.models.ItemModelType;
import evergoodteam.chassis.client.models.ModelBundler;
import evergoodteam.chassis.client.models.ModelType;
import evergoodteam.chassis.objects.assets.ModelJson;
import evergoodteam.chassis.util.IdentifierParser;
import evergoodteam.chassis.util.StringUtils;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Set;

import static evergoodteam.chassis.util.Reference.CMI;
import static evergoodteam.chassis.util.handlers.RegistryHandler.ITEM_TYPES;
import static evergoodteam.chassis.util.handlers.RegistryHandler.REGISTERED_BLOCKS;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @deprecated use {@link evergoodteam.chassis.objects.resourcepacks.ResourcePackBase ResourcePackBase} instead
 */
@Deprecated
@Mixin(ModelLoader.class)
public class ModelLoaderMixin {

    private static final Logger LOG = getLogger(CMI + "/M/Model");

    @Inject(method = "loadModelFromJson", at = @At("HEAD"), cancellable = true)
    public void loadModelFromJson(Identifier id, CallbackInfoReturnable<JsonUnbakedModel> cir) {

        List<ModelBundler> bundlerList = ModelBundler.getBundlerList();

        if (!bundlerList.isEmpty()) {

            String entryNamespace = id.getNamespace();
            String entryName = IdentifierParser.getLastPath(id);
            String entryPath = id.getPath();
            String entryIdentifier = IdentifierParser.getString(entryNamespace, entryName);

            for (ModelBundler bundler : bundlerList) {
                if (bundler.hasNamespace(entryNamespace)) {

                    JsonObject modelJson = null;

                    if (bundler.hasEntries()) {
                        boolean isBlock = "block".equals(IdentifierParser.getFirstParent(id));

                        ModelType type = isBlock ? bundler.getBlockType(entryIdentifier) : bundler.getItemType(entryIdentifier);

                        if (type != null) { // Fails if entry isn't inside bundler
                            if (isBlock) {
                                modelJson = ModelJson.createBlockModelJson((BlockModelType) type, entryNamespace, StringUtils.removeLastOccurrence(
                                        entryPath,
                                        new String[]{"_horizontal", "_mirrored"},
                                        "")
                                );

                            } else
                                modelJson = ModelJson.createItemModelJson((ItemModelType) type, entryNamespace, entryName);
                        }
                    } else {
                        // Give a model to every entry with the same namespace
                        // DEPRECATED, create a ModelBundler instead!
                        modelJson = getModel(bundler.getColumns(), id);
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

    @Deprecated
    private static @Nullable JsonObject getModel(Set<String> columns, Identifier identifier) {
        String entryNamespace = identifier.getNamespace();
        String entryName = IdentifierParser.getLastPath(identifier);
        String entryIdentifier = IdentifierParser.getString(entryNamespace, entryName);

        switch (IdentifierParser.getFirstParent(identifier)) {
            case "block": {

                if ("horizontal".equals(StringUtils.lastFromSplit(entryName, "_"))) {
                    for (String column : columns) {
                        if (entryName.contains(column))
                            return blockModel("column_horizontal", StringUtils.removeLastOccurrence(identifier.toString(), "_horizontal", ""));
                    }
                } else {
                    for (String column : columns) {
                        if (entryName.contains(column)) return blockModel("column", identifier.toString());
                    }
                    return blockModel("all", identifier.toString());
                }
            }

            case "item": {
                if (REGISTERED_BLOCKS.get(identifier.getNamespace()).contains(entryName)) {
                    return itemModel(entryNamespace, "block", entryName);
                } else {
                    if (ITEM_TYPES.containsKey(entryIdentifier)) {
                        return itemModel(entryNamespace, ITEM_TYPES.get(entryIdentifier).toString(), entryName);
                    }
                }
            }

            default:
                return null;
        }
    }

    private static JsonObject blockModel(String type, String input) {
        return ModelJson.createBlockModelJson(type, input);
    }

    private static JsonObject itemModel(String namespace, String type, String textureName) {
        return ModelJson.createItemModelJson(namespace, type, textureName);
    }
}
