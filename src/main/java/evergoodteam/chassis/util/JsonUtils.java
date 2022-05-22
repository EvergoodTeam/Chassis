package evergoodteam.chassis.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;

import static evergoodteam.chassis.util.Reference.MODID;
import static org.slf4j.LoggerFactory.getLogger;

public class JsonUtils {

    private static final Logger LOGGER = getLogger(MODID + "/Json");

    /**
     * @param file Path of a valid .json File
     * @return {@link JsonObject} with all the contents of the specified .json File
     */
    public static JsonObject getJsonObject(@NotNull Path file) {

        JsonElement jsonElement = null;

        try {
            jsonElement = JsonParser.parseReader(new FileReader(file.toString()));
        } catch (FileNotFoundException e) {
            LOGGER.error("File not found", e);
        }

        JsonObject jsonObject = jsonElement.getAsJsonObject();

        return jsonObject;
    }

    /**
     * Translates String to a JsonObject
     *
     * @param json Naturally has to be a valid Json transcript
     * @return
     */
    public static JsonObject getJsonObject(@NotNull String json) {
        return JsonParser.parseString(json).getAsJsonObject();
    }

    /**
     * Translates JsonObject to a String
     *
     * @param jsonObject Naturally has to be a valid {@link JsonObject}
     * @return
     */
    public static String getString(@NotNull JsonObject jsonObject) {
        return jsonObject.toString();
    }
}
