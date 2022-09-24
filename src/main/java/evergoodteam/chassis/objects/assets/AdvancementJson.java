package evergoodteam.chassis.objects.assets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import evergoodteam.chassis.util.JsonUtils;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class AdvancementJson {

    /**
     * Creates an advancement
     */
    public static JsonObject createAdvancementJson(Identifier icon, @Nullable String nbt, String title, String description, Identifier background, String frame,
                                                   boolean toast, boolean announce, boolean hidden,
                                                   @Nullable Identifier parent,
                                                   String name, Identifier trigger, @Nullable Identifier[] items, @Nullable Identifier tag) {

        JsonObject json = new JsonObject();
        json.add("display", display(icon, nbt, title, description, background, frame, toast, announce, hidden));
        JsonUtils.addPropertyIfNotNull(json, "parent", parent);
        json.add("criteria", criteria(name, trigger, items, tag));

        return json;
    }

    /**
     * Creates a {@link JsonObject} with cosmetic information on the advancement
     */
    public static JsonObject display(Identifier icon, @Nullable String nbt, String title, String description, Identifier background, String frame,
                                     boolean toast, boolean announce, boolean hidden) {
        JsonObject display = new JsonObject();
        JsonObject iconJson = new JsonObject();
        iconJson.addProperty("item", icon.toString());
        JsonUtils.addPropertyIfNotNull(iconJson, "nbt", nbt);

        display.add("icon", iconJson);

        display.addProperty("title", title);
        display.addProperty("description", description);
        display.addProperty("background", background.toString());
        display.addProperty("frame", frame);

        display.addProperty("show_toast", toast);
        display.addProperty("announce_to_chat", announce);
        display.addProperty("hidden", hidden);

        return display;
    }

    /**
     * Creates a {@link JsonObject} with the conditions needed to trigger the advancement
     */
    public static JsonObject criteria(String name, Identifier trigger, @Nullable Identifier[] items, @Nullable Identifier tag) {
        JsonObject criteria = new JsonObject();
        JsonObject named = new JsonObject();
        named.addProperty("trigger", trigger.toString());

        JsonObject conditions = new JsonObject();
        JsonArray itemsFirst = new JsonArray();

        JsonObject firstObject = new JsonObject();

        if (items != null) {
            JsonArray itemsSecond = new JsonArray();
            for (Identifier identifier : items) {
                itemsSecond.add(identifier.toString());
            }

            if (!itemsSecond.isEmpty()) firstObject.add("items", itemsSecond);
        }

        JsonUtils.addPropertyIfNotNull(firstObject, "tag", tag);

        itemsFirst.add(firstObject);
        conditions.add("items", itemsFirst);
        named.add("conditions", conditions);
        criteria.add(name, named);

        return criteria;
    }
}
