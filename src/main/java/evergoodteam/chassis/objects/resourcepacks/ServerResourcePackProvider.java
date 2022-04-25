package evergoodteam.chassis.objects.resourcepacks;

import lombok.extern.log4j.Log4j2;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ResourceType;

import java.util.function.Consumer;

@Log4j2
public class ServerResourcePackProvider implements ResourcePackProvider {

    public ResourcePackBuilder groupResourcePack;
    public String namespace;
    public String path;

    /**
     * @param namespace Name of the Config Folder, root of all the Resources
     * @param path      Name of your ResourcePack
     */
    public ServerResourcePackProvider(String namespace, String path) {

        this.groupResourcePack = new ResourcePackBuilder(path, ResourceType.SERVER_DATA, FabricLoader.getInstance().getConfigDir().resolve(namespace + "resourcepacks").toAbsolutePath().normalize());

        this.namespace = namespace;
        this.path = path;
    }

    @Override
    public void register(Consumer<ResourcePackProfile> profileAdder, ResourcePackProfile.Factory factory) {


        ResourcePackProfile profile = ResourcePackProfile.of(
                path,
                true,
                () -> groupResourcePack,
                factory,
                ResourcePackProfile.InsertionPosition.BOTTOM,
                ResourcePackSource.nameAndSource("built-in")
        );

        //log.info("Attempting to register Server ResourcePackProfile - {}", profile);
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