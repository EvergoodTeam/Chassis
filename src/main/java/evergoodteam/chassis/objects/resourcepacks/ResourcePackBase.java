package evergoodteam.chassis.objects.resourcepacks;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import evergoodteam.chassis.client.models.BlockModelType;
import evergoodteam.chassis.client.models.ItemModelType;
import evergoodteam.chassis.configs.ConfigBase;
import evergoodteam.chassis.configs.ConfigHandler;
import evergoodteam.chassis.configs.options.BooleanOption;
import evergoodteam.chassis.datagen.ChassisTagBuilder;
import evergoodteam.chassis.datagen.providers.ChassisGenericProvider;
import evergoodteam.chassis.datagen.providers.ChassisTextureProvider;
import evergoodteam.chassis.objects.assets.*;
import evergoodteam.chassis.util.JsonUtils;
import evergoodteam.chassis.util.Reference;
import evergoodteam.chassis.util.StringUtils;
import evergoodteam.chassis.util.handlers.FileHandler;
import evergoodteam.chassis.util.handlers.JsonHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.data.DataProvider;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static evergoodteam.chassis.util.Reference.CMI;
import static org.slf4j.LoggerFactory.getLogger;

public class ResourcePackBase {

    private static final Logger LOGGER = getLogger(CMI + "/Resource");
    private static final Map<String, List<ResourcePackBase>> RESOURCE_PACKS = new HashMap<>();
    private static final Map<String, BooleanOption> HIDDEN = new HashMap<>(); // Requires capitalized namespaces
    private static final Set<String> DEFAULT_ICON = new HashSet<>();

    private ConfigBase config;
    private final String namespace;
    private String displayName;
    private final Path path;
    private final ResourcePackRoot root;
    private BooleanOption locked;
    private BooleanOption hidden;
    private String metadataKey;
    private String hexColor = "AAAAAA";
    public final ModContainer modContainer;
    public final FabricDataGenerator generator;
    private final List<DataProvider> providers = new ArrayList<>();
    // TODO: Delete after deprecation
    private ChassisGenericProvider genericJsonProvider;
    private ChassisTextureProvider genericTextureProvider;

    /**
     * Object from which a ResourcePack is generated
     *
     * @param config    {@link ConfigBase} to which assign the ResourcePack; determines the root dir
     * @param namespace name of your ResourcePack
     * @param iconUrl   valid URL
     * @param hexColor  hex color value used for the description text in the GUI
     * @deprecated as of release 1.2.3, replaced by {@link #setIcon(String)} & {@link #setColor(String)}
     */
    @Deprecated
    public ResourcePackBase(@NotNull ConfigBase config, @NotNull String namespace, @NotNull String iconUrl, @NotNull String hexColor) {
        this(config, namespace);
        this.setIcon(iconUrl).setColor(hexColor);
    }

    /**
     * Object from which a ResourcePack without a custom icon is generated
     *
     * @param config    {@link ConfigBase} to which assign the ResourcePack; determines the root dir
     * @param namespace name of your ResourcePack
     */
    public ResourcePackBase(ConfigBase config, String namespace) {
        this(config, namespace, StringUtils.capitalize(namespace));
    }

    /**
     * Object from which a ResourcePack is generated
     *
     * @param config      {@link ConfigBase} to which assign the ResourcePack; determines the root dir
     * @param namespace   name of your ResourcePack
     * @param displayName name to display in the GUI
     */
    public ResourcePackBase(ConfigBase config, String namespace, String displayName) {
        this.config = config;
        this.namespace = namespace;
        this.displayName = displayName;
        this.path = Paths.get(config.dirPath.toString(), "resourcepacks/" + namespace.toLowerCase()); // Root
        this.root = new ResourcePackRoot(this);
        this.metadataKey = namespace + ".metadata.description";
        this.locked = new BooleanOption(namespace + "ResourceLocked", false)
                .setComment("Lock %s resources from being regenerated".formatted(displayName));
        this.hidden = new BooleanOption("hideResourcePack", false, Text.translatable("config." + namespace + ".hideResourcePack"), Text.translatable("config." + namespace + ".hideResourcePack.tooltip", displayName))
                .setEnvType(EnvType.CLIENT)
                .setComment("Hide the %s ResourcePack from the GUI".formatted(displayName));
        this.modContainer = FabricLoader.getInstance().getModContainer(namespace).get();
        this.generator = new FabricDataGenerator(this.root.resources, modContainer, true);
        this.genericJsonProvider = ChassisGenericProvider.create(this);
        this.genericTextureProvider = ChassisTextureProvider.create(this);
        this.generator.addProvider(this.genericJsonProvider);
        this.configInit();
        this.useDefaultIcon();
        HIDDEN.put(displayName, this.hidden);
        RESOURCE_PACKS.computeIfAbsent(config.namespace, k -> new ArrayList<>()).add(this);
    }

    //region Getters
    public ConfigBase getConfig() {
        return this.config;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public Path getRootPath() {
        return path;
    }

    public ResourcePackRoot getRoot() {
        return root;
    }

    public BooleanOption getHideResourcePackProperty() {
        return hidden;
    }

    public String getMetadataKey() {
        return this.metadataKey;
    }

    public String getHexColor() {
        return this.hexColor;
    }

    /**
     * Returns the ResourcePack with the provided id, which corresponds to the root dir and {@link ConfigBase} name
     * to which the ResourcePack is linked
     *
     * @param configName {@link ConfigBase} name to which the ResourcePack is linked
     * @param name       name of the ResourcePack
     */
    public static ResourcePackBase getResourcePack(String configName, String name) {
        List<ResourcePackBase> baseList = ResourcePackBase.RESOURCE_PACKS.get(configName);

        int index = -1;
        for (ResourcePackBase pack : baseList) {
            if (name.toLowerCase().equals(pack.namespace)) {
                index = baseList.indexOf(pack);
            }
        }

        return RESOURCE_PACKS.get(configName).get(index);
    }

    /**
     * Returns all the existing ResourcePacks created through {@link ResourcePackBase}
     */
    public static Map<String, List<ResourcePackBase>> getResourcePacks() {
        return RESOURCE_PACKS;
    }

    /**
     * Returns all the ResourcePack displayNames and their respective boolean
     */
    public static Map<String, BooleanOption> getHiddenBooleans() {
        return HIDDEN;
    }

    /**
     * Returns all the ResourcePack namespaces that use default icons
     */
    public static Set<String> getDefaultIconNamespaces() {
        return DEFAULT_ICON;
    }
    //endregion

    //region Setters
    public ResourcePackBase setColor(@NotNull String hexColor) {
        this.hexColor = hexColor;
        return this;
    }

    /**
     * Sets the ResourcePack's description translation key to the specified string
     */
    public ResourcePackBase setMetadataKey(String key) {
        this.metadataKey = key;
        return this;
    }

    public ResourcePackBase setIcon(@NotNull String iconUrl) {
        DEFAULT_ICON.remove(this.displayName);
        createPackIcon(this.config, this.namespace, iconUrl);
        return this;
    }
    //endregion

    //region Configs
    private void configInit() {
        config.resourcesLocked.put(namespace, locked);
        ConfigHandler.readOptions(config);

        if (ConfigHandler.getOption(config, locked.getName()) == null) {
            //LOGGER.warn("Default resource option is missing");
            root.createRoot();
            locked.setValue(true);
            config.getBuilder().writeResources();
            config.getBuilder().overwrite();

            LOGGER.info("Generated resources for \"{}\"", this.namespace);
        } else if (!locked.getWrittenValue(config)) {
            root.createRoot();
            locked.setValue(true);
            config.setWrittenValue(namespace + "ResourceLocked", true);

            LOGGER.info("Regenerated resources for \"{}\"", namespace);
        } else {
            LOGGER.info("Resources for \"{}\" already exist, skipping first generation", namespace);
        }
    }
    //endregion

    //region Blockstates

    /**
     * Creates a mirrored column-specific blockstate for the provided block (e.g. deepslate)
     *
     * @param path entry's path
     */
    @Deprecated
    public ResourcePackBase createMirroredColumnBlockstate(String path) {
        return createJsonAsset(path, root.blockstates, BlockstateJson.createMirroredColumnBlockstateJson(this.namespace, path));
    }

    /**
     * Creates a column-specific blockstate for the provided block (e.g. quartz pillar)
     *
     * @param path entry's path
     */
    @Deprecated
    public ResourcePackBase createColumnBlockstate(String path) {
        return createJsonAsset(path, root.blockstates, BlockstateJson.createColumnBlockstateJson(this.namespace, path));
    }

    /**
     * Creates a basic blockstate for the provided block
     *
     * @param path entry's path
     */
    @Deprecated
    public ResourcePackBase createBlockstate(String path) {
        return createJsonAsset(path, root.blockstates, BlockstateJson.createBlockstateJson(this.namespace, path));
    }
    //endregion

    //region Lang

    /**
     * Creates a <a href="https://fabricmc.net/wiki/tutorial:lang">language file</a> to provide translations for translatable strings in-game
     *
     * @param language      e.g. "en_us", "zn_cn"
     * @param mappedEntries map with the translation key for each entry
     * @see <a href="https://minecraft.fandom.com/wiki/Language#Languages">Available languages</a>
     */
    @Deprecated
    public ResourcePackBase createLang(String language, Map<String, String> mappedEntries) {
        return createJsonAsset(language, root.lang, LangJson.createLangJson(mappedEntries));
    }
    //endregion

    //region Models

    /**
     * Creates a mirrored column model for the block specified by the path (e.g. deepslate)
     *
     * @param path    entry's path
     * @param texture name of the texture .png file
     */
    @Deprecated
    public ResourcePackBase createMirroredColumnBlockModel(String path, String texture) {
        Path blocks = root.models.resolve("block");
        Path items = root.models.resolve("item");

        createJsonAsset(path, blocks, ModelJson.createBlockModelJson(BlockModelType.COLUMN, this.namespace, "block/" + texture));
        createJsonAsset(path + "_mirrored", blocks, ModelJson.createBlockModelJson(BlockModelType.COLUMN_MIRRORED, this.namespace, "block/" + texture));
        createJsonAsset(path, items, ModelJson.createItemModelJson(ItemModelType.BLOCK, this.namespace, path));

        return this;
    }

    /**
     * Creates a basic block model and its corresponding item model
     *
     * @param path     entry's path
     * @param texture  name of the texture .png file
     * @param cubeType "all" or "column"
     * @deprecated as of release 1.2.3, replaced by {@link #createBlockModel(String, String, String)}
     */
    @Deprecated
    public ResourcePackBase createBlockModels(String path, String texture, String cubeType) {
        return createBlockModel(path, texture, cubeType);
    }

    /**
     * Creates a basic block model and its corresponding item model
     *
     * @param path     entry's path
     * @param texture  name of the texture .png file
     * @param cubeType "all" or "column"
     */
    @Deprecated
    public ResourcePackBase createBlockModel(String path, String texture, String cubeType) {
        Path blocks = root.models.resolve("block");
        Path items = root.models.resolve("item");

        createJsonAsset(path, blocks, ModelJson.createBlockModelJson(cubeType, this.namespace + ":block/" + texture));
        if ("column".equals(cubeType))
            createJsonAsset(path + "_horizontal", blocks, ModelJson.createBlockModelJson(cubeType + "_horizontal", this.namespace + ":block/" + texture));
        createJsonAsset(path, items, ModelJson.createItemModelJson(this.namespace, "block", path));

        return this;
    }

    /**
     * Creates a basic model for a generated item (not a handheld one)
     *
     * @param path    entry's path
     * @param texture name of the texture .png file
     */
    @Deprecated
    public ResourcePackBase createItemModel(String path, String texture) {
        Path items = root.models.resolve("item");
        return createJsonAsset(path, items, ModelJson.createItemModelJson(this.namespace, "generated", texture));
    }
    //endregion

    //region Textures

    /**
     * Creates a texture from the provided image URL
     *
     * @param block       true to specify it's a block texture, false to specify it's an item texture
     * @param textureURL  direct link to your .png image <p> e.g. "https://i.imgur.com/BAStRdD.png"
     * @param textureName name of the texture .png file
     */
    @Deprecated
    public ResourcePackBase createTexture(Boolean block, String textureURL, String textureName) {
        genericTextureProvider.addTexture(textureURL, block, textureName);
        return this;
    }
    //endregion

    //region Loot tables

    /**
     * Creates a loot table for the block that makes it drop itself when mined
     *
     * @param path entry's path
     */
    @Deprecated
    public ResourcePackBase createBlockDropLootTable(String path) {
        return createBlockDropLootTable(this.namespace, path);
    }

    /**
     * Creates a loot table for the block that makes it drop itself when mined
     *
     * @param path entry's path
     */
    @Deprecated
    public ResourcePackBase createBlockDropLootTable(String namespace, String path) {
        Path lootTables = root.dataNamespace.resolve("loot_tables/blocks");
        return createJsonAsset(path, lootTables, LootJson.createBlockBreakLootJson(namespace, path));
    }

    /**
     * @deprecated as of release 1.2.3, use {@link #createGemOreDropLootTable(String, String, String, String)} instead
     */
    @Deprecated
    public ResourcePackBase createGemOreDropLootTable(String orePath, String dropNamespace, String dropPath) {
        return createGemOreDropLootTable(this.namespace, orePath, dropNamespace, dropPath);
    }

    @Deprecated
    public ResourcePackBase createGemOreDropLootTable(String oreNamespace, String orePath, String dropNamespace, String dropPath) {
        Path lootTables = root.dataNamespace.resolve("loot_tables/blocks");
        return createJsonAsset(orePath, lootTables, LootJson.createGemOreDropLootTable(oreNamespace, orePath, dropNamespace, dropPath));
    }
    //endregion

    //region Tags

    /**
     * Defines the harvest tool for the specified blocks
     *
     * @param toolType "axe", "pickaxe", "shovel" or "hoe"
     * @param paths    block identifiers
     */
    @Deprecated
    public ResourcePackBase createRequiredToolTag(String toolType, String[] paths) {
        Path mineable = root.data.resolve("minecraft/tags/blocks/mineable");
        return createJsonAsset(toolType, mineable, TagJson.createTagJson(this.namespace, paths));
    }

    /**
     * Defines the harvest level for the specified blocks
     *
     * @param tier  "stone", "iron" or "diamond"
     * @param paths entries' paths
     */
    @Deprecated
    public ResourcePackBase createMiningLevelTag(String tier, String[] paths) {
        String fileName = "needs_" + tier + "_tool";
        Path tagBlocks = root.data.resolve("minecraft/tags/blocks");
        return createJsonAsset(fileName, tagBlocks, TagJson.createTagJson(this.namespace, paths));
    }

    /**
     * Creates a global/common <a href="https://fabricmc.net/wiki/tutorial:tags">tag</a> for the specified entry
     *
     * @param path entry's path
     */
    @Deprecated
    public ResourcePackBase createGlobalTag(String path) {
        return createBlockGlobalTag(path).createItemGlobalTag(path);
    }

    /**
     * Creates an item global/common <a href="https://fabricmc.net/wiki/tutorial:tags">tag</a> for the specified entry
     *
     * @param path entry's path
     */
    @Deprecated
    public ResourcePackBase createItemGlobalTag(String path) {
        return createTag(path, "c", "items", List.of(new Identifier(this.namespace, path)));
    }

    /**
     * Creates a block global/common <a href="https://fabricmc.net/wiki/tutorial:tags">tag</a> for the specified entry
     *
     * @param path entry's path
     */
    @Deprecated
    public ResourcePackBase createBlockGlobalTag(String path) {
        return createTag(path, "c", "blocks", List.of(new Identifier(this.namespace, path)));
    }

    /**
     * Creates a <a href="https://fabricmc.net/wiki/tutorial:tags">tag</a>
     *
     * @param fileName  name of the tag
     * @param namespace e.g. "c" for common tags, "yourmodid" for personal tags
     * @param type      dir path from the namespace parent, e.g. "blocks", "blocks/mineable"
     * @param entryList entry identifiers to add to the tag
     */
    @Deprecated
    public ResourcePackBase createTag(String fileName, String namespace, String type, List<Identifier> entryList) {
        Path path = root.data.resolve(namespace + "/tags/" + type);
        return createJsonAsset(fileName, path, TagJson.createTagJson(entryList));
    }

    @Deprecated
    public ResourcePackBase createTag(ChassisTagBuilder<?> tagBuilder) {
        return createJsonAsset(tagBuilder.identifier.getPath(), tagBuilder.path, tagBuilder.getAsJsonElement());
    }
    //endregion

    //region Providers

    /**
     * Adds a {@link DataProvider}
     */
    public ResourcePackBase addProvider(DataProvider provider) {
        this.generator.addProvider(provider);
        return this;
    }

    /**
     * Runs the added providers
     */
    public void runProviders() {
        try {
            this.generator.run();
        } catch (IOException e) {
            LOGGER.error("An error occurred while running providers for {}: {}", this, e);
        }
    }
    //endregion

    //region File handling

    /**
     * Creates a json file at the specified parent dir with the provided json <p>
     * Useful when the other supplied methods are not sufficient for your needs
     *
     * @param fileName name of the json file
     * @param parent   path to the parent dir of the json file
     * @param json     valid json transcript
     */
    @Deprecated
    public ResourcePackBase createJsonAsset(@NotNull String fileName, @NotNull Path parent, @NotNull String json) {
        return createJsonAsset(fileName, parent, JsonUtils.toJsonObject(json));
    }

    @Deprecated
    public ResourcePackBase createJsonAsset(@NotNull String fileName, @NotNull Path parent, @NotNull JsonElement json) {
        return createJsonAsset(fileName, parent, json.getAsJsonObject());
    }

    /**
     * Creates a json file at the specified parent dir with the provided json <p>
     * Useful when the other supplied methods are not sufficient for your needs
     *
     * @param fileName name of the json file
     * @param parent   path to the parent dir of the json file
     * @param json     valid {@link JsonObject}
     */
    @Deprecated
    public ResourcePackBase createJsonAsset(@NotNull String fileName, @NotNull Path parent, @NotNull JsonObject json) {
        this.genericJsonProvider.addAsset(json, parent.resolve(StringUtils.addExtension(fileName, ".json")));
        return this;
    }

    @Deprecated
    private ResourcePackBase writeJsonIfEmpty(JsonObject jsonObject, @NotNull Path path) {
        Path actual = StringUtils.addExtension(path, ".json");
        if (actual.toFile().length() == 0) {
            //LOGGER.info("File is empty, writing at {}", file);
            writeJson(jsonObject, actual);
        }

        return this;
    }

    @Deprecated
    private ResourcePackBase writeJson(JsonObject jsonObject, Path path) {
        Path actual = StringUtils.addExtension(path, ".json");
        JsonHandler.writeToJson(jsonObject, actual);
        return this;
    }

    /**
     * Creates a json file at the specified path
     *
     * @param path NOTE: .json extension is not required
     */
    @Deprecated
    private ResourcePackBase createJsonFile(Path path) {
        FileHandler.createJsonFile(path);
        return this;
    }
    //endregion

    //region GUI

    /**
     * Uses the default icon for the ResourcePack
     */
    public ResourcePackBase useDefaultIcon() {
        DEFAULT_ICON.add(this.displayName);
        return this;
    }

    /**
     * Hides the ResourcePack from the GUI
     */
    public ResourcePackBase hide() {
        hidden.setValue(true);
        return this;
    }

    /**
     * Unhides the ResourcePack from the GUI
     */
    public ResourcePackBase unhide() {
        hidden.setValue(false);
        return this;
    }

    private void createPackIcon(@NotNull ConfigBase config, @NotNull String namespace, String iconURL) {
        Path iconPath = Paths.get(config.dirPath.toString(), "resourcepacks/" + namespace.toLowerCase() + "/resources/pack.png");
        this.addProvider(ChassisTextureProvider.create(this)
                .addTexture(iconURL, iconPath));
    }
    //endregion
}
