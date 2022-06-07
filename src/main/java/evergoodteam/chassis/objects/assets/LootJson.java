package evergoodteam.chassis.objects.assets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import net.minecraft.util.Identifier;

@Log4j2
public class LootJson {

    public static JsonObject createBlockBreakLootJson(String namespace, String path) {
        return createBlockBreakLootJson(new Identifier(namespace, path));
    }

    public static JsonObject createBlockBreakLootJson(Identifier input) {

        JsonObject json = new JsonObject();

        json.addProperty("type", "minecraft:block");

        JsonArray pools = new JsonArray();
        JsonObject poolContents = new JsonObject();

        poolContents.addProperty("rolls", 1);
        poolContents.addProperty("bonus_rolls", 0.0);

        JsonArray entries = new JsonArray();
        JsonObject entriesContents = new JsonObject();
        entriesContents.addProperty("type", "minecraft:item");
        entriesContents.addProperty("name", input.toString());
        entries.add(entriesContents);

        poolContents.add("entries", entries);

        JsonArray conditions = new JsonArray();
        JsonObject conditionContents = new JsonObject();
        conditionContents.addProperty("condition", "minecraft:survives_explosion");
        conditions.add(conditionContents);

        poolContents.add("conditions", conditions);

        pools.add(poolContents);

        json.add("pools", pools);

        log.info(json);

        return json;
    }
}
