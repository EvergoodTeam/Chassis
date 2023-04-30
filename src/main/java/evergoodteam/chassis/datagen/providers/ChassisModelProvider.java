package evergoodteam.chassis.datagen.providers;

import evergoodteam.chassis.objects.resourcepacks.ResourcePackBase;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateSupplier;
import net.minecraft.data.client.ItemModelGenerator;

import java.util.function.Consumer;

public class ChassisModelProvider extends FabricModelProvider {

    private Consumer<BlockStateModelGenerator> blockStateConsumer;
    private Consumer<ItemModelGenerator> itemModelConsumer;

    public static ChassisModelProvider create(ResourcePackBase resourcePack){
        return new ChassisModelProvider(resourcePack);
    }

    public ChassisModelProvider(ResourcePackBase resourcePack) {
        super(resourcePack.generator);
    }

    public ChassisModelProvider buildBlock(Consumer<BlockStateModelGenerator> consumer){
        this.blockStateConsumer = consumer;
        return this;
    }

    public ChassisModelProvider buildItem(Consumer<ItemModelGenerator> consumer){
        this.itemModelConsumer = consumer;
        return this;
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        if(this.blockStateConsumer != null) this.blockStateConsumer.accept(blockStateModelGenerator);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        if(this.itemModelConsumer != null) this.itemModelConsumer.accept(itemModelGenerator);
    }
}
