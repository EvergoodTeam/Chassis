package evergoodteam.chassis.objects.assets;

import com.google.gson.JsonObject;

import java.util.Map;

public class LangJson {

    /**
     * Creates a language json object from the provided map <p>
     *
     * @param entryMap map composed of translation keys (eg. "modId.item.tooltip") with each their translation (eg. "Cool tooltip")
     */
    public static JsonObject createLangJson(Map<String, String> entryMap) {

        JsonObject json = new JsonObject();

        for (int i = 0; i < entryMap.keySet().size(); i++) {
            json.addProperty(entryMap.keySet().toArray()[i].toString(), entryMap.get(entryMap.keySet().toArray()[i]));
        }

        return json;
    }
}