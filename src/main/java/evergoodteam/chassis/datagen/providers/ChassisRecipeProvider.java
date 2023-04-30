package evergoodteam.chassis.datagen.providers;

import evergoodteam.chassis.objects.resourcepacks.ResourcePackBase;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;

import java.util.function.Consumer;

public class ChassisRecipeProvider extends FabricRecipeProvider {

    //private Consumer<Consumer<RecipeJsonProvider>> shapelessBuilder;
    private Consumer<Consumer<RecipeJsonProvider>> recipeConsumer;

    public static ChassisRecipeProvider create(ResourcePackBase resourcePack) {
        return new ChassisRecipeProvider(resourcePack);
    }

    public ChassisRecipeProvider(ResourcePackBase resourcePack) {
        super(resourcePack.generator);
    }

    /*
    public ChassisRecipeProvider buildShapeless(Consumer<Consumer<RecipeJsonProvider>> consumer) {
        this.shapelessBuilder = consumer;
        return this;
    }
    */

    public ChassisRecipeProvider build(Consumer<Consumer<RecipeJsonProvider>> consumer) {
        this.recipeConsumer = consumer;
        return this;
    }

    @Override
    protected void generateRecipes(Consumer<RecipeJsonProvider> exporter) {
        //if (shapelessBuilder != null) shapelessBuilder.accept(exporter);
        if (recipeConsumer != null) recipeConsumer.accept(exporter);
    }
}
