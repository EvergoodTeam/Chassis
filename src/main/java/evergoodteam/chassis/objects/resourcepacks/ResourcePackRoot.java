package evergoodteam.chassis.objects.resourcepacks;

import evergoodteam.chassis.util.handlers.DirHandler;

import java.nio.file.Path;

public class ResourcePackRoot {

    public final ResourcePackBase resourcePack;
    public final Path resources;
    public final Path assets;
    public final Path assetsNamespace;
    public final Path blockstates;
    public final Path lang;
    public final Path models;
    public final Path textures;
    public final Path data;
    public final Path dataNamespace;
    public final Path tags;

    public ResourcePackRoot(ResourcePackBase resourcePack) {
        this.resourcePack = resourcePack;
        this.resources = resourcePack.getRootPath().resolve("resources");
        this.assets = this.resources.resolve("assets");
        this.data = this.resources.resolve("data");
        this.assetsNamespace = this.assets.resolve(resourcePack.getNamespace());
        this.dataNamespace = this.data.resolve(resourcePack.getNamespace());
        this.blockstates = this.assetsNamespace.resolve("blockstates");
        this.lang = this.assetsNamespace.resolve("lang");
        this.models = this.assetsNamespace.resolve("models");
        this.textures = this.assetsNamespace.resolve("textures");
        this.tags = this.dataNamespace.resolve("tags");
    }

    public void createRoot() {
        DirHandler.clean(resourcePack.getRootPath());
        DirHandler.create(resources);
        DirHandler.create(resources, new String[]{"assets", "data"});
    }
}
