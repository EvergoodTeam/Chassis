package evergoodteam.chassis.datagen.providers;

import evergoodteam.chassis.objects.resourcepacks.ResourcePackBase;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextType;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ChassisLootTableProvider extends SimpleFabricLootTableProvider {

    public final ResourcePackBase resourcePack;
    private final Map<Identifier, LootTable.Builder> builderMap = new HashMap<>();

    public static ChassisLootTableProvider create(LootContextType contextType, ResourcePackBase resourcePack) {
        return new ChassisLootTableProvider(contextType, resourcePack);
    }

    public ChassisLootTableProvider(LootContextType lootContextType, ResourcePackBase resourcePack) {
        super(resourcePack.generator, lootContextType);
        this.resourcePack = resourcePack;
    }

    public ChassisLootTableProvider build(Identifier identifier, LootTable.Builder builder) {
        this.builderMap.put(identifier, builder);
        return this;
    }

    public ChassisLootTableProvider build(Identifier identifier, Consumer<LootTable.Builder> consumer) {
        LootTable.Builder builder = LootTable.builder();
        consumer.accept(builder);
        this.builderMap.put(identifier, builder);
        return this;
    }

    @Override
    public void accept(BiConsumer<Identifier, LootTable.Builder> biConsumer) {
        for (Identifier identifier : builderMap.keySet()) {
            biConsumer.accept(identifier, builderMap.get(identifier));
        }
    }

    @Override
    public String getName() {
        return "Loot Table " + Objects.requireNonNull(LootContextTypes.getId(lootContextType), "Could not get id for loot context type");
    }
}
