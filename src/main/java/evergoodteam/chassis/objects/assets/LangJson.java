package evergoodteam.chassis.objects.assets;

import com.google.gson.JsonObject;

import java.util.Map;

public class LangJson {

    /**
     * Generates a language {@link JsonObject} from the provided map
     *
     * @param langMap map composed of translation keys (e.g. "modId.item.tooltip") with each their translation (e.g. "Cool tooltip")
     */
    public static JsonObject createLangJson(Map<String, String> langMap) {

        JsonObject json = new JsonObject();

        for (String translationKey : langMap.keySet()) {
            json.addProperty(translationKey, langMap.get(translationKey));
        }

        return json;
    }
}