package evergoodteam.chassis.objects.blocks;

import evergoodteam.chassis.client.ChassisClient;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.PillarBlock;
import net.minecraft.sound.BlockSoundGroup;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Needed for proper Blockstate/Model generation when using providers
 */
public class PillarBase extends PillarBlock {

    public PillarBase(Float hardness, Float resistance, BlockSoundGroup sound) {
        this(FabricBlockSettings.create().requiresTool().strength(hardness, resistance).sounds(sound));
    }

    public PillarBase(Float strength, BlockSoundGroup sound) {
        this(FabricBlockSettings.create().requiresTool().strength(strength).sounds(sound));
    }

    public PillarBase(Settings settings) {
        super(settings);
    }

    public PillarBase setTransparent(){
        ChassisClient.addTransparentBlock(this);
        return this;
    }

    @SafeVarargs
    public final PillarBase addTo(List<Block>... lists) {
        for (List<Block> list : lists) list.add(this);
        return this;
    }

    public PillarBase addTo(@NotNull List<Block> list) {
        list.add(this);
        return this;
    }
}
