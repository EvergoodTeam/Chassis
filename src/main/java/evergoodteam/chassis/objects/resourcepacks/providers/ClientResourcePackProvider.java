package evergoodteam.chassis.objects.resourcepacks.providers;

import evergoodteam.chassis.objects.resourcepacks.ResourcePackBuilder;
import evergoodteam.chassis.util.ColorConverter;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class ClientResourcePackProvider implements ResourcePackProvider {

    public ResourcePackBuilder groupResourcePack;
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
    public ClientResourcePackProvider(String namespace, String path, String hexDescColor) {
        this(namespace, path);
        this.resourcePackSource = text -> Text.translatable("pack.source." + namespace, text).setStyle(Style.EMPTY.withColor(ColorConverter.getDecimalFromHex(hexDescColor)));
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
    public ClientResourcePackProvider(String namespace, String path, MutableText description) {
        this(namespace, path);
        this.resourcePackSource = text -> description;
    }

    /**
     * Provider responsible for the ResourcePackProfile, which displays your ResourcePack in the GUI with a description
     * that you can modify with a lang file at {@code pack.source.<namespace>}
     *
     * @param namespace name of the Config Folder, root of all the Resources
     * @param path      name of your ResourcePack
     * @see evergoodteam.chassis.mixin.ResourcePackManagerMixin
     */
    public ClientResourcePackProvider(String namespace, String path) {
        this.groupResourcePack = new ResourcePackBuilder(path, ResourceType.CLIENT_RESOURCES, FabricLoader.getInstance().getConfigDir().resolve(namespace + "/resourcepacks").toAbsolutePath().normalize());
        this.namespace = namespace;
        this.path = path;
        this.resourcePackSource = text -> Text.translatable("pack.source." + namespace, text);
    }

    @Override
    public void register(Consumer<ResourcePackProfile> profileAdder, ResourcePackProfile.Factory factory) {

        ResourcePackProfile profile = ResourcePackProfile.of(
                path,
                true,
                () -> groupResourcePack,
                factory,
                ResourcePackProfile.InsertionPosition.BOTTOM,
                resourcePackSource // Description/source, displayed in the GUI
        );

        //LOGGER.info("Attempting to register Client ResourcePackProfile - {}", profile);
        profileAdder.accept(new ResourcePackProfile(
                profile.getName(),
                profile.isAlwaysEnabled(),
                profile::createResourcePack,
                profile.getDisplayName(),
                profile.getDescription(),
                profile.getCompatibility(),
                profile.getInitialPosition(),
                profile.isPinned(),
                profile.getSource())
        );
    }
}