package evergoodteam.chassis.datagen.providers;

import com.google.common.hash.Hashing;
import com.google.common.hash.HashingOutputStream;
import com.google.gson.JsonElement;
import evergoodteam.chassis.objects.resourcepacks.ResourcePackBase;
import lombok.extern.log4j.Log4j2;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import org.slf4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static evergoodteam.chassis.util.Reference.CMI;
import static org.slf4j.LoggerFactory.getLogger;

@Log4j2
public class ChassisGenericProvider implements DataProvider {

    private static final Logger LOGGER = getLogger(CMI + "/D/Generic");
    private final FabricDataGenerator dataGenerator;
    private final ResourcePackBase resourcePack;
    private final Map<Path, JsonElement> json = new HashMap<>();
    //private final Map<Path, String> textures = new HashMap<>();

    public static ChassisGenericProvider create(ResourcePackBase resourcePack) {
        return new ChassisGenericProvider(resourcePack);
    }

    public ChassisGenericProvider(ResourcePackBase resourcePack) {
        this.dataGenerator = resourcePack.generator;
        this.resourcePack = resourcePack;
    }

    public ChassisGenericProvider addAsset(JsonElement json, Path path) {
        this.json.put(path, json);
        return this;
    }

    @Override
    public void run(DataWriter writer) throws IOException {
        for (Path path : json.keySet()) {
            try {
                DataProvider.writeToPath(writer, json.get(path), path);
            } catch (IOException iOException) {
                LOGGER.error("Couldn't save asset to {}: {}", path, iOException);
            }
        }
    }

    @Override
    public String getName() {
        return "Generic Assets";
    }

    /**
     * Writes provided data to the specified path
     */
    public static void writeToPath(DataWriter writer, Path path, byte[] data) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        HashingOutputStream hashingOutputStream = new HashingOutputStream(Hashing.sha1(), byteArrayOutputStream);
        writer.write(path, data, hashingOutputStream.hash());
    }
}
