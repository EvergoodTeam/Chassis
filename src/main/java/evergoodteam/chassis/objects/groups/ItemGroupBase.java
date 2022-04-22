package evergoodteam.chassis.objects.groups;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ItemGroupBase {

    public ItemGroup group;

    /**
     * @param namespace Your ModId
     * @param path Used to identify from other additions from the same namespace
     * @param icon Accepts Item or Block objects; will be used as the icon for the Creative Tab
     */
    public ItemGroupBase(String namespace, String path, ItemConvertible icon){
        this.group = FabricItemGroupBuilder.build(new Identifier(namespace, path),
                () -> new ItemStack(icon));
    }
}
