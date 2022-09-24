package evergoodteam.chassis.objects.assets;

import com.google.gson.JsonObject;
import evergoodteam.chassis.client.models.BlockModelType;
import evergoodteam.chassis.client.models.ItemModelType;
import evergoodteam.chassis.util.IdentifierParser;
import evergoodteam.chassis.util.JsonUtils;
import org.jetbrains.annotations.Nullable;

public class ModelJson {

    /**
     * Generates a {@link JsonObject} with the required information for an item model
     *
     * @param type        check {@link ItemModelType} for the available types
     * @param namespace   your modId
     * @param textureName name of your texture .png file
     */
    public static @Nullable JsonObject createItemModelJson(ItemModelType type, String namespace, String textureName) {
        return createItemModelJson(namespace, type.toString(), textureName);
    }

    /**
     * Generates a {@link JsonObject} with the required information for an item model
     *
     * @param namespace   your modId
     * @param type        "handheld": used mostly for tools <p>
     *                    "generated": every other type of item <p>
     *                    "block": item generated from a block
     * @param textureName name of your texture .png file
     * @deprecated as of release 1.2.3, replaced by {@link #createItemModelJson(ItemModelType, String, String)}
     */
    public static @Nullable JsonObject createItemModelJson(String namespace, String type, String textureName) {
        switch (type) {
            case "generated":
            case "handheld": {
                String json = """
                        {
                          "parent": "item/%s",
                          "textures": {
                            "layer0": "%s:item/%s"
                          }
                        }""".formatted(type, namespace, textureName);

                return JsonUtils.toJsonObject(json);
            }
            case "block": {
                String json = """
                        {
                          "parent": "%s:block/%s"
                        }""".formatted(namespace, textureName);

                return JsonUtils.toJsonObject(json);
            }
            default:
                return null;
        }
    }

    /**
     * Generates a {@link JsonObject} with the required information for a block model <p>
     * When dealing with columns, have every texture with the same prefix (e.g. "example_block") and
     * with the "_end"/"_side" suffix to specify the bases texture and the side texture <p>
     * e.g. "example_block_side", "example_block_end"
     *
     * @param type             check {@link BlockModelType} for the available types
     * @param textureNamespace your modId
     * @param texturePath      path to the texture .png file
     */
    public static @Nullable JsonObject createBlockModelJson(BlockModelType type, String textureNamespace, String texturePath) {
        return createBlockModelJson(type.toString(), IdentifierParser.getString(textureNamespace, texturePath));
    }

    /**
     * Generates a {@link JsonObject} with the required information for a block model <p>
     * When dealing with columns, have every texture with the same prefix (e.g. "example_block") and
     * with the "_end"/"_side" suffix to specify the bases texture and the side texture <p>
     * e.g. "example_block_side", "example_block_end"
     *
     * @param type              "all": same texture on all 6 sides <p>
     *                          "column": uses a specific texture for the bases and one for the sides <p>
     *                          "column_mirrored": sister model of a column model in a mirrored column block (e.g. deepslate)
     * @param textureIdentifier path to your texture (e.g. "yourmodid:block/ruby_ore")
     * @deprecated as of release 1.2.3, replaced by {@link #createBlockModelJson(BlockModelType, String, String)}
     */
    public static @Nullable JsonObject createBlockModelJson(String type, String textureIdentifier) {
        switch (type) {
            case "all": {
                String json = """
                        {
                          "parent": "block/cube_%s",
                          "textures": {
                            "all": "%s"
                          }
                        }""".formatted(type, textureIdentifier);

                return JsonUtils.toJsonObject(json);
            }
            case "column":
            case "column_mirrored":
            case "column_horizontal": {
                String json = """
                        {
                          "parent": "block/cube_%1$s",
                          "textures": {
                            "end": "%2$s_end",
                            "side": "%2$s_side"
                          }
                        }""".formatted(type, textureIdentifier);

                return JsonUtils.toJsonObject(json);
            }
            default:
                return null;
        }
    }
}
