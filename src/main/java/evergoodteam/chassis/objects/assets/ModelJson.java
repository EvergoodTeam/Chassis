package evergoodteam.chassis.objects.assets;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

public class ModelJson {

    /**
     * Generates a {@link JsonObject} with the required information for an item's model
     *
     * @param namespace   your modId
     * @param type        "handheld": used mostly for tools <p> "generated": everything else item related <p>
     *                    "block": item generated from block
     * @param textureName name of your texture
     */
    public static @Nullable JsonObject createItemModelJson(String namespace, String type, String textureName) {
        if ("generated".equals(type) || "handheld".equals(type)) {

            JsonObject json = new JsonObject();

            json.addProperty("parent", "item/" + type);

            JsonObject textures = new JsonObject();
            textures.addProperty("layer0", namespace + ":item/" + textureName);

            json.add("textures", textures);

            return json;
        } else if ("block".equals(type)) {

            JsonObject json = new JsonObject();

            json.addProperty("parent", namespace + ":block/" + textureName);

            return json;

        } else return null;
    }

    /**
     * Generates a {@link JsonObject} with the required information for a block's model <p>
     * When dealing with columns, have every texture with the same prefix (eg. "example_block") and
     * with the "_end"/"_side" suffix to specify top/bottom textures and side textures <p>
     * eg. "example_block_side", "example_block_end" <p>
     *
     * @param cubeType    "all": same texture on all 6 sides <p> "column": uses a specific textures for top/bottom and sides <p>
     * @param textureName name of your .png Texture File
     */
    public static JsonObject createBlockModelJson(String cubeType, String textureName) {
        if ("all".equals(cubeType)) {

            JsonObject json = new JsonObject();

            json.addProperty("parent", "block/cube_all");

            JsonObject textures = new JsonObject();
            textures.addProperty("all", textureName);

            json.add("textures", textures);

            return json;
        } else if ("column".equals(cubeType)) {

            JsonObject json = new JsonObject();

            json.addProperty("parent", "block/cube_column");

            JsonObject textures = new JsonObject();
            textures.addProperty("end", textureName + "_end");
            textures.addProperty("side", textureName + "_side");

            json.add("textures", textures);

            return json;
        } else return null;
    }
}
