package evergoodteam.chassis.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;

import static evergoodteam.chassis.util.Reference.CMI;
import static org.slf4j.LoggerFactory.getLogger;

// TODO: JsonElement after deprecation
public class JsonUtils {

    private static final Logger LOGGER = getLogger(CMI + "/U/Json");

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
    public static @Nullable JsonObject toJsonObject(@NotNull Path file) {
        try {
            JsonElement jsonElement = JsonParser.parseReader(new FileReader(file.toString()));
            return jsonElement.getAsJsonObject();
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
    public static JsonObject toJsonObject(@NotNull String json) {
        return JsonParser.parseString(json).getAsJsonObject();
    }

    /**
     * Translates a {@link JsonObject} to a string
     *
     * @param json valid {@link JsonObject}
     */
    public static String toString(@NotNull JsonObject json) {
        return json.toString();
    }

    /**
     * Adds the specified value if not null to the provided {@link JsonObject}
     *
     * @param parent {@link JsonObject} to add to
     * @param name   name of the property
     * @param value  value of the property
     */
    public static void addPropertyIfNotNull(JsonObject parent, String name, Identifier value) {
        if (value != null) parent.addProperty(name, value.toString());
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
