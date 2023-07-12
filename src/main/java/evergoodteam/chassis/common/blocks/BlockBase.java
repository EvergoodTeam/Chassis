package evergoodteam.chassis.common.blocks;

import evergoodteam.chassis.client.ChassisClient;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.sound.BlockSoundGroup;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BlockBase extends Block {

    public BlockBase(Float hardness, Float resistance, BlockSoundGroup sound) {
        this(FabricBlockSettings.create().requiresTool().hardness(hardness).resistance(resistance).sounds(sound));
    }

    public BlockBase(Float strength, BlockSoundGroup sound) {
        this(FabricBlockSettings.create().requiresTool().strength(strength).sounds(sound));
    }

    public BlockBase(FabricBlockSettings blockSettings) {
        super(blockSettings);
    }

    public BlockBase setTransparent(){
        ChassisClient.addTransparentBlock(this);
        return this;
    }

    @SafeVarargs
    public final BlockBase addTo(List<Block>... lists) {
        for (List<Block> list : lists) list.add(this);
        return this;
    }

    public BlockBase addTo(@NotNull List<Block> list) {
        list.add(this);
        return this;
    }
}
