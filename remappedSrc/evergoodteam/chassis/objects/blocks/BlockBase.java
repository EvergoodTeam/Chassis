package evergoodteam.chassis.objects.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

import java.util.ArrayList;
import java.util.List;

public class BlockBase extends Block {

    public static final List<Block> TRANSPARENT = new ArrayList<>();

    /**
     * Generates a Block that you can later register
     *
     * @param namespaceGroup list to which add the generated Block - you can use this List to later register the Block(s)
     * @param material
     * @param hardness
     * @param resistance
     * @param sound          {@link BlockSoundGroup}
     */
    public BlockBase(List<Block> namespaceGroup, Material material, Float hardness, Float resistance, BlockSoundGroup sound) {
        this(namespaceGroup, FabricBlockSettings.of(material).requiresTool().strength(hardness, resistance).sounds(sound));
    }

    public BlockBase(List<Block> namespaceGroup, Material material, Float strength, BlockSoundGroup sound) {
        this(namespaceGroup, FabricBlockSettings.of(material).requiresTool().strength(strength).sounds(sound));
    }

    /**
     * Generates a Block that you can later register
     *
     * @param namespaceGroup list to which add the generated Block - you can use this List to later register the Block(s)
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

    public BlockBase(Material material, Float hardness, Float resistance, BlockSoundGroup sound) {
        //super(FabricBlockSettings.of(block).requiresTool().strength(hardness, resistance).sounds(sound));
        this(FabricBlockSettings.of(material).requiresTool().hardness(hardness).resistance(resistance).sounds(sound));
    }

    public BlockBase(Material material, Float strength, BlockSoundGroup sound) {
        this(FabricBlockSettings.of(material).requiresTool().strength(strength).sounds(sound));
    }

    public BlockBase(FabricBlockSettings blockSettings, Boolean transparent) {
        this(blockSettings);

        if (this != null) {
            if (transparent) TRANSPARENT.add(this);
        }
    }

    public BlockBase(FabricBlockSettings blockSettings) {
        super(blockSettings);
    }
}
