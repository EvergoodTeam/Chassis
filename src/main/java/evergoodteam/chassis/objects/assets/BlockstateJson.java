package evergoodteam.chassis.objects.assets;

import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import net.minecraft.util.Identifier;

@Log4j2
public class BlockstateJson {

    public static JsonObject createBlockstateJson(String namespace, String path) {
        return createBlockstateJson(new Identifier(namespace, path));
    }

    public static JsonObject createBlockstateJson(Identifier input) {

        JsonObject json = new JsonObject();

        JsonObject empty = new JsonObject();
        empty.addProperty("model", input.getNamespace() + ":block/" + input.getPath());

        JsonObject variants = new JsonObject();
        variants.add("", empty);

        json.add("variants", variants);

        return json;
    }
}
