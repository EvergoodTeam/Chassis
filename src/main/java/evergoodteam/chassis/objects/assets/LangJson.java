package evergoodteam.chassis.objects.assets;

import com.google.gson.JsonObject;

import java.util.Map;

public class LangJson {

    public static JsonObject createLangJson(Map<String, String> entries){

        JsonObject json = new JsonObject();

        for(int i = 0; i < entries.keySet().size(); i++){
            json.addProperty(entries.keySet().toArray()[i].toString(), entries.get(entries.keySet().toArray()[i]));
        }

        return json;
    }
}