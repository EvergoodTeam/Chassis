package evergoodteam.chassis.datagen.providers;

import evergoodteam.chassis.objects.resourcepacks.ResourcePackBase;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.data.DataProvider;

import java.util.function.Consumer;

public class ChassisLanguageProvider extends FabricLanguageProvider implements FabricDataGenerator.Pack.Factory<DataProvider> {

    public final ResourcePackBase resourcePack;
    private Consumer<TranslationBuilder> consumer;

    public static ChassisLanguageProvider create(ResourcePackBase resourcePack) {
        return new ChassisLanguageProvider(resourcePack);
    }

    public static ChassisLanguageProvider create(ResourcePackBase resourcePack, String languageCode) {
        return new ChassisLanguageProvider(resourcePack, languageCode);
    }

    public ChassisLanguageProvider(ResourcePackBase resourcePack) {
        this(resourcePack, "en_us");
    }

    public ChassisLanguageProvider(ResourcePackBase resourcePack, String languageCode) {
        super(resourcePack.output, languageCode);
        this.resourcePack = resourcePack;
    }

    public ChassisLanguageProvider build(Consumer<TranslationBuilder> builder) {
        consumer = builder;
        return this;
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        consumer.accept(translationBuilder);
    }

    @Override
    public DataProvider create(FabricDataOutput output) {
        return this;
    }
}
