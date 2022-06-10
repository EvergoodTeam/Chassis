package evergoodteam.chassis.objects.assets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class TagJson {

    /**
     * Create a JsonObject for a Tag File <br>
     *
     * @param namespace
     * @param paths
     * @return {@link JsonObject} with the inserted info
     */
    public static JsonObject createTagJson(String namespace, String[] paths) {

        JsonObject json = new JsonObject();

        json.addProperty("replace", false);

        JsonArray jsonArray = new JsonArray();

        for (int i = 0; i < paths.length; i++) {
            jsonArray.add(namespace + ":" + paths[i]);
        }

        json.add("values", jsonArray);

        return json;
    }
}
