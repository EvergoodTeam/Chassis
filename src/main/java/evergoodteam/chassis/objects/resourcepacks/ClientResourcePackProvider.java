package evergoodteam.chassis.objects.resourcepacks;

import lombok.extern.log4j.Log4j2;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ResourceType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Log4j2
public class ClientResourcePackProvider implements ResourcePackProvider {

    public List<ResourcePackBuilder> groupResourcePacks = new ArrayList<>();
    public List<String> namespaces;

    public ClientResourcePackProvider(List<String> namespaces) {

        for(int i = 0; i < namespaces.size(); i++){

            this.groupResourcePacks.add(new ResourcePackBuilder(namespaces.get(i), ResourceType.CLIENT_RESOURCES, FabricLoader.getInstance().getConfigDir().resolve("chassis/resourcepacks").toAbsolutePath().normalize()));
        }

        this.namespaces = namespaces;
    }

    @Override
    public void register(Consumer<ResourcePackProfile> profileAdder, ResourcePackProfile.Factory factory) {

        for(int index = 0; index < groupResourcePacks.size(); index++) {

            int finalIndex = index;
            ResourcePackProfile profile = ResourcePackProfile.of(
                    namespaces.get(index),
                    true,
                    () -> groupResourcePacks.get(finalIndex),
                    factory,
                    ResourcePackProfile.InsertionPosition.BOTTOM,
                    ResourcePackSource.nameAndSource("built-in") // Source of RP, displayed in the menu
            );

            //log.info("Attempting to register Client ResourcePackProfile - {}", profile);
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
}