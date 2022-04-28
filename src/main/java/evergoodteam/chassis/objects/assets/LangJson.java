package evergoodteam.chassis.objects.assets;

import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LangJson {

    public static JsonObject createLangJson(Map<String, String> entries) {

        List<Identifier> identifiers = new ArrayList<>();

        for (Map.Entry<String, String> entry : entries.entrySet()) {
            identifiers.add(new Identifier(entry.getKey(), entry.getValue()));
        }

        return createLangJson(identifiers);
    }

    public static JsonObject createLangJson(List<Identifier> entries) {

        JsonObject json = new JsonObject();

        for (Identifier entry : entries) {
            json.addProperty(entry.getNamespace(), entry.getPath());
        }

        return json;
    }
}
