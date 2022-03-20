package evergoodteam.chassis.objects.blocks;


import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

import static evergoodteam.chassis.util.Reference.BLOCKS;


public class BlockBase extends Block {

    public BlockBase(Material block, Float strength, BlockSoundGroup sound) {
        super(FabricBlockSettings.of(block).strength(strength).sounds(sound).nonOpaque());

        if(this != null){
            BLOCKS.add(this);
        }
    }

    public BlockBase(Material block, Float strength) {
        super(FabricBlockSettings.of(block).strength(strength));

        if(this != null){
            BLOCKS.add(this);
        }
    }

}
