package evergoodteam.chassis.datagen.providers;

import evergoodteam.chassis.common.resourcepack.ResourcePackBase;
import evergoodteam.chassis.util.StringUtils;
import evergoodteam.chassis.util.UrlUtils;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static evergoodteam.chassis.util.Reference.CMI;
import static org.slf4j.LoggerFactory.getLogger;

public class ChassisTextureProvider implements DataProvider, FabricDataGenerator.Pack.Factory<DataProvider> {

    private static final Logger LOGGER = getLogger(CMI + "/D/Texture");
    protected final FabricDataGenerator dataGenerator;
    public final ResourcePackBase resourcePack;
    private final Map<Path, String> textures = new HashMap<>();
    private String name = "Textures";

    public ChassisTextureProvider(ResourcePackBase resourcePack, String name) {
        this(resourcePack);
        this.name = name;
    }

    public ChassisTextureProvider(ResourcePackBase resourcePack) {
        this.dataGenerator = resourcePack.generator;
        this.resourcePack = resourcePack;
    }

    public static ChassisTextureProvider create(ResourcePackBase resourcePack, String name) {
        return new ChassisTextureProvider(resourcePack, name);
    }

    public static ChassisTextureProvider create(ResourcePackBase resourcePack) {
        return new ChassisTextureProvider(resourcePack);
    }

    public ChassisTextureProvider addTexture(String textureURL, boolean block, String textureName) {
        String actual = StringUtils.addExtension(textureName, ".png");
        Path blockDir = resourcePack.getRoot().textures.resolve("block");
        Path itemDir = resourcePack.getRoot().textures.resolve("item");

        textures.put(block ? blockDir.resolve(actual) : itemDir.resolve(actual), textureURL);
        return this;
    }

    public ChassisTextureProvider addTexture(String textureUrl, Path path) {
        if (UrlUtils.isImage(textureUrl)) this.textures.put(path, textureUrl);
        else LOGGER.error("Invalid URL (\"{}\") for texture \"{}\"", textureUrl, path);
        return this;
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {

        Map<Path, byte[]> mappedData = new HashMap<>();

        textures.forEach((path, url) -> {
            InputStream in;
            try {
                in = new URL(textures.get(path)).openStream();
                mappedData.put(path, in.readAllBytes());
            } catch (IOException e) {
                LOGGER.error("Error on creating a texture .png file", e);
                throw new RuntimeException(e);
            }
        });

        try {
            return AbstractResourceProvider.writeToPath(writer, mappedData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public DataProvider create(FabricDataOutput output) {
        return this;
    }
}
