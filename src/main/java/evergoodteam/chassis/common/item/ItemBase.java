package evergoodteam.chassis.common.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemBase extends Item {

    public ItemBase(FabricItemSettings itemSettings) {
        super(itemSettings);
    }

    @SafeVarargs
    public final ItemBase addTo(@NotNull List<Item>... lists) {
        for (List<Item> list : lists) list.add(this);
        return this;
    }

    public ItemBase addTo(@NotNull List<Item> list) {
        list.add(this);
        return this;
    }
}
