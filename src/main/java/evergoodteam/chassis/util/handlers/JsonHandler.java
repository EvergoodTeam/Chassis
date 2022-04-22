package evergoodteam.chassis.util.handlers;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import evergoodteam.chassis.util.JsonUtils;
import lombok.extern.log4j.Log4j2;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static evergoodteam.chassis.util.Reference.LOGGER;

@Log4j2
public class JsonHandler {

    public static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();

    /**
     * Writes provided JsonObject to the .json File identified with the Path
     * @param jsonObject
     * @param path
     */
    public static void writeToJson(JsonObject jsonObject, Path path){

        Path actual = path;

        if(!path.toString().contains(".json")){
            actual = Paths.get(path + ".json"); // TODO: [NU] Unify finding
        }

        //LOGGER.info("Attempting to write to path {}", actual);

        if(jsonObject.size() != 0){

            String jsonString = GSON.toJson(jsonObject);

            try (FileWriter fileWriter = new FileWriter(actual.toFile())) {

                fileWriter.write(jsonString);
            }
            catch (IOException e) {
                log.error("Error on writing to Json file: {}", e);
            }
        }

        else{
            log.warn("Provided JsonObject is not valid");
        }
    }

    /**
     * After translating the String to a JsonObject, writes it to the .json File identified with the Path
     * @param json
     * @param path
     */
    public static void writeToJson(String json, Path path){

        writeToJson(JsonUtils.getJsonObject(json), path);
    }
}
