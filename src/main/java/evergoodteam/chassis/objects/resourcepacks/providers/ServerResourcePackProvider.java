package evergoodteam.chassis.objects.resourcepacks.providers;

import evergoodteam.chassis.objects.resourcepacks.ResourcePackBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class ServerResourcePackProvider implements ResourcePackProvider {

    public ResourcePackBuilder groupResourcePack;
    public String namespace;
    public String path;

    private ResourcePackSource resourcePackSource;

    /**
     * @param namespace name of the Config Folder, root of all the Resources
     * @param path      name of your ResourcePack
     * @see evergoodteam.chassis.mixin.ResourcePackManagerMixin
     */
    public ServerResourcePackProvider(String namespace, String path) {
        this.groupResourcePack = new ResourcePackBuilder(path, ResourceType.SERVER_DATA, FabricLoader.getInstance().getConfigDir().resolve(namespace + "/resourcepacks").toAbsolutePath().normalize());
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
                resourcePackSource
        );

        //LOGGER.info("Attempting to register Server ResourcePackProfile - {}", profile);
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