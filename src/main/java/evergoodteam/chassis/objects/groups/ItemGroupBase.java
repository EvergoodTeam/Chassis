package evergoodteam.chassis.objects.groups;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ItemGroupBase {

    public ItemGroup group;
    public String namespace;
    public ItemConvertible icon;

    /**
     * Creates an ItemGroup, a.k.a. a creative tab
     *
     * @param namespace your modid
     * @param path      used to identify from other additions from the same namespace
     * @param icon      accepts Item or Block objects; will be used as the icon for the Creative Tab
     */
    public ItemGroupBase(String namespace, String path, ItemConvertible icon) {
        this.group = FabricItemGroupBuilder.build(new Identifier(namespace, path), () -> new ItemStack(icon));
        this.namespace = namespace;
        this.icon = icon;
    }
}
