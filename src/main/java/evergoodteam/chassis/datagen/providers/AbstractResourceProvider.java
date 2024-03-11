package evergoodteam.chassis.datagen.providers;

import com.google.common.hash.Hashing;
import com.google.common.hash.HashingOutputStream;
import com.google.gson.JsonElement;
import evergoodteam.chassis.common.resourcepack.ResourcePackBase;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.util.Util;
import org.slf4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static evergoodteam.chassis.util.Reference.CMI;
import static org.slf4j.LoggerFactory.getLogger;

public abstract class AbstractResourceProvider implements DataProvider, FabricDataGenerator.Pack.Factory<DataProvider> {

    private static final Logger LOGGER = getLogger(CMI + "/D/Generic");
    private final FabricDataGenerator dataGenerator;
    private final ResourcePackBase resourcePack;
    private final Map<Path, JsonElement> json = new HashMap<>();

    public AbstractResourceProvider(ResourcePackBase resourcePack) {
        this.dataGenerator = resourcePack.generator;
        this.resourcePack = resourcePack;
    }

    public void addJsonFile(JsonElement json, Path path) {
        this.json.put(path, json);
    }

    // TODO: instead of creating objects with a useless output (because a path is forced from maps), separate the actual user friendly dataProvider from the actual dataProvider (consumer, apply?)
    @Override
    public DataProvider create(FabricDataOutput output) {
        return this;
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        return CompletableFuture.runAsync(() -> {
            for (Path path : json.keySet()) {
                DataProvider.writeToPath(writer, json.get(path), path);
            }
        });
    }

    public static CompletableFuture<?> writeToPath(DataWriter writer, Path path, byte[] data) throws IOException {
        return writeToPath(writer, Collections.singletonMap(path, data));
    }

    public static CompletableFuture<?> writeToPath(DataWriter writer, Map<Path, byte[]> mappedData) throws IOException {
        return CompletableFuture.runAsync(() -> {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            mappedData.forEach(((path, data) -> {
                HashingOutputStream hashingOutputStream = new HashingOutputStream(Hashing.sha256(), byteArrayOutputStream);
                try {
                    writer.write(path, data, hashingOutputStream.hash());
                } catch (IOException e) {
                    LOGGER.error("Failed to save file to {}: {}", path, e);
                    throw new RuntimeException(e);
                }
            }));
        }, Util.getMainWorkerExecutor());
    }
}
