package evergoodteam.chassis.common.resourcepack.providers;

import evergoodteam.chassis.common.resourcepack.FileResourcePack;
import evergoodteam.chassis.util.gui.ColorUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.*;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class ClientResourcePackProvider implements ResourcePackProvider {

    public FileResourcePack groupResourcePack;
    public String namespace;
    public String path;

    /**
     * @see net.minecraft.text.Style
     */
    private ResourcePackSource resourcePackSource;

    /**
     * Provider responsible for the ResourcePackProfile, which displays your ResourcePack in the GUI with a description
     * that you can modify with a lang file at {@code pack.source.<namespace>}
     *
     * @param namespace    name of the Config Folder, root of all the Resources
     * @param path         name of your ResourcePack
     * @param hexDescColor hex color value used for the description text in the GUI
     * @see evergoodteam.chassis.mixin.ResourcePackManagerMixin
     */
    public ClientResourcePackProvider(String namespace, String path, String metadataKey, String hexDescColor) {
        this(namespace, path, metadataKey);

        MutableText text = Text.translatable("pack.source." + namespace);
        UnaryOperator<Text> unaryOperator = name -> Text.translatable("pack.nameAndSource", name, text).setStyle(Style.EMPTY.withColor(ColorUtils.getDecimalFromHex(hexDescColor)));

        this.resourcePackSource = ResourcePackSource.create(unaryOperator, true);
    }

    /**
     * Provider responsible for the ResourcePackProfile, which displays your ResourcePack in the GUI with a description
     * provided with {@link MutableText}
     *
     * @param namespace   name of the Config Folder, root of all the Resources
     * @param path        name of your ResourcePack
     * @param description description of the resources to be displayed in the GUI
     * @see evergoodteam.chassis.mixin.ResourcePackManagerMixin
     */
    public ClientResourcePackProvider(String namespace, String path, String metadataKey, MutableText description) {
        this(namespace, path, metadataKey);
    }

    /**
     * Provider responsible for the ResourcePackProfile, which displays your ResourcePack in the GUI with a description
     * that you can modify with a lang file at {@code pack.source.<namespace>}
     *
     * @param namespace name of the Config Folder, root of all the Resources
     * @param path      name of your ResourcePack
     * @see evergoodteam.chassis.mixin.ResourcePackManagerMixin
     */
    public ClientResourcePackProvider(String namespace, String path, String metadataKey) {
        this.groupResourcePack = new FileResourcePack(path, metadataKey, ResourceType.CLIENT_RESOURCES, FabricLoader.getInstance().getConfigDir().resolve(namespace + "/resourcepacks").toAbsolutePath().normalize());
        this.namespace = namespace;
        this.path = path;
    }

    @Override
    public void register(Consumer<ResourcePackProfile> consumer) {

        ResourcePackProfile profile = ResourcePackProfile.create(
                namespace,
                Text.literal(namespace),
                true,
                new ResourcePackProfile.PackFactory() {
                    @Override
                    public ResourcePack open(String name) {
                        return groupResourcePack;
                    }

                    @Override
                    public ResourcePack openWithOverlays(String name, ResourcePackProfile.Metadata metadata) {
                        return groupResourcePack;
                    }
                },
                ResourceType.CLIENT_RESOURCES,
                ResourcePackProfile.InsertionPosition.BOTTOM,
                resourcePackSource);

        //LOGGER.info("Attempting to register Client ResourcePackProfile - {}", profile);
        consumer.accept(profile);
    }
}