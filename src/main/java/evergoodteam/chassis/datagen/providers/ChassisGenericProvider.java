package evergoodteam.chassis.datagen.providers;

import com.google.gson.JsonElement;
import evergoodteam.chassis.common.resourcepack.ResourcePackBase;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static evergoodteam.chassis.util.Reference.CMI;
import static org.slf4j.LoggerFactory.getLogger;

public class ChassisGenericProvider extends AbstractResourceProvider {

    private static final Logger LOGGER = getLogger(CMI + "/D/Generic");
    private final FabricDataGenerator dataGenerator;
    private final ResourcePackBase resourcePack;
    private final Map<Path, JsonElement> json = new HashMap<>();

    public static ChassisGenericProvider create(ResourcePackBase resourcePack) {
        return new ChassisGenericProvider(resourcePack);
    }

    public ChassisGenericProvider(ResourcePackBase resourcePack) {
        super(resourcePack);
        this.dataGenerator = resourcePack.generator;
        this.resourcePack = resourcePack;
    }

    public void addJsonFile(JsonElement json, Path path) {
        this.json.put(path, json);
        //return this;
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        return CompletableFuture.runAsync(() -> {
            for (Path path : json.keySet()) {
                DataProvider.writeToPath(writer, json.get(path), path);
            }
        });
    }

    @Override
    public String getName() {
        return "Generic Assets";
    }
}
