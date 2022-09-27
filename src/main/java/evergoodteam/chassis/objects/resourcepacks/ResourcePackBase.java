package evergoodteam.chassis.objects.resourcepacks;

import com.google.gson.JsonObject;
import evergoodteam.chassis.configs.ConfigBase;
import evergoodteam.chassis.configs.ConfigHandler;
import evergoodteam.chassis.configs.options.BooleanOption;
import evergoodteam.chassis.objects.EntryBase;
import evergoodteam.chassis.objects.assets.*;
import evergoodteam.chassis.util.JsonUtils;
import evergoodteam.chassis.util.StringUtils;
import evergoodteam.chassis.util.UrlUtils;
import evergoodteam.chassis.util.handlers.DirHandler;
import evergoodteam.chassis.util.handlers.FileHandler;
import evergoodteam.chassis.util.handlers.JsonHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static evergoodteam.chassis.util.Reference.CMI;
import static org.slf4j.LoggerFactory.getLogger;

public class ResourcePackBase implements EntryBase {

    private static final Logger LOGGER = getLogger(CMI + "/Resource");

    private static final Map<String, List<ResourcePackBase>> RESOURCE_PACKS = new HashMap<>();
    private static final Map<String, BooleanOption> HIDDEN = new HashMap<>(); // Requires capitalized namespaces
    private static final Set<String> DEFAULT_ICON = new HashSet<>();

    private ConfigBase config;
    private String namespace;
    private String displayName;
    private Path path;
    private String hexColor;
    private BooleanOption locked;
    private BooleanOption hidden;

    private Path assetsDir;
    private Path namespaceAssetsDir;
    private Path blockstatesDir;
    private Path dataDir;
    private Path namespaceDataDir;
    private Path tagsDir;

    // TODO: metadata.description custom key

    /**
     * Object from which a ResourcePack is generated
     *
     * @param config    {@link ConfigBase} to which assign the ResourcePack; determines the root dir
     * @param namespace name of your ResourcePack
     * @param iconUrl   valid URL
     * @param hexColor  hex color value used for the description text in the GUI
     * @deprecated as of release 1.2.3, replaced by {@link #addIcon(String)} & {@link #addColor(String)}
     */
    @Deprecated
    public ResourcePackBase(@NotNull ConfigBase config, @NotNull String namespace, @NotNull String iconUrl, @NotNull String hexColor) {
        this(config, namespace);
        this.addIcon(iconUrl).addColor(hexColor);
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
        this.hexColor = "AAAAAA";

        this.locked = new BooleanOption(namespace + "ResourceLocked", false);
        this.hidden = new BooleanOption("hideResourcePack", false, Text.translatable("config." + namespace + ".hideResourcePack"), Text.translatable("config." + namespace + ".hideResourcePack.tooltip", displayName))
                .setComment("Hide the %s ResourcePack from the GUI".formatted(displayName));

        HIDDEN.put(displayName, this.hidden);
        RESOURCE_PACKS.computeIfAbsent(config.namespace, k -> new ArrayList<>()).add(this);

        this.configInit();
        this.useDefaultIcon();
        this.assignDirs();
    }

    public ResourcePackBase addIcon(@NotNull String iconUrl) {
        DEFAULT_ICON.remove(this.displayName);
        createPackIcon(this.config, this.namespace, iconUrl);
        return this;
    }

    public ResourcePackBase addColor(@NotNull String hexColor) {
        this.hexColor = hexColor;
        return this;
    }

    public String getNamespace() {
        return this.namespace;
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

    public BooleanOption getHiddenBoolean() {
        return hidden;
    }

    /**
     * Returns all the ResourcePack namespaces that use default icons
     */
    public static Set<String> getDefaultIcons() {
        return DEFAULT_ICON;
    }

    //region Configs
    private void configInit() {
        this.config.resourcesLocked.put(this.namespace, this.locked);
        ConfigHandler.readOptions(this.config);

        if (ConfigHandler.getOption(this.config, this.locked.getName()) == null) {
            //LOGGER.warn("Default resource option is missing");
            createRoot();
            this.locked.setValue(true);
            this.config.getBuilder().setupResourceProperties();

            LOGGER.info("Generated resources for \"{}\"", this.namespace);
        } else if (!this.locked.getWrittenValue(this.config)) {
            createRoot();
            this.locked.setValue(true);
            this.config.setWrittenValue(namespace + "ResourceLocked", true);

            LOGGER.info("Regenerated resources for \"{}\"", this.namespace);
        } else {
            LOGGER.info("Resources for \"{}\" already exist, skipping first generation", this.namespace);
        }
    }
    //endregion

    //region File generation
    private void assignDirs() {
        this.assetsDir = this.path.resolve("resources/assets");
        this.namespaceAssetsDir = this.assetsDir.resolve(this.namespace);
        this.blockstatesDir = this.namespaceAssetsDir.resolve("blockstates");
        this.dataDir = this.path.resolve("resources/data");
        this.namespaceDataDir = this.dataDir.resolve(this.namespace);
        this.tagsDir = this.namespaceDataDir.resolve("tags");
    }

    private void createRoot() {
        DirHandler.clean(this.path);
        DirHandler.create(this.path.resolve("resources"));
        DirHandler.create(this.path.resolve("resources"), new String[]{"assets", "data"});
    }

    //region Blockstate

    /**
     * Creates a mirrored column-specific blockstate for the provided block (e.g. deepslate)
     *
     * @param path entry's path
     */
    public ResourcePackBase createMirroredColumnBlockstate(String path) {
        return createJsonAsset(path, this.blockstatesDir, BlockstateJson.createMirroredColumnBlockstateJson(this.namespace, path));
    }

    /**
     * Creates a column-specific blockstate for the provided block (e.g. quartz pillar)
     *
     * @param path entry's path
     */
    public ResourcePackBase createColumnBlockstate(String path) {
        return createJsonAsset(path, this.blockstatesDir, BlockstateJson.createColumnBlockstateJson(this.namespace, path));
    }

    /**
     * Creates a basic blockstate for the provided block
     *
     * @param path entry's path
     */
    public ResourcePackBase createBlockstate(String path) {
        return createJsonAsset(path, this.blockstatesDir, BlockstateJson.createBlockstateJson(this.namespace, path));
    }
    //endregion

    //region Lang

    /**
     * Creates a <a href="https://fabricmc.net/wiki/tutorial:lang">language file</a> to provide translations for translatable strings in-game
     *
     * @param language e.g. "en_us", "zn_cn"
     * @param entryMap map with the translation key for each entry
     * @see <a href="https://minecraft.fandom.com/wiki/Language#Languages">Available languages</a>
     */
    public ResourcePackBase createLang(String language, Map<String, String> entryMap) {
        return createJsonAsset(language, this.namespaceAssetsDir.resolve("lang"), LangJson.createLangJson(entryMap));
    }
    //endregion

    //region Model

    // TODO: transition to ModelBlockType/ItemModelType

    /**
     * Creates a mirrored column model for the block specified by the path (e.g. deepslate)
     *
     * @param path    entry's path
     * @param texture name of the texture .png file
     */
    public ResourcePackBase createMirroredColumnBlockModel(String path, String texture) {
        Path blocks = Paths.get(this.namespaceAssetsDir.toString(), "models/block");
        Path items = Paths.get(this.namespaceAssetsDir.toString(), "models/item");

        createJsonAsset(path, blocks, ModelJson.createBlockModelJson("column", this.namespace + ":block/" + texture));
        createJsonAsset(path + "_mirrored", blocks, ModelJson.createBlockModelJson("column_mirrored", this.namespace + ":block/" + texture));
        createJsonAsset(path, items, ModelJson.createItemModelJson(this.namespace, "block", path));

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
    public ResourcePackBase createBlockModel(String path, String texture, String cubeType) {
        Path blocks = this.namespaceAssetsDir.resolve("models/block");
        Path items = this.namespaceAssetsDir.resolve("models/item");

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
    public ResourcePackBase createItemModel(String path, String texture) {
        Path items = this.namespaceAssetsDir.resolve("models/item");
        return createJsonAsset(path, items, ModelJson.createItemModelJson(this.namespace, "generated", texture));
    }
    //endregion

    //region Texture

    /**
     * Creates a texture from the provided image URL
     *
     * @param block       true to specify it's a block texture, false to specify it's an item texture
     * @param textureURL  direct link to your .png image <p> e.g. "https://i.imgur.com/BAStRdD.png"
     * @param textureName name of the texture .png file
     */
    public ResourcePackBase createTexture(Boolean block, String textureURL, String textureName) {
        if (UrlUtils.isImage(textureURL)) {

            String actual = StringUtils.addExtension(textureName, ".png");

            DirHandler.create(this.namespaceAssetsDir.resolve("textures"));
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
                LOGGER.error("Error on creating a texture .png file", e);
            }
        } else {
            LOGGER.error("Invalid URL (\"{}\") for texture \"{}\"", textureURL, textureName);
        }

        return this;
    }
    //endregion

    //region Advancement

    /**
     * Creates an advancement
     */
    public ResourcePackBase createAdvancement(Identifier icon, @Nullable String nbt, String title, String description, Identifier background, String frame,
                                              boolean toast, boolean announce, boolean hidden,
                                              @Nullable Identifier parent,
                                              String name, Identifier trigger, @Nullable Identifier[] items, @Nullable Identifier tag) {
        Path advancements = this.namespaceDataDir.resolve("advancements");
        return createJsonAsset(name, advancements, AdvancementJson.createAdvancementJson(icon, nbt, title, description, background, frame, toast, announce, hidden, parent, name, trigger, items, tag));
    }
    //endregion

    //region LootTable

    /**
     * Creates a loot table for the block that makes it drop itself when mined
     *
     * @param path entry's path
     */
    public ResourcePackBase createBlockDropLootTable(String path) {
        Path lootTables = this.namespaceDataDir.resolve("loot_tables/blocks");
        return createJsonAsset(path, lootTables, LootJson.createBlockBreakLootJson(this.namespace, path));
    }

    // TODO: account for using entries from other mods

    public ResourcePackBase createGemOreDropLootTable(String orePath, String dropNamespace, String dropPath) {
        return createGemOreDropLootTable(orePath, new Identifier(dropNamespace, dropPath));
    }

    public ResourcePackBase createGemOreDropLootTable(String orePath, Identifier drop) {
        Path lootTables = this.namespaceDataDir.resolve("loot_tables/blocks");
        return createJsonAsset(orePath, lootTables, LootJson.createGemOreDropLootTable(this.namespace, orePath, drop.getNamespace(), drop.getPath()));
    }
    //endregion

    //region Tag

    /**
     * Defines the harvest tool for the specified blocks
     *
     * @param toolType "axe", "pickaxe", "shovel" or "hoe"
     * @param paths    block identifiers
     */
    public ResourcePackBase createRequiredToolTag(String toolType, String[] paths) {
        Path mineable = this.dataDir.resolve("minecraft/tags/blocks/mineable");
        return createJsonAsset(toolType, mineable, TagJson.createTagJson(this.namespace, paths));
    }

    /**
     * Defines the harvest level for the specified blocks
     *
     * @param tier  "stone", "iron" or "diamond"
     * @param paths entries' paths
     */
    public ResourcePackBase createMiningLevelTag(String tier, String[] paths) {
        String fileName = "needs_" + tier + "_tool";
        Path tagBlocks = this.dataDir.resolve("minecraft/tags/blocks");
        return createJsonAsset(fileName, tagBlocks, TagJson.createTagJson(this.namespace, paths));
    }

    /**
     * Creates a global/common <a href="https://fabricmc.net/wiki/tutorial:tags">tag</a> for the specified entry
     *
     * @param path entry's path
     */
    public ResourcePackBase createGlobalTag(String path) {
        return createBlockGlobalTag(path).createItemGlobalTag(path);
    }

    /**
     * Creates an item global/common <a href="https://fabricmc.net/wiki/tutorial:tags">tag</a> for the specified entry
     *
     * @param path entry's path
     */
    public ResourcePackBase createItemGlobalTag(String path) {
        return createTag(path, "c", "items", List.of(new Identifier(this.namespace, path)));
    }

    /**
     * Creates a block global/common <a href="https://fabricmc.net/wiki/tutorial:tags">tag</a> for the specified entry
     *
     * @param path entry's path
     */
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
    public ResourcePackBase createTag(String fileName, String namespace, String type, List<Identifier> entryList) {
        Path path = this.dataDir.resolve(namespace + "/tags/" + type);
        return createJsonAsset(fileName, path, TagJson.createTagJson(entryList));
    }
    //endregion

    /**
     * Creates a json file at the specified parent dir with the provided json <p>
     * Useful when the other supplied methods are not sufficient for your needs
     *
     * @param fileName name of the json file
     * @param parent   path to the parent dir of the json file
     * @param json     valid json transcript
     */
    public ResourcePackBase createJsonAsset(@NotNull String fileName, @NotNull Path parent, @NotNull String json) {
        return createJsonAsset(fileName, parent, JsonUtils.toJsonObject(json));
    }

    /**
     * Creates a json file at the specified parent dir with the provided json <p>
     * Useful when the other supplied methods are not sufficient for your needs
     *
     * @param fileName name of the json file
     * @param parent   path to the parent dir of the json file
     * @param json     valid {@link JsonObject}
     */
    public ResourcePackBase createJsonAsset(@NotNull String fileName, @NotNull Path parent, @NotNull JsonObject json) {
        DirHandler.create(parent);

        createJsonFile(parent.resolve(fileName))
                .writeJsonIfEmpty(json, parent.resolve(fileName + ".json"));

        return this;
    }
    //endregion

    //region File Handling
    private ResourcePackBase writeJsonIfEmpty(JsonObject jsonObject, @NotNull Path path) {
        Path actual = StringUtils.addExtension(path, ".json");
        if (actual.toFile().length() == 0) {
            //LOGGER.info("File is empty, writing at {}", file);
            writeJson(jsonObject, actual);
        }

        // This will remove any sort of modification the user makes, which we don't really want, we want to give the
        // user the option to customize everything about the resourcepack used by a mod
        // TODO: we need a way to check if the rest of the json is correct and leave out the variables to be customizable

        /*else if (!JsonUtils.getJsonObject(file).equals(jsonObject)) {
            //LOGGER.warn("File contains outdated json, re-writing at {}", file);
            FileHandler.emptyFile(file.toFile());
            writeJson(jsonObject, path);
        }*/

        return this;
    }

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
        this.hidden.setValue(true);
        return this;
    }

    public ResourcePackBase unhide() {
        this.hidden.setValue(false);
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
