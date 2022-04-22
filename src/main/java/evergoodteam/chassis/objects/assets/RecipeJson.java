package evergoodteam.chassis.objects.assets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;

public class RecipeJson {

    // TODO: [NU] Namespace and path
    public static JsonObject createShapedRecipeJson(ArrayList<String> pattern, ArrayList<Character> keys, ArrayList<String> types, ArrayList<String> items, String output, int outputCount) {

        JsonObject json = new JsonObject();

        json.addProperty("type", "minecraft:crafting_shaped");
        //"type": "minecraft:crafting_shaped"


        JsonArray jsonArray = new JsonArray();
        jsonArray.add(pattern.get(0));
        jsonArray.add(pattern.get(1));
        jsonArray.add(pattern.get(2));
        json.add("pattern", jsonArray);
        //"pattern": [
        //  "#xx",
        //  "#xx",
        //  "###"
        //]


        JsonObject individualKey;
        JsonObject keyList = new JsonObject();

        for (int i = 0; i < keys.size(); ++i) {
            individualKey = new JsonObject();
            individualKey.addProperty(types.get(i), items.get(i));
            keyList.add(keys.get(i) + "", individualKey);
            //"key1": { "type1": "input1" }
            //"key2": { "type2": "input2 }
        }

        json.add("key", keyList);
        //"key": {
        //  "key1": {
        //    "type1": "input1"
        //  },
        //  "key2": {
        //    "type2": "input2"
        //  }
        //},


        JsonObject result = new JsonObject();
        result.addProperty("item", output);
        result.addProperty("count", outputCount);
        json.add("result", result);
        //"result": {
        //  "item": "output",
        //  "count": outputCount
        //}

        return json;
    }


    public static JsonObject createShapelessRecipeJson(String type, String input, String output, int outputCount) {

        JsonObject json = new JsonObject();

        json.addProperty("type", "minecraft:crafting_shapeless");
        //"type": "minecraft:crafting_shapeless"

        JsonArray ingredients = new JsonArray();
        JsonObject inputIngredient = new JsonObject();
        inputIngredient.addProperty(type, input);
        ingredients.add(inputIngredient);
        json.add("ingredients", ingredients);
        //"ingredients": [
        //    {
        //      "type": "input"
        //    }
        //],

        JsonObject result = new JsonObject();
        result.addProperty("item", output);
        result.addProperty("count", outputCount);
        json.add("result", result);
        //"result": {
        //  "item": "output",
        //  "count": outputCount
        //}

        return json;
    }


    public static JsonObject create3x3RecipeJson(String input, String type, String output, int outputCount){

        return createShapedRecipeJson(new ArrayList<String>(Arrays.asList("xxx", "xxx", "xxx")), new ArrayList<Character>(Arrays.asList('x')),  new ArrayList<String>(Arrays.asList(type)), new ArrayList<String>(Arrays.asList(input)), output, outputCount);
    }


    public static JsonObject createRingRecipeJson(String input, String type, String output, int outputCount){

        return createShapedRecipeJson(new ArrayList<String>(Arrays.asList("xxx", "x x", "xxx")), new ArrayList<Character>(Arrays.asList('x')), new ArrayList<String>(Arrays.asList(type)), new ArrayList<String>(Arrays.asList(input)), output, outputCount);
    }
}
