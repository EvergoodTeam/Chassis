package evergoodteam.chassis.objects.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

import java.util.ArrayList;
import java.util.List;

public class BlockBase extends Block {

    public static final List<Block> TRANSPARENT = new ArrayList<>();
    public static int blockCount = 0;

    /**
     * @param namespaceGroup list to which add the generated Block - you may use this List to later register the Block(s)
     * @param block
     * @param hardness
     * @param resistance
     * @param sound
     */
    public BlockBase(List<Block> namespaceGroup, Material block, Float hardness, Float resistance, BlockSoundGroup sound) {
        this(namespaceGroup, FabricBlockSettings.of(block).requiresTool().strength(hardness, resistance).sounds(sound));
    }

    public BlockBase(List<Block> namespaceGroup, Material block, Float strength, BlockSoundGroup sound) {
        this(namespaceGroup, FabricBlockSettings.of(block).requiresTool().strength(strength).sounds(sound));
    }

    /**
     * @param namespaceGroup list to which add generated Block - you may use this List to later register the Block(s)
     * @param blockSettings
     * @param transparent
     */
    public BlockBase(List<Block> namespaceGroup, FabricBlockSettings blockSettings, Boolean transparent) {
        this(namespaceGroup, blockSettings);

        if (this != null) if (transparent) TRANSPARENT.add(this);
    }

    public BlockBase(List<Block> namespaceGroup, FabricBlockSettings blockSettings) {
        //super(blockSettings);
        this(blockSettings);

        if (this != null) namespaceGroup.add(this);
    }

    public BlockBase(Material block, Float hardness, Float resistance, BlockSoundGroup sound) {
        //super(FabricBlockSettings.of(block).requiresTool().strength(hardness, resistance).sounds(sound));
        this(FabricBlockSettings.of(block).requiresTool().hardness(hardness).resistance(resistance).sounds(sound));
    }

    public BlockBase(Material block, Float strength, BlockSoundGroup sound) {
        this(FabricBlockSettings.of(block).requiresTool().strength(strength).sounds(sound));
    }

    public BlockBase(FabricBlockSettings blockSettings, Boolean transparent) {
        this(blockSettings);

        if (this != null) {
            if (transparent) TRANSPARENT.add(this);
        }
    }

    public BlockBase(FabricBlockSettings blockSettings) {
        super(blockSettings);

        if (this != null) blockCount++;
    }
}
