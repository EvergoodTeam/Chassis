package evergoodteam.chassis.datagen.providers;

import evergoodteam.chassis.objects.resourcepacks.ResourcePackBase;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ChassisBlockLootTableProvider extends FabricBlockLootTableProvider {

    public final ResourcePackBase resourcePack;
    private final Map<Identifier, Consumer<BlockLootTableGenerator>> blockMap = new HashMap<>();

    public static ChassisBlockLootTableProvider create(ResourcePackBase resourcePack) {
        return new ChassisBlockLootTableProvider(resourcePack);
    }

    protected ChassisBlockLootTableProvider(ResourcePackBase resourcePack) {
        super(resourcePack.generator);
        this.resourcePack = resourcePack;
    }

    public ChassisBlockLootTableProvider buildBlock(Identifier identifier, Consumer<BlockLootTableGenerator> builder) {
        this.blockMap.put(identifier, builder);
        return this;
    }

    @Override
    protected void generateBlockLootTables() {
        for (Identifier identifier : blockMap.keySet()) {
            blockMap.get(identifier).accept(this);
        }
    }
}
