package evergoodteam.chassis.objects.assets;

import com.google.gson.JsonObject;
import evergoodteam.chassis.util.JsonUtils;
import net.minecraft.util.Identifier;

public class BlockstateJson {

    /**
     * Creates a blockstate for a mirrored column (e.g. deepslate) <p>
     * NOTE: requires 2 models (column and column_mirrored)
     *
     * @param namespace your modId
     * @param path      name to identify your block from other entries in the same namespace
     */
    public static JsonObject createMirroredColumnBlockstateJson(String namespace, String path) {
        return createMirroredColumnBlockstateJson(new Identifier(namespace, path));
    }

    /**
     * Creates a blockstate for a mirrored column (e.g. deepslate) <p>
     * NOTE: requires 2 models (column and column_mirrored)
     *
     * @param identifier id of your block
     */
    public static JsonObject createMirroredColumnBlockstateJson(Identifier identifier) {
        String json = """
                {
                  "variants": {
                    "axis=x": [
                      {
                        "model": "%1$s:block/%2$s",
                        "x": 90,
                        "y": 90
                      },
                      {
                        "model": "%1$s:block/%2$s_mirrored",
                        "x": 90,
                        "y": 90
                      },
                      {
                        "model": "%1$s:block/%2$s",
                        "x": 90,
                        "y": 90
                      },
                      {
                        "model": "%1$s:block/%2$s_mirrored",
                        "x": 90,
                        "y": 90
                      }
                    ],
                    "axis=y": [
                      {
                        "model": "%1$s:block/%2$s"
                      },
                      {
                        "model": "%1$s:block/%2$s_mirrored"
                      },
                      {
                        "model": "%1$s:block/%2$s",
                        "y": 180
                      },
                      {
                        "model": "%1$s:block/%2$s_mirrored",
                        "y": 180
                      }
                    ],
                    "axis=z": [
                      {
                        "model": "%1$s:block/%2$s",
                        "x": 90
                      },
                      {
                        "model": "%1$s:block/%2$s_mirrored",
                        "x": 90
                      },
                      {
                        "model": "%1$s:block/%2$s",
                        "x": 90,
                        "y": 180
                      },
                      {
                        "model": "%1$s:block/%2$s_mirrored",
                        "x": 90,
                        "y": 180
                      }
                    ]
                  }
                }""".formatted(identifier.getNamespace(), identifier.getPath());

        return JsonUtils.toJsonObject(json);
    }

    /**
     * Creates a blockstate for a column (e.g. quartz pillar) <p>
     * NOTE: requires 2 models (column and column_horizontal)
     *
     * @param namespace your modId
     * @param path      name to identify your block from other entries in the same namespace
     */
    public static JsonObject createColumnBlockstateJson(String namespace, String path) {
        return createColumnBlockstateJson(new Identifier(namespace, path));
    }

    /**
     * Creates a blockstate for a column (e.g. quartz pillar) <p>
     * NOTE: requires 2 models (column and column_horizontal)
     *
     * @param identifier id of your block
     */
    public static JsonObject createColumnBlockstateJson(Identifier identifier) {
        String json = """
                {
                  "variants": {
                    "axis=x": {
                      "model": "%1$s:block/%2$s_horizontal",
                      "x": 90,
                      "y": 90
                    },
                    "axis=y": {
                      "model": "%1$s:block/%2$s"
                    },
                    "axis=z": {
                      "model": "%1$s:block/%2$s_horizontal",
                      "x": 90
                    }
                  }
                }""".formatted(identifier.getNamespace(), identifier.getPath());

        return JsonUtils.toJsonObject(json);
    }

    /**
     * Creates a basic blockstate for a block
     *
     * @param namespace your modId
     * @param path      name to identify your block from other entries in the same namespace
     */
    public static JsonObject createBlockstateJson(String namespace, String path) {
        return createBlockstateJson(new Identifier(namespace, path));
    }

    /**
     * Creates a basic blockstate for a block
     *
     * @param identifier id of your block
     */
    public static JsonObject createBlockstateJson(Identifier identifier) {
        String json = """
                {
                  "variants": {
                    "": {
                      "model": "%s:block/%s"
                    }
                  }
                }""".formatted(identifier.getNamespace(), identifier.getPath());

        return JsonUtils.toJsonObject(json);
    }
}