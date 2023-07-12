package evergoodteam.chassis.util;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;

import static evergoodteam.chassis.util.Reference.CMI;
import static org.slf4j.LoggerFactory.getLogger;

public class JsonUtils {

    private static final Logger LOGGER = getLogger(CMI + "/H/Json");
    private static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();

    /**
     * After translating the string to a {@link JsonElement}, writes it to the .json file identified by the path
     */
    public static void writeToJson(String json, @NotNull Path path) {
        writeToJson(toJson(json), path);
    }

    /**
     * Writes the provided {@link JsonObject} to the .json file identified by the path
     */
    public static void writeToJson(JsonElement json, @NotNull Path path) {
        if (isJsonFile(path)) {
            //LOGGER.info("Attempting to write to path {}", path);
            if (json.getAsJsonObject().size() != 0) {
                String jsonString = GSON.toJson(json);
                FileUtils.writeToFile(jsonString, path);
            } else {
                LOGGER.warn("Provided Json is empty, cannot write it to {}", path);
            }
        }
    }

    /**
     * Checks whether the specified file is a .json file
     */
    public static boolean isJsonFile(Path path) {
        return FileUtils.ofExtension(path, ".json");
    }

    /**
     * Fetches the contents of a .json file and returns them as a JsonObject
     *
     * @param file path of a valid .json file
     * @return {@link JsonObject} with all the contents of the specified .json file
     */
    public static @Nullable JsonElement toJson(@NotNull Path file) {
        try {
            return JsonParser.parseReader(new FileReader(file.toString()));
        } catch (FileNotFoundException e) {
            LOGGER.error("File not found", e);
        }

        return null;
    }

    /**
     * Translates a string to a {@link JsonObject}
     *
     * @param json valid json transcript
     */
    public static JsonElement toJson(@NotNull String json) {
        return JsonParser.parseString(json);
    }

    /**
     * Adds the specified value if not null to the provided {@link JsonObject}
     *
     * @param parent {@link JsonObject} to add to
     * @param name   name of the property
     * @param value  value of the property
     */
    public static void addPropertyIfNotNull(JsonObject parent, String name, String value) {
        if (value != null) parent.addProperty(name, value);
    }
}
