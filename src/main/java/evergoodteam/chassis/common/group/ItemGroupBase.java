package evergoodteam.chassis.common.group;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ItemGroupBase {

    private static final Map<RegistryKey<ItemGroup>, ItemGroupBase> GROUP_MAP = new HashMap<>();
    private RegistryKey<ItemGroup> registryKey;
    private ItemGroup.Builder builder;

    /**
     * Creates an {@link ItemGroupBase} object, which holds the registry key, identifier and icon for quick access
     *
     * @param icon icon for the creative tab
     */
    public ItemGroupBase(String namespace, String path, ItemConvertible icon) {
        this(new Identifier(namespace, path), icon);
    }

    /**
     * Creates an {@link ItemGroupBase} object, which holds the registry key, identifier and icon for quick access
     *
     * @param icon icon for the creative tab
     */
    public ItemGroupBase(Identifier identifier, ItemConvertible icon) {
        this(identifier);
        this.buildItemGroup(builder -> builder
                .icon(() -> new ItemStack(icon))
                .displayName(Text.translatable(identifier.toTranslationKey())));
    }

    public ItemGroupBase(Identifier identifier) {
        this.registryKey = RegistryKey.of(RegistryKeys.ITEM_GROUP, identifier);
        this.builder = FabricItemGroup.builder();
        GROUP_MAP.put(this.registryKey, this);
    }

    public RegistryKey<ItemGroup> getRegistryKey() {
        return this.registryKey;
    }

    /**
     * Performs the operations on the stored instance of {@link ItemGroup.Builder}, obtained initially with {@link FabricItemGroup#builder()}
     */
    public ItemGroupBase buildItemGroup(Consumer<ItemGroup.Builder> consumer) {
        consumer.accept(this.builder);
        return this;
    }

    /**
     * Gets the ItemGroup held
     */
    public ItemGroup get() {
        return this.builder.build();
    }

    public Identifier getIdentifier() {
        return this.registryKey.getValue();
    }

    /**
     * Returns the {@link ItemGroupBase} with the specified id
     */
    public static ItemGroupBase get(Identifier identifier) {
        return getGroupMap().get(RegistryKey.of(RegistryKeys.ITEM_GROUP, identifier));
    }

    public static ItemGroupBase get(RegistryKey<ItemGroup> registryKey) {
        return getGroupMap().get(registryKey);
    }

    /**
     * Returns all the ItemGroups created with {@link ItemGroupBase}
     */
    public static ImmutableMap<RegistryKey<ItemGroup>, ItemGroupBase> getGroupMap() {
        return ImmutableMap.copyOf(GROUP_MAP);
    }
}