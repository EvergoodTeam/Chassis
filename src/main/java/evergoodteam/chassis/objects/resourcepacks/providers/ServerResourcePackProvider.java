package evergoodteam.chassis.objects.resourcepacks.providers;

import evergoodteam.chassis.objects.resourcepacks.ResourcePackBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class ServerResourcePackProvider implements ResourcePackProvider {

    public String namespace;
    public String path;
    public ResourcePackBuilder groupResourcePack;
    private ResourcePackSource resourcePackSource;

    /**
     * @param namespace name of your {@link evergoodteam.chassis.configs.ConfigBase ConfigBase}
     * @param path      name of your ResourcePack
     * @see evergoodteam.chassis.mixin.ResourcePackManagerMixin
     */
    public ServerResourcePackProvider(String namespace, String path, String metadataKey) {
        this.namespace = namespace;
        this.path = path;
        MutableText text = Text.translatable("pack.source." + namespace);
        UnaryOperator<Text> unaryOperator = name -> Text.translatable("pack.nameAndSource", name, text);
        this.groupResourcePack = new ResourcePackBuilder(path, metadataKey, ResourceType.SERVER_DATA, FabricLoader.getInstance().getConfigDir().resolve(namespace + "/resourcepacks").toAbsolutePath().normalize());
        this.resourcePackSource = ResourcePackSource.create(unaryOperator, true);
    }

    @Override
    public void register(Consumer<ResourcePackProfile> consumer) {

        ResourcePackProfile profile = ResourcePackProfile.create(
                namespace,
                Text.literal(namespace),
                true,
                factory -> groupResourcePack,
                ResourceType.SERVER_DATA,
                ResourcePackProfile.InsertionPosition.BOTTOM,
                resourcePackSource);

        //LOGGER.info("Attempting to register Server ResourcePackProfile - {}", profile);
        consumer.accept(profile);
    }
}