package evergoodteam.chassis.datagen.providers;

import evergoodteam.chassis.common.resourcepack.ResourcePackBase;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.DataProvider;
import net.minecraft.data.server.recipe.RecipeExporter;

import java.util.function.Consumer;

public class ChassisRecipeProvider extends FabricRecipeProvider implements FabricDataGenerator.Pack.Factory<DataProvider> {

    private Consumer<RecipeExporter> recipeConsumer;

    public static ChassisRecipeProvider create(ResourcePackBase resourcePack) {
        return new ChassisRecipeProvider(resourcePack);
    }

    public ChassisRecipeProvider(ResourcePackBase resourcePack) {
        super(resourcePack.output);
    }

    public ChassisRecipeProvider build(Consumer<RecipeExporter> consumer) {
        this.recipeConsumer = consumer;
        return this;
    }

    @Override
    public void generate(RecipeExporter exporter) {
        if (recipeConsumer != null) recipeConsumer.accept(exporter);
    }

    @Override
    public DataProvider create(FabricDataOutput output) {
        return this;
    }
}
