package evergoodteam.chassis.objects.assets;

import com.google.gson.JsonObject;
import evergoodteam.chassis.util.JsonUtils;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;

import static evergoodteam.chassis.util.Reference.CMI;
import static org.slf4j.LoggerFactory.getLogger;

public class LootJson {

    private static Logger LOGGER = getLogger(CMI + "/J/Loot");

    /**
     * Generates a {@link JsonObject} with the required information for a block loot table
     * that makes such block drop a random amount of a specific item, influenced by the fortune enchantment
     *
     * @param oreNamespace  ore's namespace
     * @param orePath       name to identify the ore from other entries in the same namespace
     * @param dropNamespace drop's namespace
     * @param dropPath      name to identify the drop from other entries in the same namespace
     */
    public static JsonObject createGemOreDropLootTable(String oreNamespace, String orePath, String dropNamespace, String dropPath) {
        return createGemOreDropLootTable(new Identifier(oreNamespace, orePath), new Identifier(dropNamespace, dropPath));
    }

    /**
     * Generates a {@link JsonObject} with the required information for a block loot table
     * that makes such block drop a random amount of a specific item, influenced by the fortune enchantment
     *
     * @param ore  ore's identifier
     * @param drop drop's identifier
     */
    public static JsonObject createGemOreDropLootTable(Identifier ore, Identifier drop) {
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
                }""".formatted(ore.toString(), drop.toString());

        return JsonUtils.toJsonObject(json);
    }

    /**
     * Generates a {@link JsonObject} with the required information for a block loot table
     * that makes such block drop itself when mined
     *
     * @param namespace your modId
     * @param path      name to identify your entry from other entries in the same namespace
     */
    public static JsonObject createBlockBreakLootJson(String namespace, String path) {
        return createBlockBreakLootJson(new Identifier(namespace, path));
    }

    /**
     * Generates a {@link JsonObject} with the required information for a block loot table
     * that makes such block drop itself when mined
     *
     * @param identifier entry's identifier
     */
    public static JsonObject createBlockBreakLootJson(Identifier identifier) {
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
                }""".formatted(identifier.toString());

        return JsonUtils.toJsonObject(json);
    }
}
