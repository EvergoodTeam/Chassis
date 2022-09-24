package evergoodteam.chassis.objects.blocks;

import evergoodteam.chassis.objects.EntryBase;
import net.minecraft.block.OreBlock;

public class OreBase extends OreBlock implements BlockSettings, EntryBase {

    public OreBase(Settings settings) {
        super(settings);
    }
}
