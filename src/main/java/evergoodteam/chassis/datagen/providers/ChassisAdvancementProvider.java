package evergoodteam.chassis.datagen.providers;

import evergoodteam.chassis.objects.resourcepacks.ResourcePackBase;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ChassisAdvancementProvider extends FabricAdvancementProvider {

    public final ResourcePackBase resourcePack;
    private final Map<Identifier, Advancement.Builder> builderMap = new HashMap<>();

    public static ChassisAdvancementProvider create(ResourcePackBase resourcePack) {
        return new ChassisAdvancementProvider(resourcePack);
    }

    public ChassisAdvancementProvider(ResourcePackBase resourcePack) {
        super(resourcePack.generator);
        this.resourcePack = resourcePack;
    }

    public ChassisAdvancementProvider build(Identifier identifier, Consumer<Advancement.Builder> consumer) {
        Advancement.Builder builder = Advancement.Builder.create();
        consumer.accept(builder);
        builderMap.put(identifier, builder);
        return this;
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        for(Identifier identifier : builderMap.keySet()){
            builderMap.get(identifier).build(consumer, identifier.toString());
        }
    }
}
