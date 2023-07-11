package evergoodteam.chassis.datagen.providers;

import evergoodteam.chassis.objects.resourcepacks.ResourcePackBase;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.data.DataProvider;
import net.minecraft.data.server.loottable.BlockLootTableGenerator;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ChassisBlockLootTableProvider extends FabricBlockLootTableProvider implements FabricDataGenerator.Pack.Factory<DataProvider> {

    public final ResourcePackBase resourcePack;
    private final Map<Identifier, Consumer<BlockLootTableGenerator>> blockMap = new HashMap<>();

    public static ChassisBlockLootTableProvider create(ResourcePackBase resourcePack) {
        return new ChassisBlockLootTableProvider(resourcePack);
    }

    public ChassisBlockLootTableProvider(ResourcePackBase resourcePack) {
        super(resourcePack.output);
        this.resourcePack = resourcePack;
    }

    public ChassisBlockLootTableProvider buildBlock(Identifier identifier, Consumer<BlockLootTableGenerator> builder) {
        this.blockMap.put(identifier, builder);
        return this;
    }

    @Override
    public void generate() {
        for (Identifier identifier : blockMap.keySet()) {
            blockMap.get(identifier).accept(this);
        }
    }

    @Override
    public DataProvider create(FabricDataOutput output) {
        return this;
    }
}
