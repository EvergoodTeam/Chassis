package evergoodteam.chassis.datagen.providers;

import evergoodteam.chassis.objects.resourcepacks.ResourcePackBase;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.DataProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;

import java.util.function.Consumer;

public class ChassisRecipeProvider extends FabricRecipeProvider implements FabricDataGenerator.Pack.Factory<DataProvider> {

    private Consumer<Consumer<RecipeJsonProvider>> recipeConsumer;

    public static ChassisRecipeProvider create(ResourcePackBase resourcePack) {
        return new ChassisRecipeProvider(resourcePack);
    }

    public ChassisRecipeProvider(ResourcePackBase resourcePack) {
        super(resourcePack.output);
    }

    public ChassisRecipeProvider build(Consumer<Consumer<RecipeJsonProvider>> consumer) {
        this.recipeConsumer = consumer;
        return this;
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        if (recipeConsumer != null) recipeConsumer.accept(exporter);
    }

    @Override
    public DataProvider create(FabricDataOutput output) {
        return this;
    }
}
