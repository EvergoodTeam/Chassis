package evergoodteam.chassis.objects.assets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class LootJson {


    public static JsonObject createBlockBreakLootJson(String output){

        JsonObject json = new JsonObject();

        json.addProperty("type", "minecraft:block");


        JsonArray pools = new JsonArray();


        JsonObject everything = new JsonObject();
        everything.addProperty("rolls", 1);



        JsonArray entries = new JsonArray();

        JsonObject type = new JsonObject();
        type.addProperty("type", "minecraft:item");
        type.addProperty("name", output);

        entries.add(type);

        everything.add("entries", entries);


        JsonArray conditions = new JsonArray();

        JsonObject condition = new JsonObject();
        condition.addProperty("condition", "minecraft:survives_explosion");
        conditions.add(condition);

        everything.add("conditions", conditions);

        pools.add(everything);

        json.add("pools", pools);

        return json;

    }

}
