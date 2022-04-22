package evergoodteam.chassis.objects.assets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class TagJson {

    /**
     * Create a JsonObject for a Tag File <br>
     * NOTE: each Path MUST have its own Namespace
     * @param namespaces
     * @param paths
     * @return JsonObject with the inserted info
     */
    public static JsonObject createTagJson(String[] namespaces, String[] paths){

        if(namespaces.length != paths.length) return null;

        JsonObject json = new JsonObject();

        json.addProperty("replace", false);

        JsonArray jsonArray = new JsonArray();

        for(int i = 0; i < namespaces.length; i++){
            jsonArray.add(namespaces[i] + ":" + paths[i]);
        }

        json.add("values", jsonArray);

        return json;
    }
}
