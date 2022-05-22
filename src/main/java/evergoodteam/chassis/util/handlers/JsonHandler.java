package evergoodteam.chassis.util.handlers;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import evergoodteam.chassis.util.JsonUtils;
import evergoodteam.chassis.util.StringUtils;
import org.slf4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import static evergoodteam.chassis.util.Reference.MODID;
import static org.slf4j.LoggerFactory.getLogger;

public class JsonHandler {

    private static final Logger LOGGER = getLogger(MODID + "/JHandler");

    public static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();

    /**
     * After translating the String to a JsonObject, writes it to the .json File identified with the Path
     *
     * @param json
     * @param path
     */
    public static void writeToJson(String json, Path path) {

        writeToJson(JsonUtils.getJsonObject(json), path);
    }

    /**
     * Writes provided JsonObject to the .json File identified with the Path
     *
     * @param jsonObject
     * @param path
     */
    public static void writeToJson(JsonObject jsonObject, Path path) {

        Path actual = StringUtils.checkMissingExtension(path, ".json");

        //log.info("Attempting to write to path {}", actual);

        if (jsonObject.size() != 0) {

            String jsonString = GSON.toJson(jsonObject);

            try (FileWriter fileWriter = new FileWriter(actual.toFile())) {
                fileWriter.write(jsonString);
            } catch (IOException e) {
                LOGGER.error("Error on writing to Json file with path {}", path, e);
            }
        } else {
            LOGGER.warn("Provided JsonObject is not valid");
        }
    }
}
