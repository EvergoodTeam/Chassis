package evergoodteam.chassis.objects.assets;

import com.google.gson.JsonObject;
import evergoodteam.chassis.util.IdentifierParser;
import evergoodteam.chassis.util.JsonUtils;
import net.minecraft.util.Identifier;

public class LootJson {

    /**
     * Generates a {@link JsonObject} with the required information for a block loot table
     * that makes such block drop a random amount of a specific item, influenced by the fortune enchantment
     */
    public static JsonObject createGemOreDropLootTable(String oreNamespace, String orePath, String dropNamespace, String dropPath) {
        return createGemOreDropLootTable(IdentifierParser.getString(oreNamespace, orePath), IdentifierParser.getString(dropNamespace, dropPath));
    }

    /**
     * Generates a {@link JsonObject} with the required information for a block loot table
     * that makes such block drop a random amount of a specific item, influenced by the fortune enchantment
     */
    public static JsonObject createGemOreDropLootTable(String oreIdentifier, String dropIdentifier) {
        String json = """
                {
                  "type": "minecraft:block",
                  "pools": [
                    {
                      "bonus_rolls": 0.0,
                      "entries": [
                        {
                          "type": "minecraft:alternatives",
                          "children": [
                            {
                              "type": "minecraft:item",
                              "conditions": [
                                {
                                  "condition": "minecraft:match_tool",
                                  "predicate": {
                                    "enchantments": [
                                      {
                                        "enchantment": "minecraft:silk_touch",
                                        "levels": {
                                          "min": 1
                                        }
                                      }
                                    ]
                                  }
                                }
                              ],
                              "name": "%s"
                            },
                            {
                              "type": "minecraft:item",
                              "functions": [
                                {
                                  "enchantment": "minecraft:fortune",
                                  "formula": "minecraft:ore_drops",
                                  "function": "minecraft:apply_bonus"
                                },
                                {
                                  "function": "minecraft:explosion_decay"
                                }
                              ],
                              "name": "%s"
                            }
                          ]
                        }
                      ],
                      "rolls": 1.0
                    }
                  ]
                }""".formatted(oreIdentifier, dropIdentifier);

        return JsonUtils.toJsonObject(json);
    }

    /**
     * Generates a {@link JsonObject} with the required information for a block loot table
     * that makes such block drop itself when mined
     */
    public static JsonObject createBlockBreakLootJson(Identifier identifier) {
        return createBlockBreakLootJson(identifier.toString());
    }

    /**
     * Generates a {@link JsonObject} with the required information for a block loot table
     * that makes such block drop itself when mined
     */
    public static JsonObject createBlockBreakLootJson(String namespace, String path) {
        return createBlockBreakLootJson(IdentifierParser.getString(namespace, path));
    }

    /**
     * Generates a {@link JsonObject} with the required information for a block loot table
     * that makes such block drop itself when mined
     */
    public static JsonObject createBlockBreakLootJson(String identifier) {
        String json = """
                {
                  "type": "minecraft:block",
                  "pools": [
                    {
                      "rolls": 1,
                      "bonus_rolls": 0.0,
                      "entries": [
                        {
                          "type": "minecraft:item",
                          "name": "%s"
                        }
                      ],
                      "conditions": [
                        {
                          "condition": "minecraft:survives_explosion"
                        }
                      ]
                    }
                  ]
                }""".formatted(identifier);

        return JsonUtils.toJsonObject(json);
    }
}
