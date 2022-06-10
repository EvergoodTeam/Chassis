package evergoodteam.chassis.objects.assets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RecipeJson {

    public static JsonObject create3x3RecipeJson(String type, Identifier input, Identifier output, int outputCount) {
        return createShapedRecipeJson(Arrays.asList("xxx", "xxx", "xxx"), List.of('x'), List.of(type), List.of(input), output, outputCount);
    }

    public static JsonObject createRingRecipeJson(String type, Identifier input, Identifier output, int outputCount) {
        return createShapedRecipeJson(Arrays.asList("xxx", "x x", "xxx"), List.of('x'), List.of(type), List.of(input), output, outputCount);
    }

    public static JsonObject createShapedRecipeJson(List<String> pattern, List<Character> keys, List<String> types, Map<String, String> inputs, String outputNamespace, String outputPath, int outputCount) {

        List<Identifier> identifiers = new ArrayList<>();

        for (Map.Entry<String, String> entry : inputs.entrySet()) {
            identifiers.add(new Identifier(entry.getKey(), entry.getValue()));
        }

        return createShapedRecipeJson(pattern, keys, types, identifiers, new Identifier(outputNamespace, outputPath), outputCount);
    }

    public static JsonObject createShapedRecipeJson(List<String> pattern, List<Character> keys, List<String> types, List<String> inputs, String output, int outputCount) {

        List<Identifier> identifiers = new ArrayList<>();

        for (String entry : inputs) {
            identifiers.add(new Identifier(entry));
        }

        return createShapedRecipeJson(pattern, keys, types, identifiers, new Identifier(output), outputCount);
    }

    public static JsonObject createShapedRecipeJson(List<String> pattern, List<Character> keys, List<String> types, List<Identifier> inputs, Identifier output, int outputCount) {

        JsonObject json = new JsonObject();

        json.addProperty("type",
                "minecraft:crafting_shaped");                 //"type": "minecraft:crafting_shaped"

        JsonArray jsonArray = new JsonArray();                      //"pattern": [
        jsonArray.add(pattern.get(0));                              //  "#xx",
        jsonArray.add(pattern.get(1));                              //  "#xx",
        jsonArray.add(pattern.get(2));                              //  "###"
        json.add("pattern", jsonArray);                     //]

        JsonObject individualKey;
        JsonObject keyList = new JsonObject();                      //"key": {
        for (int i = 0; i < keys.size(); ++i) {                     //  "key1": {
            individualKey = new JsonObject();                       //    "type1": "input1"
            individualKey.addProperty(types.get(i),                 //  },
                    inputs.get(i).toString());                      //  "key2": {
            keyList.add(keys.get(i) + "", individualKey);   //    "type2": "input2"
        }                                                           //  }
        json.add("key", keyList);                           //},

        JsonObject result = new JsonObject();                       //"result": {
        result.addProperty("item", output.toString());      //  "item": "output",
        result.addProperty("count", outputCount);           //  "count": outputCount
        json.add("result", result);                         //}

        return json;
    }


    public static JsonObject createShapelessRecipeJson(String type, Identifier input, Identifier output, int outputCount) {

        JsonObject json = new JsonObject();

        json.addProperty("type",
                "minecraft:crafting_shapeless");              //"type": "minecraft:crafting_shapeless"

        JsonArray ingredients = new JsonArray();                    //"ingredients": [
        JsonObject inputIngredient = new JsonObject();              //    {
        inputIngredient.addProperty(type, input.toString());        //      "type": "input"
        ingredients.add(inputIngredient);                           //    }
        json.add("ingredients", ingredients);               //],

        JsonObject result = new JsonObject();                       //"result": {
        result.addProperty("item", output.toString());      //  "item": "output",
        result.addProperty("count", outputCount);           //  "count": outputCount
        json.add("result", result);                         //}

        return json;
    }
}
