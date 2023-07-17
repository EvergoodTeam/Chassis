package evergoodteam.chassis.common.resourcepack;

import evergoodteam.chassis.config.ConfigBase;
import evergoodteam.chassis.config.option.BooleanOption;
import evergoodteam.chassis.datagen.providers.ChassisTextureProvider;
import evergoodteam.chassis.util.StringUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.data.DataProvider;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;

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
    public @Nullable ProviderRegistry providerRegistry;
    private boolean providersDone = false;
    public boolean noProviders = false;

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
     * Object from which a ResourcePack is generated with strictValidation
     *
     * @param config      {@link ConfigBase} to which assign the ResourcePack; determines the root dir
     * @param namespace   name of your ResourcePack
     * @param displayName name to display in the GUI
     */
    public ResourcePackBase(ConfigBase config, String namespace, String displayName) {
        this(config, namespace, displayName, true);
    }

    public FabricDataGenerator.Pack pack;
    public FabricDataOutput output;
    public CompletableFuture<RegistryWrapper.WrapperLookup> future;

    /**
     * Object from which a ResourcePack is generated
     *
     * @param config           {@link ConfigBase} to which assign the ResourcePack; determines the root dir
     * @param namespace        name of your ResourcePack
     * @param displayName      name to display in the GUI
     * @param strictValidation if a cache should be generated and checked each time providers are run
     */
    public ResourcePackBase(ConfigBase config, String namespace, String displayName, boolean strictValidation) {
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

        this.output = new FabricDataOutput(modContainer, this.root.resources, strictValidation);
        this.future = runInternal();
        this.generator = new FabricDataGenerator(this.root.resources, modContainer, strictValidation, future);
        this.pack = generator.createPack();

        this.configInit();
        this.useDefaultIcon();
        HIDDEN.put(displayName, this.hidden);
        RESOURCE_PACKS.computeIfAbsent(config.modid, k -> new ArrayList<>()).add(this);
    }

    private CompletableFuture<RegistryWrapper.WrapperLookup> runInternal() {

        RegistryBuilder merged = new RegistryBuilder();

        RegistryWrapper.WrapperLookup wrapperLookup = merged.createWrapperLookup(DynamicRegistryManager.of(Registries.REGISTRIES));
        //BuiltinRegistries.validate(wrapperLookup.getWrapperOrThrow(RegistryKeys.PLACED_FEATURE), wrapperLookup.getWrapperOrThrow(RegistryKeys.BIOME));
        return CompletableFuture.supplyAsync(() -> wrapperLookup, Util.getMainWorkerExecutor());
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

    public BooleanOption getLock() {
        return locked;
    }

    public boolean isLocked() {
        return locked.getValue();
    }

    public boolean areProvidersDone() {
        return providersDone;
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
        Path iconPath = root.resources.resolve("pack.png");
        addProvider(ChassisTextureProvider.create(this, "TextureIcon")
                .addTexture(iconUrl, iconPath));
        return this;
    }

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
    //endregion

    //region Configs
    private void configInit() {
        config.resourcesLocked.put(namespace, locked);
        config.getHandler().readOptions();

        if ((config.getHandler().getOptionValue(locked.getName())) == null) {
            root.createRoot();
            config.getWriter().writeResources();
            config.getWriter().overwrite();

            LOGGER.info("Generated resources for \"{}\"", this.namespace);
        } else if (!locked.getWrittenValue(config)) {
            root.createRoot();

            LOGGER.info("Regenerated resources for \"{}\"", namespace);
        } else {
            LOGGER.info("Resources for \"{}\" already exist, skipping first generation", namespace);
        }
    }
    //endregion

    //region Providers

    /**
     * Adds a {@link DataProvider}
     *
     * @see ProviderRegistry#registerProviders()
     */
    public ResourcePackBase addProvider(FabricDataGenerator.Pack.Factory<DataProvider> factory) {
        this.pack.addProvider(factory);
        return this;
    }

    /**
     * Interface used for declaring, initializing and adding providers. Any sort of provider task should be done
     * inside {@link #registerProviders()}, a method that is only called {@link evergoodteam.chassis.mixin.ResourcePackManagerMixin when resources are reloaded}
     * <p><i>Why?</i>
     * <p> Check out <a href="https://github.com/DaFuqs/Spectrum/issues/190">this issue</a>: trying to load the
     * {@link net.minecraft.predicate.item.ItemPredicate ItemPredicate} class (needed for loot tables)
     * will trigger all the associated mixins, which can include stuff that isn't initialized
     */
    public interface ProviderRegistry {

        void registerProviders();
    }

    /**
     * Runs the added providers, must be called for the providers to actually be used. <p>
     * Recommended procedure is calling it inside of {@link ProviderRegistry#registerProviders()} after adding all the providers.
     */
    public void runProviders() {
        try {
            if (!locked.getValue()) {
                generator.run();
                locked.setValue(true);
                config.setWrittenValue(locked, true);
                LOGGER.info("Providers for {} done", namespace);
            } else LOGGER.error("Couldn't run providers for {} because the resource is locked", namespace);
            providersDone = true;
        } catch (IOException e) {
            LOGGER.error("An error occurred while running providers for {}: {}", namespace, e);
        }
    }
    //endregion
}
