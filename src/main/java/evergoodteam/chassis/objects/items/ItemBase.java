package evergoodteam.chassis.objects.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

import java.util.ArrayList;
import java.util.List;

public class ItemBase extends Item {

    public static List<Item> ITEMS = new ArrayList<>();

    public ItemBase(ItemGroup group) {
        super(new FabricItemSettings().group(group));

        if(this != null) ITEMS.add(this);
    }

    public ItemBase(FabricItemSettings itemSettings){
        super(itemSettings);

        if(this != null) ITEMS.add(this);
    }
}
