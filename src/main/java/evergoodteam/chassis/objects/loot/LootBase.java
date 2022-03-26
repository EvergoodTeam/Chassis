package evergoodteam.chassis.objects.loot;

import com.google.gson.JsonObject;

import static evergoodteam.chassis.util.Reference.LOOTTABLE;

public class LootBase {

    // Identifiers don't like uppercase
    public static void addLoot(String namespace, String path, JsonObject jsonRecipe){

        LOOTTABLE.put(namespace.toLowerCase(), path.toLowerCase(), jsonRecipe);
    }
}
