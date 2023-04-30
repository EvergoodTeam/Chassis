package evergoodteam.chassis.datagen.providers;

import evergoodteam.chassis.objects.resourcepacks.ResourcePackBase;
import lombok.extern.log4j.Log4j2;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Log4j2
public class ChassisTagProvider<T> extends FabricTagProvider<T> {

    public final ResourcePackBase resourcePack;
    private final Map<TagKey<T>, Consumer<FabricTagBuilder<T>>> tags = new HashMap<>();

    public static <T> ChassisTagProvider<T> create(Registry<T> registry, ResourcePackBase resourcePack) {
        return new ChassisTagProvider<>(registry, resourcePack);
    }

    public ChassisTagProvider(Registry<T> registry, ResourcePackBase resourcePack) {
        super(resourcePack.generator, registry);
        this.resourcePack = resourcePack;
    }

    public ChassisTagProvider<T> build(TagKey<T> key, Consumer<FabricTagBuilder<T>> consumer) {
        this.tags.put(key, consumer);
        return this;
    }

    @Override
    protected void generateTags() {
        for (TagKey<T> key : tags.keySet()) {
            tags.get(key).accept(this.getOrCreateTagBuilder(key));
        }
    }
}
