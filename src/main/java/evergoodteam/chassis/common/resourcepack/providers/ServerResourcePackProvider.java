package evergoodteam.chassis.common.resourcepack.providers;

import evergoodteam.chassis.common.resourcepack.FileResourcePack;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.*;
import net.minecraft.text.Text;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class ServerResourcePackProvider implements ResourcePackProvider {

    private ResourcePackSource resourcePackSource;
    private FileResourcePack fileResourcePack;
    private String namespace;
    private Text displayName;
    private String path;

    /**
     * @param namespace    name of the config that is root of the resources
     * @param path         name of your ResourcePack
     * @param displayName  translation key for the displayed name
     * @param metadataKey  translation key for the description
     * @see evergoodteam.chassis.mixin.ResourcePackManagerMixin
     */
    public ServerResourcePackProvider(String namespace, String path, String displayName, String metadataKey) {
        Text description = Text.translatable("pack.nameAndSource", Text.translatable(metadataKey), Text.translatable("pack.source." + namespace));
        UnaryOperator<Text> unaryOperator = name -> description;

        this.resourcePackSource = ResourcePackSource.create(unaryOperator, true);
        this.fileResourcePack = new FileResourcePack(path, description, ResourceType.SERVER_DATA, FabricLoader.getInstance().getConfigDir().resolve(namespace + "/resourcepacks").toAbsolutePath().normalize());
        this.namespace = namespace;
        this.displayName = Text.translatable(displayName);
        this.path = path;
    }

    @Override
    public void register(Consumer<ResourcePackProfile> consumer) {

        ResourcePackProfile profile = ResourcePackProfile.create(
                namespace,
                displayName,
                true,
                new ResourcePackProfile.PackFactory() {
                    @Override
                    public ResourcePack open(String name) {
                        return fileResourcePack;
                    }

                    @Override
                    public ResourcePack openWithOverlays(String name, ResourcePackProfile.Metadata metadata) {
                        return fileResourcePack;
                    }
                },
                ResourceType.SERVER_DATA,
                ResourcePackProfile.InsertionPosition.BOTTOM,
                resourcePackSource);

        //LOGGER.info("Attempting to register Server ResourcePackProfile - {}", profile);
        consumer.accept(profile);
    }
}