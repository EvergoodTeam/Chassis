package evergoodteam.chassis.objects.group;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class GroupBase {

    public static ItemGroup createItemGroup(String namespace, String path, Item icon){

        ItemGroup result = FabricItemGroupBuilder.build(new Identifier(namespace, path),
                () -> new ItemStack(icon));

        return result;
    }
}
