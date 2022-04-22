package evergoodteam.chassis.objects.assets;

import com.google.gson.JsonObject;

public class BlockstateJson {

    public static JsonObject createBlockstateJson(String namespace, String path){

        JsonObject json = new JsonObject();

        JsonObject empty = new JsonObject();
        empty.addProperty("model", namespace+":block/"+path);

        JsonObject variants = new JsonObject();
        variants.add("", empty);

        json.add("variants", variants);

        return json;
    }
}
