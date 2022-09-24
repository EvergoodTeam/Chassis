package evergoodteam.chassis.util.handlers;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import evergoodteam.chassis.util.JsonUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.nio.file.Path;

import static evergoodteam.chassis.util.Reference.CMI;
import static org.slf4j.LoggerFactory.getLogger;

public class JsonHandler {

    private static final Logger LOGGER = getLogger(CMI + "/H/Json");
    private static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();

    /**
     * After translating the string to a {@link JsonObject}, writes it to the .json file identified by the path
     */
    public static void writeToJson(String json, @NotNull Path path) {
        writeToJson(JsonUtils.toJsonObject(json), path);
    }

    /**
     * Writes the provided {@link JsonObject} to the .json file identified by the path
     */
    public static void writeToJson(JsonObject jsonObject, @NotNull Path path) {
        if (JsonUtils.isJsonFile(path)) {
            //LOGGER.info("Attempting to write to path {}", path);
            if (jsonObject.size() != 0) {
                String jsonString = GSON.toJson(jsonObject);
                FileHandler.writeToFile(jsonString, path);
            } else {
                LOGGER.warn("Provided JsonObject is not valid");
            }
        }
    }
}
