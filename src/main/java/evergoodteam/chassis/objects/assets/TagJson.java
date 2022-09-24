package evergoodteam.chassis.objects.assets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class TagJson {

    /**
     * Generates a {@link JsonObject} with the required information for a <a href="https://fabricmc.net/wiki/tutorial:tags">tag</a>
     *
     * @param namespace your modId
     * @param paths     entries' paths from the provided namespace to be added to the tag
     */
    public static JsonObject createTagJson(String namespace, String[] paths) {

        List<Identifier> identifiers = new ArrayList<>();
        for (String path : paths) {
            identifiers.add(new Identifier(namespace, path));
        }

        return createTagJson(identifiers);
    }

    /**
     * Generates a {@link JsonObject} with the required information for a <a href="https://fabricmc.net/wiki/tutorial:tags">tag</a>
     *
     * @param identifiers map of the form {@code <namespace>:<path>}
     */
    public static JsonObject createTagJson(List<Identifier> identifiers) {
        JsonObject json = new JsonObject();

        json.addProperty("replace", false);

        JsonArray jsonArray = new JsonArray();
        for (Identifier identifier : identifiers) {
            jsonArray.add(identifier.toString());
        }

        json.add("values", jsonArray);

        return json;
    }
}
