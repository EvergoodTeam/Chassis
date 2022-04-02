package evergoodteam.chassis.objects.blocks;


import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

import static evergoodteam.chassis.util.Reference.BLOCKS;


public class BlockBase extends Block {

    public BlockBase(Material block, Float hardness, BlockSoundGroup sound) {
        super(FabricBlockSettings.of(block).requiresTool().strength(hardness).sounds(sound).nonOpaque());

        if(this != null){
            BLOCKS.add(this);
        }
    }

    public BlockBase(Material block, Float hardness) {
        super(FabricBlockSettings.of(block).requiresTool().strength(hardness));

        if(this != null){
            BLOCKS.add(this);
        }
    }

}
