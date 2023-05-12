package evergoodteam.chassis.objects.blocks;

import evergoodteam.chassis.client.ChassisClient;
import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OreBase extends OreBlock {

    public OreBase(Settings settings) {
        super(settings);
    }

    public OreBase setTransparent(){
        ChassisClient.addTransparentBlock(this);
        return this;
    }

    @SafeVarargs
    public final OreBase addTo(List<Block>... lists) {
        for (List<Block> list : lists) list.add(this);
        return this;
    }

    public OreBase addTo(@NotNull List<Block> list) {
        list.add(this);
        return this;
    }
}
