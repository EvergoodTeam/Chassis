package evergoodteam.chassis.objects.groups;

import evergoodteam.chassis.objects.EntryBase;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class ItemGroupBase implements EntryBase {

    private static final Map<String, ItemGroupBase> GROUP_MAP = new HashMap<>();

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
        this.group = FabricItemGroupBuilder.build(identifier, () -> new ItemStack(icon));
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
    public static ItemGroup createItemGroup(String namespace, String path, ItemConvertible icon) {
        return new ItemGroupBase(namespace, path, icon).getGroup();
    }

    /**
     * Creates an ItemGroup, a.k.a. a creative tab
     *
     * @param identifier ID of your addition
     * @param icon       accepts Item or Block objects; will be used as the icon for the Creative Tab
     */
    public static ItemGroup createItemGroup(Identifier identifier, ItemConvertible icon) {
        return new ItemGroupBase(identifier, icon).getGroup();
    }
}
