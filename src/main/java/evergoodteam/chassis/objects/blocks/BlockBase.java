package evergoodteam.chassis.objects.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockBase extends Block {

    public static final Map<String, List<Block>> BLOCKS = new HashMap<>(); // TODO: [NU] Convert to Map for easier access
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

        if(this != null) namespaceGroup.add(this);
    }

    public BlockBase(List<Block> namespaceGroup, Material block, Float strength, BlockSoundGroup sound) {
        super(FabricBlockSettings.of(block).requiresTool().strength(strength).sounds(sound));

        if(this != null) namespaceGroup.add(this);
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
        }
    }

    public BlockBase(List<Block> namespaceGroup, FabricBlockSettings blockSettings){
        super(blockSettings);

        if(this != null) namespaceGroup.add(this);
    }
}
