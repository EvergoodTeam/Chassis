package evergoodteam.chassis.mixin;

import evergoodteam.chassis.datagen.ProviderSizeGetter;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(DataGenerator.class)
public class DataGeneratorMixin implements ProviderSizeGetter {

    @Final
    @Shadow
    private List<DataProvider> runningProviders;
    @Final
    @Shadow
    private List<DataProvider> providers;

    @Override
    public int runningProvidersSize() {
        return runningProviders.size();
    }

    @Override
    public int providersSize() {
        return providers.size();
    }
}
