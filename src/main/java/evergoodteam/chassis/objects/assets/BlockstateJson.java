package evergoodteam.chassis.objects.assets;

import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;

public class BlockstateJson {

    public static JsonObject createBlockstateJson(String namespace, String path) {
        return createBlockstateJson(new Identifier(namespace, path));
    }

    public static JsonObject createBlockstateJson(Identifier input) {

        JsonObject json = new JsonObject();

        JsonObject empty = new JsonObject();
        empty.addProperty("model", input.toString());

        JsonObject variants = new JsonObject();
        variants.add("", empty);

        json.add("variants", variants);

        return json;
    }
}
