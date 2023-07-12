package evergoodteam.chassis.datagen.providers;

import evergoodteam.chassis.common.resourcepacks.ResourcePackBase;
import lombok.extern.log4j.Log4j2;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.data.DataProvider;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;


import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Log4j2
public class ChassisTagProvider<T> extends FabricTagProvider<T> implements FabricDataGenerator.Pack.Factory<DataProvider> {

    public final ResourcePackBase resourcePack;
    private final Map<TagKey<T>, Consumer<FabricTagBuilder>> tags = new HashMap<>();

    public static <T> ChassisTagProvider<T> create(RegistryKey<? extends Registry<T>> key, ResourcePackBase resourcePack) {
        return new ChassisTagProvider<>(key, resourcePack);
    }

    public ChassisTagProvider(RegistryKey<? extends Registry<T>> key, ResourcePackBase resourcePack) {
        super(resourcePack.output, key, resourcePack.future);
        this.resourcePack = resourcePack;
    }

    public ChassisTagProvider<T> build(TagKey<T> key, Consumer<FabricTagBuilder> consumer) {
        this.tags.put(key, consumer);
        return this;
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        for (TagKey<T> key : tags.keySet()) {
            tags.get(key).accept(this.getOrCreateTagBuilder(key));
        }
    }

    @Override
    public DataProvider create(FabricDataOutput output) {
        return this;
    }
}
