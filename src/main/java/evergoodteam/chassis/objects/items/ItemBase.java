package evergoodteam.chassis.objects.items;

import evergoodteam.chassis.objects.EntryBase;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class ItemBase extends Item implements EntryBase {

    public ItemBase(ItemGroup group) {
        this(new FabricItemSettings().group(group));
    }

    public ItemBase(FabricItemSettings itemSettings) {
        super(itemSettings);
    }
}
