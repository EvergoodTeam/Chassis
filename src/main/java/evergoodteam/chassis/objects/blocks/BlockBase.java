package evergoodteam.chassis.objects.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

import java.util.ArrayList;
import java.util.List;

public class BlockBase extends Block {

    public static int b = 0;
    public static final List<Block> BLOCK_LIST = new ArrayList<>();
    public static final List<Block> TRANSPARENT = new ArrayList<>();

    /**
     * @param namespaceGroup List to which add generated Block - You may use this List to later register the Block(s)
     * @param block
     * @param hardness
     * @param resistance
     * @param sound
     */
    public BlockBase(List<Block> namespaceGroup, Material block, Float hardness, Float resistance, BlockSoundGroup sound) {
        super(FabricBlockSettings.of(block).requiresTool().strength(hardness, resistance).sounds(sound));

        if(this != null){
            namespaceGroup.add(this);
            b++;
        }
    }

    public BlockBase(List<Block> namespaceGroup, Material block, Float strength, BlockSoundGroup sound) {
        super(FabricBlockSettings.of(block).requiresTool().strength(strength).sounds(sound));

        if(this != null){
            namespaceGroup.add(this);
            b++;
        }
    }

    /**
     * @param namespaceGroup List to which add generated Block - You may use this List to later register the Block(s)
     * @param blockSettings
     * @param transparent
     */
    public BlockBase(List<Block> namespaceGroup, FabricBlockSettings blockSettings, Boolean transparent){
        super(blockSettings);

        if(this != null){
            namespaceGroup.add(this);
            if(transparent) TRANSPARENT.add(this);
            b++;
        }
    }

    public BlockBase(List<Block> namespaceGroup, FabricBlockSettings blockSettings){
        super(blockSettings);

        if(this != null){
            namespaceGroup.add(this);
            b++;
        }
    }
}
