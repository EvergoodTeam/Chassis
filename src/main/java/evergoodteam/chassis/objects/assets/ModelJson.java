package evergoodteam.chassis.objects.assets;

import com.google.gson.JsonObject;

public class ModelJson {

    /**
     * <p> Generates a {@link JsonObject} with the required information for the Item's Model </p>
     *
     * @param namespace   your ModId
     * @param type        <p> "handheld": used mostly for tools </p> <p> "generated": everything else item related </p>
     *                    <p> "block": item generated from block </p>
     * @param textureName name of your .png Texture File
     * @return
     */
    public static JsonObject createItemModelJson(String namespace, String type, String textureName) {

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
     * <p> Generates a {@link JsonObject} with the required information for the Block's Model </p>
     * <p> When dealing with Columns, have every texture with the same prefix (eg. example_block) and
     * with either the "_end" or "_side" suffix to specify top/bottom textures and side textures </p>
     * <p> (eg. example_block_side, example_block_end) </p>
     *
     * @param cubeType    <p> "all": same texture on all 6 sides </p><p> "column": uses a specific textures for top/bottom and sides </p>
     * @param textureName name of your .png Texture File
     * @return
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
