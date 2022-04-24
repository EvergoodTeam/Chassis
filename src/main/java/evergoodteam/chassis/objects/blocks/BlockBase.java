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

    public static final Map<String, Block> BLOCKS = new HashMap<>(); // TODO: [NU] Convert to Map for easier access
    public static final List<Block> TRANSPARENT = new ArrayList<>();

    public BlockBase(String namespace, Material block, Float hardness, Float resistance, BlockSoundGroup sound) {
        super(FabricBlockSettings.of(block).requiresTool().strength(hardness, resistance).sounds(sound));

        if(this != null) BLOCKS.put(namespace, this);
    }

    public BlockBase(String namespace, Material block, Float strength, BlockSoundGroup sound) {
        super(FabricBlockSettings.of(block).requiresTool().strength(strength).sounds(sound));

        if(this != null) BLOCKS.put(namespace, this);
    }

    public BlockBase(String namespace, FabricBlockSettings blockSettings, Boolean transparent){
        super(blockSettings);

        if(this != null){
            BLOCKS.put(namespace, this);
            if(transparent) TRANSPARENT.add(this);
        }
    }

    public BlockBase(String namespace, FabricBlockSettings blockSettings){
        super(blockSettings);

        if(this != null) BLOCKS.put(namespace, this);
    }
}
