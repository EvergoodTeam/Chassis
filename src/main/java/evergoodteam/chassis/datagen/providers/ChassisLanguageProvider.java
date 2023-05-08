package evergoodteam.chassis.datagen.providers;

import evergoodteam.chassis.objects.resourcepacks.ResourcePackBase;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

import java.util.function.Consumer;

public class ChassisLanguageProvider extends FabricLanguageProvider {

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
        super(resourcePack.generator, languageCode);
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
}
