package evergoodteam.chassis.objects.resourcepacks;

import com.google.gson.JsonObject;
import evergoodteam.chassis.configs.ConfigBase;
import evergoodteam.chassis.configs.ConfigHandler;
import evergoodteam.chassis.objects.assets.*;
import evergoodteam.chassis.util.StringUtils;
import evergoodteam.chassis.util.UrlUtils;
import evergoodteam.chassis.util.handlers.DirHandler;
import evergoodteam.chassis.util.handlers.FileHandler;
import evergoodteam.chassis.util.handlers.JsonHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static evergoodteam.chassis.util.Reference.CMI;
import static org.slf4j.LoggerFactory.getLogger;

public class ResourcePackBase {

    private static Logger LOGGER = getLogger(CMI + "/Resource");

    public static final Map<String, List<ResourcePackBase>> RESOURCE_PACKS = new HashMap<>(); // Used for identifying RPs

    public static final List<String> HIDDEN = new ArrayList<>(); // Requires capitalized namespaces
    public static final List<String> DEFAULT_ICON = new ArrayList<>();

    public String namespace;
    public Path path;

    public String hexDescColor;

    private Path assetsDir;
    private Path dataDir;
    private Path namespaceAssetsDir;
    private Path namespaceDataDir;
    private Path blockstatesDir;
    private Path tagsDir;

    /**
     * Object from which a ResourcePack without a custom icon is generated
     *
     * @param config    {@link ConfigBase} to which assign the ResourcePack; determines the root dir
     * @param namespace name of your ResourcePack
     */
    public ResourcePackBase(ConfigBase config, String namespace) {
        this(config, namespace, null, "AAAAAA");
    }

    /**
     * Object from which a ResourcePack is generated
     *
     * @param config       {@link ConfigBase} to which assign the ResourcePack; determines the root dir
     * @param namespace    name of your ResourcePack
     * @param iconUrl      valid URL, nullable
     * @param hexDescColor hex color value used for the description text in the GUI
     */
    public ResourcePackBase(@NotNull ConfigBase config, @NotNull String namespace, @Nullable String iconUrl, @Nullable String hexDescColor) {

        this.namespace = namespace;
        this.path = Paths.get(config.dirPath.toString(), "resourcepacks/" + namespace.toLowerCase()); // Root of every ResourcePack
        this.hexDescColor = hexDescColor;

        RESOURCE_PACKS.computeIfAbsent(config.namespace, k -> new ArrayList<>()).add(this);

        configInit(config);

        if (iconUrl == null) this.useDefaultIcon();
        else createPackIcon(config, namespace, iconUrl);

        assignDirs();
    }

    /**
     * Gets the ResourcePack with the given id, which corresponds to the root dir and Config name
     * to which the ResourcePack is assigned
     *
     * @param id   name of the Configs to which the ResourcePack is assigned
     * @param name name of the ResourcePack
     * @return ResourcePack from the Config {@code id} and named {@code name}
     */
    public static ResourcePackBase getResourcePack(String id, String name) {

        List<ResourcePackBase> baseList = ResourcePackBase.RESOURCE_PACKS.get(id);

        int index = 0;

        for (ResourcePackBase pack : baseList) {
            if (name.toLowerCase().equals(pack.namespace)) {
                index = baseList.indexOf(pack);
            }
        }

        return RESOURCE_PACKS.get(id).get(index);
    }

    //region Configs

    private void configInit(ConfigBase config) {
        config.resourcesLocked.put(namespace + "ResourceLocked", false);

        ConfigHandler.readOptions(config);

        if (ConfigHandler.getOption(config, namespace + "ResourceLocked") == null) {
            //LOGGER.info("Attempting to generate Resources");
            DirHandler.clean(this.path);
            createRoot();

            config.resourcesLocked.put(namespace + "ResourceLocked", true);
            config.builder.setupResourceProperties();

            LOGGER.info("Generated Resources for \"{}\"", this.namespace);
        } else if (!Boolean.parseBoolean(String.valueOf(ConfigHandler.getOption(config, namespace + "ResourceLocked")))) {
            FileHandler.delete(this.path);
            createRoot();

            config.resourcesLocked.put(namespace + "ResourceLocked", true);
            config.overwrite(namespace + "ResourceLocked", "true");

            LOGGER.info("Regenerated Resources for \"{}\"", this.namespace);
        } else {
            LOGGER.info("Resources for \"{}\" already exist, skipping first generation", this.namespace);
        }
    }

    //endregion

    //region File Generation
    private void assignDirs() {

        this.assetsDir = this.path.resolve("resources/assets");
        this.dataDir = this.path.resolve("resources/data");

        this.namespaceAssetsDir = this.path.resolve("resources/assets/" + this.namespace);
        this.namespaceDataDir = this.path.resolve("resources/data/" + this.namespace);
        this.blockstatesDir = this.namespaceAssetsDir.resolve("blockstates");
        this.tagsDir = this.namespaceDataDir.resolve("tags");

    }

    private void createRoot() {
        DirHandler.create(this.path.resolve("resources"), new String[]{"assets", "data"}); // Without assets and data folder getPath from Builder dies
    }

    /**
     * Creates a basic blockstate for the provided block
     *
     * @param path entry's path
     */
    public ResourcePackBase createBlockstate(String path) {

        DirHandler.create(this.blockstatesDir);

        createJsonFile(this.blockstatesDir.resolve(path))
                .writeJsonIfEmpty(BlockstateJson.createBlockstateJson(this.namespace, path), this.blockstatesDir.resolve(path));

        return this;
    }

    // TODO: expand selection (include all sort of models) + methods to let user add whatever json they want

    /**
     * Creates a basic model for a generated item (not a handheld one)
     *
     * @param path    entry's path
     * @param texture name of the texture
     */
    public ResourcePackBase createItemModel(String path, String texture) {

        Path item = Paths.get(this.namespaceAssetsDir.toString(), "models/item");

        DirHandler.create(item);

        createJsonFile(item.resolve(path))
                .writeJsonIfEmpty(ModelJson.createItemModelJson(this.namespace, "generated", texture), item.resolve(path));

        return this;
    }

    /**
     * Creates a basic block model
     *
     * @param path     entry's path
     * @param texture  name of the texture
     * @param cubeType "all" or "column"
     */
    public ResourcePackBase createBlockModels(String path, String texture, String cubeType) {

        DirHandler.create(this.namespaceAssetsDir.resolve("models"), new String[]{"block", "item"});

        Path block = Paths.get(this.namespaceAssetsDir.toString(), "models/block");
        Path item = Paths.get(this.namespaceAssetsDir.toString(), "models/item");

        createJsonFile(block.resolve(path))
                .writeJsonIfEmpty(ModelJson.createBlockModelJson(cubeType, this.namespace + ":block/" + texture), block.resolve(path));

        createJsonFile(item.resolve(path))
                .writeJsonIfEmpty(ModelJson.createItemModelJson(this.namespace, "block", path), item.resolve(path));

        return this;
    }

    /**
     * Creates a loot table for the block to make it drop itself when mined
     *
     * @param path entry's path
     */
    public ResourcePackBase createBlockDropLootTable(String path) {

        Path lootTables = Paths.get(this.namespaceDataDir.toString(), "loot_tables/blocks");

        DirHandler.create(lootTables);

        createJsonFile(lootTables.resolve(path))
                .writeJsonIfEmpty(LootJson.createBlockBreakLootJson(this.namespace, path), lootTables.resolve(path));

        return this;
    }

    /**
     * Creates a global/common <a href="https://fabricmc.net/wiki/tutorial:tags">tag</a> for the specified entry
     *
     * @param path entry's path
     */
    public ResourcePackBase createGlobalTag(String path) {

        Path commonTagsDir = this.dataDir.resolve("c/tags");
        DirHandler.create(commonTagsDir, new String[]{"blocks", "items"});

        Path blocks = Paths.get(commonTagsDir.toString(), "blocks");
        Path items = Paths.get(commonTagsDir.toString(), "items");

        createJsonFile(blocks.resolve(path))
                .writeJsonIfEmpty(TagJson.createTagJson(this.namespace, new String[]{path}), blocks.resolve(path));

        createJsonFile(items.resolve(path))
                .writeJsonIfEmpty(TagJson.createTagJson(this.namespace, new String[]{path}), items.resolve(path));

        return this;
    }

    /**
     * Defines the harvest tool for the specified blocks
     *
     * @param toolType "axe", "pickaxe", "shovel" or "hoe"
     * @param paths    block identifiers
     */
    public ResourcePackBase createRequiredToolTag(String toolType, String[] paths) {

        Path mineable = Paths.get(this.dataDir.toString(), "minecraft/tags/blocks/mineable"); // Has to be inside of Minecraft folder

        DirHandler.create(mineable);

        if (!Files.exists(mineable.resolve(toolType)))
            createJsonFile(mineable.resolve(toolType)); // Create once, write big Json

        writeJsonIfEmpty(TagJson.createTagJson(this.namespace, paths), mineable.resolve(toolType));

        return this;
    }

    /**
     * Defines the harvest level for the specified blocks
     *
     * @param tier  "stone", "iron" or "diamond"
     * @param paths entries' paths
     */
    public ResourcePackBase createMiningLevelTag(String tier, String[] paths) {
        String fileName = "needs_" + tier + "_tool";
        Path tagBlocks = Paths.get(this.dataDir.toString(), "minecraft/tags/blocks");

        DirHandler.create(tagBlocks);

        if (!Files.exists(tagBlocks.resolve(fileName))) createJsonFile(tagBlocks.resolve(fileName));

        writeJsonIfEmpty(TagJson.createTagJson(this.namespace, paths), tagBlocks.resolve(fileName));

        return this;
    }

    /**
     * Creates a texture from the provided image URL
     *
     * @param block       true to specify it's a block texture
     * @param textureURL  direct link to your .png image <br> (eg. https://i.imgur.com/BAStRdD.png)
     * @param textureName name of the texture file
     */
    public ResourcePackBase createTexture(Boolean block, String textureURL, String textureName) {
        if (UrlUtils.isImage(textureURL)) {

            String actual = StringUtils.checkMissingExtension(textureName, ".png");

            DirHandler.create(this.namespaceAssetsDir.resolve("textures"), new String[]{"block", "item"});

            Path blockDir = this.namespaceAssetsDir.resolve("textures/block");
            Path itemDir = this.namespaceAssetsDir.resolve("textures/item");

            try (InputStream in = new URL(textureURL).openStream()) {
                if (block) {
                    if (!Files.exists(blockDir.resolve(actual))) Files.copy(in, blockDir.resolve(actual));
                } else {
                    if (!Files.exists(itemDir.resolve(actual))) Files.copy(in, itemDir.resolve(actual));
                }
            } catch (IOException e) {
                LOGGER.error("Error on creating a texture .png", e);
            }
        } else {
            LOGGER.warn("Invalid URL for a texture: \"{}\"", textureURL);
        }

        return this;
    }

    /**
     * Creates a language file
     *
     * @param language eg. "en_us"
     * @param entryMap map with the translation key for each entry
     */
    public ResourcePackBase createLang(String language, Map<String, String> entryMap) {

        DirHandler.create(this.namespaceAssetsDir.resolve("lang"));

        createJsonFile(this.namespaceAssetsDir.resolve("lang/" + language))
                .writeJsonIfEmpty(LangJson.createLangJson(entryMap), this.namespaceAssetsDir.resolve("lang/" + language));

        return this;
    }
    //endregion

    //region File Handling
    private ResourcePackBase writeJsonIfEmpty(JsonObject jsonObject, @NotNull Path path) {

        if (Paths.get(path + ".json").toFile().length() == 0) {
            //LOGGER.info("File is empty, writing at {}", path);
            writeJson(jsonObject, path);
        }

        return this;
    }

    private ResourcePackBase writeJsonIfEmpty(String json, @NotNull Path path) {

        if (Paths.get(path + ".json").toFile().length() == 0) {
            LOGGER.info("File is empty, writing at {}", path);
            writeJson(json, path);
        }

        return this;
    }

    private ResourcePackBase writeJson(JsonObject jsonObject, Path path) {

        JsonHandler.writeToJson(jsonObject, path);
        return this;
    }

    private ResourcePackBase writeJson(String json, Path path) {

        JsonHandler.writeToJson(json, path);
        return this;
    }

    private ResourcePackBase createJsonFile(Path path) {

        FileHandler.createJsonFile(path);
        return this;
    }
    //endregion

    //region GUI

    /**
     * Use the default icon for the ResourcePack <p>
     * NOTE: this overwrites any icon specified previously
     */
    public ResourcePackBase useDefaultIcon() {
        DEFAULT_ICON.add(this.namespace);
        return this;
    }

    /**
     * Hide ResourcePack from the GUI
     */
    public ResourcePackBase hide() {
        HIDDEN.add(StringUtils.capitalize(this.namespace));
        return this;
    }

    private void createPackIcon(@NotNull ConfigBase config, @NotNull String namespace, String iconURL) {
        if (UrlUtils.isImage(iconURL)) {
            Path iconPath = Paths.get(config.dirPath.toString(), "resourcepacks/" + namespace.toLowerCase() + "/resources/pack.png");

            try (InputStream in = new URL(iconURL).openStream()) {
                if (!Files.exists(iconPath)) Files.copy(in, iconPath);
            } catch (IOException e) {
                LOGGER.error("Error on creating pack icon .png, falling back to default icon", e);
                this.useDefaultIcon();
            }
        } else {
            LOGGER.warn("Invalid URL for the pack icon (\"{}\"), falling back to default icon", iconURL);
            this.useDefaultIcon();
        }
    }
    //endregion
}
