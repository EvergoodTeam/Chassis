package evergoodteam.chassis.objects.blocks;

import evergoodteam.chassis.client.models.BlockModelType;
import evergoodteam.chassis.client.models.ModelBundler;
import evergoodteam.chassis.objects.EntryBase;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.List;

public class BlockBase extends Block implements BlockSettings, EntryBase {

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
        this(FabricBlockSettings.of(material).requiresTool().strength(hardness, resistance).sounds(sound));
        this.addToList(namespaceGroup);
    }

    public BlockBase(List<Block> namespaceGroup, Material material, Float strength, BlockSoundGroup sound) {
        this(FabricBlockSettings.of(material).requiresTool().strength(strength).sounds(sound));
        this.addToList(namespaceGroup);
    }

    /**
     * Generates a Block that you can later register
     *
     * @param namespaceGroup list to which add the generated Block - you can use this List to later register the Block(s)
     * @param blockSettings
     * @param transparent
     * @deprecated as of release 1.2.3, replaced by {@link BlockSettings#setTransparent() setTransparent()}
     */
    @Deprecated
    public BlockBase(List<Block> namespaceGroup, FabricBlockSettings blockSettings, Boolean transparent) {
        this(namespaceGroup, blockSettings);
        this.addToList(namespaceGroup);

        if (transparent) this.setTransparent();
    }

    public BlockBase(List<Block> namespaceGroup, FabricBlockSettings blockSettings) {
        this(blockSettings);
        this.addToList(namespaceGroup);
    }

    public BlockBase(Material material, Float hardness, Float resistance, BlockSoundGroup sound) {
        this(FabricBlockSettings.of(material).requiresTool().hardness(hardness).resistance(resistance).sounds(sound));
    }

    public BlockBase(Material material, Float strength, BlockSoundGroup sound) {
        this(FabricBlockSettings.of(material).requiresTool().strength(strength).sounds(sound));
    }

    /**
     * @deprecated as of release 1.2.3, replaced by {@link BlockSettings#setTransparent() setTransparent()}
     */
    @Deprecated
    public BlockBase(FabricBlockSettings blockSettings, Boolean transparent) {
        this(blockSettings);
        if (transparent) this.setTransparent();
    }

    public BlockBase(FabricBlockSettings blockSettings) {
        super(blockSettings);
    }

    public void bundleModel(ModelBundler bundler, String namespace, String path, BlockModelType type) {
        bundler.addBlock(new Identifier(namespace, path), type);
    }
}
