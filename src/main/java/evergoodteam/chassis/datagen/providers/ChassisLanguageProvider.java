package evergoodteam.chassis.datagen.providers;

import com.google.gson.JsonElement;
import evergoodteam.chassis.objects.assets.LangJson;
import evergoodteam.chassis.objects.resourcepacks.ResourcePackBase;
import evergoodteam.chassis.util.StringUtils;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static evergoodteam.chassis.util.Reference.CMI;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Temporary provider, FabricLanguageProvider available from 1.19.2
 */
public class ChassisLanguageProvider implements DataProvider {

    private static final Logger LOGGER = getLogger(CMI + "/D/Generic");
    protected final FabricDataGenerator dataGenerator;
    public final ResourcePackBase resourcePack;
    private final Map<Path, JsonElement> assets = new HashMap<>();

    public static ChassisLanguageProvider create(ResourcePackBase resourcePack) {
        return new ChassisLanguageProvider(resourcePack);
    }

    public ChassisLanguageProvider(ResourcePackBase resourcePack) {
        this.dataGenerator = resourcePack.generator;
        this.resourcePack = resourcePack;
    }

    public ChassisLanguageProvider addLang(String language, Map<String, String> mappedEntries) {
        this.assets.put(resourcePack.getRoot().assetsNamespace.resolve("lang")
                .resolve(StringUtils.addExtension(language, ".json")), LangJson.createLangJson(mappedEntries));
        return this;
    }

    public ChassisLanguageProvider addLang(Map<String, String> mappedEntries, Path path) {
        this.assets.put(path, LangJson.createLangJson(mappedEntries));
        return this;
    }

    @Override
    public void run(DataWriter writer) throws IOException {
        for (Path path : assets.keySet()) {
            try {
                DataProvider.writeToPath(writer, assets.get(path), path);
            } catch (IOException iOException) {
                LOGGER.error("Couldn't save asset to {}: {}", path, iOException);
            }
        }
    }

    @Override
    public String getName() {
        return "Language";
    }
}
