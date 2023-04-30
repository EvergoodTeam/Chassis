package evergoodteam.chassis.objects.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemBase extends Item {

    public ItemBase(ItemGroup group) {
        this(new FabricItemSettings().group(group));
    }

    public ItemBase(FabricItemSettings itemSettings) {
        super(itemSettings);
    }

    public ItemBase addTo(@NotNull List<Item> list) {
        list.add(this);
        return this;
    }
}
