package evergoodteam.chassis.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;

@Log4j2
public class JsonUtils {

    /**
     * @param file Path of a valid .json File
     * @return {@link JsonObject} with all the contents of the specified .json File
     */
    public static JsonObject getJsonObject(@NotNull Path file) {

        JsonElement jsonElement = null;

        try {
            jsonElement = JsonParser.parseReader(new FileReader(file.toString()));
        } catch (FileNotFoundException e) {
            log.error(e);
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
