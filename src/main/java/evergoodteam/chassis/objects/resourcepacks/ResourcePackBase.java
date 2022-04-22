package evergoodteam.chassis.objects.resourcepacks;

import lombok.extern.log4j.Log4j2;
import com.google.gson.JsonObject;
import net.fabricmc.loader.impl.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import evergoodteam.chassis.objects.assets.BlockstateJson;
import evergoodteam.chassis.objects.assets.LootJson;
import evergoodteam.chassis.objects.assets.ModelJson;
import evergoodteam.chassis.util.handlers.DirHandler;
import evergoodteam.chassis.configs.ConfigBase;
import evergoodteam.chassis.objects.assets.TagJson;
import evergoodteam.chassis.util.handlers.FileHandler;
import evergoodteam.chassis.util.handlers.JsonHandler;

import static evergoodteam.chassis.util.Reference.*;

@Log4j2
public class ResourcePackBase {

    public static final Map<String, ResourcePackBase> RESOURCE_PACKS = new HashMap<>();

    public static final List<String> NAMESPACES = new ArrayList<>();
    public static final List<String> HIDDEN = new ArrayList<>(); // Capitalized namespaces
    public static final List<String> NO_ICON = new ArrayList<>();

    public String namespace;
    public Path path;

    /**
     * Object from which ResourcePacks are generated
     * @param config Used to determine the root folder
     * @param namespace Name of your ResourcePack
     * @param iconUrl null to not provide a pack icon, otherwise must be a valid URL
     */
    public ResourcePackBase(@NotNull ConfigBase config, @NotNull String namespace, String iconUrl){

        this.namespace = namespace;
        this.path = Paths.get(config.dirPath.toString(), "resourcepacks/" + namespace.toLowerCase()); // Root of every ResourcePack
        //log.info(this.path);

        NAMESPACES.add(namespace);
        log.info(NAMESPACES);
        RESOURCE_PACKS.put(this.namespace, this);

        if(!config.resourceLocked){
            config.cleanResources();
            createRoot();
            config.resourceLocked = true;
            config.setupDefaultProperties();
        }
        else LOGGER.info("Resources for \"{}\" already exist, skipping creation", this.namespace);

        if(iconUrl == null) NO_ICON.add(namespace);
        else createPackIcon(config, namespace, iconUrl);
    }

    // TODO: General cleanup

    public ResourcePackBase createRoot(){

        DirHandler.createDir(this.path.resolve("resources"));
        return this;
    }

    public ResourcePackBase createBlockstate(String path){

        Path assetsDir = this.path.resolve("resources/assets/"+this.namespace);
        DirHandler.createDir(assetsDir.resolve("blockstates"));

        Path blockstates = assetsDir.resolve("blockstates");

        createJsonFile(blockstates.resolve(path))
                .writeJson(BlockstateJson.createBlockstateJson(this.namespace, path), blockstates.resolve(path));

        return this;
    }

    public ResourcePackBase createBlockModels(String path, String texture){

        Path assetsDir = this.path.resolve("resources/assets/"+this.namespace);
        DirHandler.createDirs(assetsDir.resolve("models"), new String[]{"block", "item"});

        Path block = Paths.get(this.path.toString(), "resources/assets", this.namespace + "/models/block");
        Path item = Paths.get(this.path.toString(), "resources/assets", this.namespace + "/models/item");

        createJsonFile(block.resolve(path))
                .writeJson(ModelJson.createBlockModelJson("all", this.namespace + ":block/" + texture), block.resolve(path));

        createJsonFile(item.resolve(path))
                .writeJson(ModelJson.createItemModelJson(this.namespace, "block", path), item.resolve(path));

        return this;
    }

    public ResourcePackBase createBlockDropLootTable(String path){

        Path dataDir = this.path.resolve("resources/data/"+this.namespace);
        DirHandler.createDirs(dataDir.resolve("tags"), new String[]{"blocks", "items"});

        Path lootTables = Paths.get(this.path.toString(), "resources/data/"+this.namespace+"/loot_tables/blocks");

        DirHandler.createDir(lootTables);

        createJsonFile(lootTables.resolve(path))
                .writeJson(LootJson.createBlockBreakLootJson(this.namespace, path), lootTables.resolve(path));

        return this;
    }

    public ResourcePackBase createGlobalTag(String input){

        Path globalCommonDir = this.path.resolve("resources/data/c");
        DirHandler.createDirs(globalCommonDir.resolve("tags"), new String[]{"blocks", "items"});

        Path blocks = Paths.get(this.path.toString(), "resources/data/c/tags/blocks");
        Path items = Paths.get(this.path.toString(), "resources/data/c/tags/items");

        createJsonFile(blocks.resolve(input))
                .writeJson(TagJson.createTagJson(this.namespace, new String[]{input}), blocks.resolve(input));

        createJsonFile(items.resolve(input))
                .writeJson(TagJson.createTagJson(this.namespace, new String[]{input}), items.resolve(input));

        return this;
    }

    public ResourcePackBase createRequiredToolTag(String tool, String[] inputs){

        Path mineable = Paths.get(this.path.toString(), "resources/data/minecraft/tags/blocks/mineable"); // Have to be inside of Minecraft folder

        DirHandler.createDir(mineable);

        if(!Files.exists(mineable.resolve(tool))) createJsonFile(mineable.resolve(tool));

        writeJson(TagJson.createTagJson(this.namespace, inputs), mineable.resolve(tool));

        return this;
    }

    /**
     * @param miningLevel stone, iron, diamond, etc.
     * @param inputs Block name
     * @return
     */
    public ResourcePackBase createMiningLevelTag(String miningLevel, String[] inputs){

        String name = "needs_"+miningLevel+"_tool";

        Path tagBlocks = Paths.get(this.path.toString(), "resources/data/minecraft/tags/blocks");

        DirHandler.createDir(tagBlocks);

        if(!Files.exists(tagBlocks.resolve(name))) createJsonFile(tagBlocks.resolve(name));

        writeJson(TagJson.createTagJson(this.namespace, inputs), tagBlocks.resolve(name));

        return this;
    }

    /**
     * Untested
     * @param block
     * @param textureURL
     * @param textureName Name of the texture; MUSN'T contain .png extension
     * @return
     */
    public ResourcePackBase createTexture(Boolean block, String textureURL, String textureName){

        Path assetsDir = this.path.resolve("resources/assets/"+this.namespace);
        DirHandler.createDirs(assetsDir.resolve("textures"), new String[]{"block", "item"});

        Path blockDir = assetsDir.resolve("textures/block");
        Path itemDir = assetsDir.resolve("textures/item");

        try(InputStream in = new URL(textureURL).openStream()){
            if(block){
                if(!Files.exists(blockDir.resolve(textureName + ".png"))) Files.copy(in, blockDir.resolve(textureName + ".png"));
            }
            else{
                if(!Files.exists(itemDir.resolve(textureName + ".png"))) Files.copy(in, itemDir.resolve(textureName + ".png"));
            }

        } catch (IOException e) {
            log.warn("Error on creating Texture .png", (e));
        }

        return this;
    }


    public ResourcePackBase writeJson(JsonObject jsonObject, Path path){

        JsonHandler.writeToJson(jsonObject, path);
        return this;
    }

    public ResourcePackBase writeJson(String json, Path path){

        JsonHandler.writeToJson(json, path);
        return this;
    }

    public ResourcePackBase createJsonFile(Path path){

        FileHandler.createJsonFile(path);
        return this;
    }


    public ResourcePackBase hide(){
        HIDDEN.add(StringUtil.capitalize(this.namespace));
        return this;
    }

    public void createPackIcon(@NotNull ConfigBase config, @NotNull String namespace, String iconURL){

        Path iconPath = Paths.get(config.dirPath.toString(), "resourcepacks/" + namespace.toLowerCase() + "/resources/pack.png");

        try(InputStream in = new URL(iconURL).openStream()){
            if(!Files.exists(iconPath)) Files.copy(in, iconPath);
        } catch (IOException e) {
            log.warn("Error on creating Pack Icon file, falling back to Unknown Icon", (e));
            this.hide();
        }
    }
}
