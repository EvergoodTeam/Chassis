package evergoodteam.chassis.objects.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

import java.util.ArrayList;
import java.util.List;

public class ItemBase extends Item {

    public static int itemCount = 0;

    public ItemBase(ItemGroup group) {
        this(new FabricItemSettings().group(group));
    }

    public ItemBase(List<Item> namespaceGroup, FabricItemSettings itemSettings){
        this(itemSettings);
        if (this != null) namespaceGroup.add(this);
    }

    public ItemBase(FabricItemSettings itemSettings) {
        super(itemSettings);
        itemCount++;
    }
}
