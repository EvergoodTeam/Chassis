package evergoodteam.chassis.common.group;

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

public class ItemGroupBase {

    private static final Map<String, ItemGroupBase> GROUP_MAP = new HashMap<>();

    public RegistryKey<ItemGroup> registry;
    public ItemGroup group;
    public Identifier identifier;
    public ItemConvertible icon;

    /**
     * Creates an ItemGroup, a.k.a. a creative tab
     *
     * @param namespace your modid
     * @param path      used to identify from other additions from the same namespace
     * @param icon      accepts Item or Block objects; will be used as the icon for the Creative Tab
     */
    public ItemGroupBase(String namespace, String path, ItemConvertible icon) {
        this(new Identifier(namespace, path), icon);
    }

    /**
     * Creates an ItemGroup, a.k.a. a creative tab
     *
     * @param identifier ID of your addition
     * @param icon       accepts Item or Block objects; will be used as the icon for the Creative Tab
     */
    public ItemGroupBase(Identifier identifier, ItemConvertible icon) {
        this.registry = RegistryKey.of(RegistryKeys.ITEM_GROUP, identifier);
        this.group = FabricItemGroup.builder()
                .icon(() -> new ItemStack(icon))
                .displayName(Text.translatable(identifier.toTranslationKey()))
                .build();
        this.identifier = identifier;
        this.icon = icon;

        GROUP_MAP.put(identifier.toString(), this);
    }

    /**
     * Gets the ItemGroup associated with the ItemGroupBase
     */
    public ItemGroup getGroup() {
        return this.group;
    }

    /**
     * Gets the ItemGroupBase with the provided identifier
     */
    public static ItemGroupBase getGroup(Identifier identifier) {
        return getGroupMap().get(identifier.toString());
    }

    /**
     * Gets all the ItemGroupBases created
     */
    public static Map<String, ItemGroupBase> getGroupMap() {
        return GROUP_MAP;
    }

    /**
     * Creates an ItemGroup, a.k.a. a creative tab
     *
     * @param namespace your modid
     * @param path      used to identify from other additions from the same namespace
     * @param icon      accepts Item or Block objects; will be used as the icon for the Creative Tab
     */
    public static ItemGroupBase createItemGroup(String namespace, String path, ItemConvertible icon) {
        return new ItemGroupBase(namespace, path, icon);
    }

    /**
     * Creates an ItemGroup, a.k.a. a creative tab
     *
     * @param identifier ID of your addition
     * @param icon       accepts Item or Block objects; will be used as the icon for the Creative Tab
     */
    public static ItemGroupBase createItemGroup(Identifier identifier, ItemConvertible icon) {
        return new ItemGroupBase(identifier, icon);
    }
}
