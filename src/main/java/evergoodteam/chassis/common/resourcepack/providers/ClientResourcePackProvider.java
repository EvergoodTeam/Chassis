package evergoodteam.chassis.common.resourcepack.providers;

import evergoodteam.chassis.common.resourcepack.FileResourcePack;
import evergoodteam.chassis.util.gui.ColorUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.*;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class ClientResourcePackProvider implements ResourcePackProvider {

    private ResourcePackSource resourcePackSource;
    private FileResourcePack fileResourcePack;
    private String namespace;
    private Text displayName;
    private String path;

    /**
     * Provider responsible for the ResourcePackProfile, which displays your ResourcePack in the GUI with a description
     * that you can modify with a lang file at {@code pack.source.<namespace>}
     *
     * @param namespace    name of the config that is root of the resources
     * @param path         name of your ResourcePack
     * @param displayName  translation key for the displayed name
     * @param metadataKey  translation key for the description
     * @param hexDescColor hex color value used for coloring the description
     * @see evergoodteam.chassis.mixin.ResourcePackManagerMixin
     */
    public ClientResourcePackProvider(String namespace, String path, String displayName, String metadataKey, String hexDescColor) {
        this(namespace, path, Text.translatable(displayName), Text.translatable("pack.nameAndSource", Text.translatable(metadataKey), Text.translatable("pack.source." + namespace))
                .setStyle(Style.EMPTY.withColor(ColorUtils.RGB.getIntFromHexRGB(hexDescColor))));
    }

    /**
     * Provider responsible for the ResourcePackProfile, which displays your ResourcePack in the GUI with a description
     * that you can modify with a lang file at {@code pack.source.<namespace>}
     *
     * @param namespace name of the config that is root of the resources
     * @param path      name of your ResourcePack
     * @see evergoodteam.chassis.mixin.ResourcePackManagerMixin
     */
    public ClientResourcePackProvider(String namespace, String path, Text displayName, Text description) {
        this.resourcePackSource = ResourcePackSource.create(name -> description, true);
        this.fileResourcePack = new FileResourcePack(path, description, ResourceType.CLIENT_RESOURCES, FabricLoader.getInstance().getConfigDir().resolve(namespace + "/resourcepacks").toAbsolutePath().normalize());
        this.namespace = namespace;
        this.displayName = displayName;
        this.path = path;
    }

    @Override
    public void register(Consumer<ResourcePackProfile> consumer) {

        ResourcePackProfile profile = ResourcePackProfile.create(
                namespace,
                displayName,
                true,
                factory -> fileResourcePack,
                ResourceType.CLIENT_RESOURCES,
                ResourcePackProfile.InsertionPosition.BOTTOM,
                resourcePackSource);

        //LOGGER.info("Attempting to register Client ResourcePackProfile - {}", profile);
        consumer.accept(profile);
    }
}