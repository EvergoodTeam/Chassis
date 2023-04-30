package evergoodteam.chassis.datagen;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import evergoodteam.chassis.objects.resourcepacks.ResourcePackBase;
import lombok.extern.log4j.Log4j2;
import net.minecraft.tag.TagBuilder;
import net.minecraft.tag.TagFile;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.stream.Stream;

import static evergoodteam.chassis.util.Reference.CMI;

@Deprecated
@Log4j2
public class ChassisTagBuilder<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CMI + "/D/TagBuilder");
    public final Identifier identifier;
    public final TagKey<T> tagKey;
    public final TagBuilder builder = TagBuilder.create();
    public final Registry<T> registry;
    public final Path path;

    public ChassisTagBuilder(Identifier identifier, Registry<T> registry, ResourcePackBase resourcePackBase){
        this(identifier, registry, resourcePackBase.getRoot().data.resolve("%s/%ss".formatted(identifier.getNamespace(), registry.getKey().getValue().getPath())));
    }

    public ChassisTagBuilder(Identifier identifier, Registry<T> registry, Path output) {
        this.identifier = identifier;
        this.tagKey = TagKey.of(registry.getKey(), identifier);
        this.registry = registry;
        this.path = output;
    }

    public ChassisTagBuilder<T> add(T element) {
        this.builder.add(this.registry.getId(element));
        return this;
    }

    @SafeVarargs
    public final ChassisTagBuilder<T> add(RegistryKey<T>... keys) {
        for (RegistryKey<T> registryKey : keys) {
            this.builder.add(registryKey.getValue());
        }
        return this;
    }

    @SafeVarargs
    public final ChassisTagBuilder<T> add(T... elements) {
        Stream.of(elements).map(this.registry::getId).forEach(id -> this.builder.add((Identifier) id));
        return this;
    }

    public ChassisTagBuilder<T> addTag(TagKey<T> identifiedTag) {
        this.builder.addTag(identifiedTag.id());
        return this;
    }

    public ChassisTagBuilder<T> addOptional(Identifier id) {
        this.builder.addOptional(id);
        return this;
    }

    public ChassisTagBuilder<T> addOptionalTag(Identifier id) {
        this.builder.addOptionalTag(id);
        return this;
    }

    public ChassisTagBuilder<T> addOptionalTag(TagKey<T> tagKey){
        return addOptionalTag(tagKey.id());
    }

    public JsonElement getAsJsonElement(){
        return TagFile.CODEC.encodeStart(JsonOps.INSTANCE, new TagFile(builder.build(), false))
                .getOrThrow(false, LOGGER::error);
    }
}
