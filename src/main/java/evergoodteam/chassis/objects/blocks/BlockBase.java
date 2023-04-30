package evergoodteam.chassis.objects.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import org.jetbrains.annotations.NotNull;

import java.util.List;

// TODO: clean up constructors with version group change
public class BlockBase extends Block implements BlockSettings {

    /**
     * @deprecated as of release 1.2.3, use {@link #addTo(List)} ()} instead
     */
    @Deprecated
    public BlockBase(List<Block> list, Material material, Float hardness, Float resistance, BlockSoundGroup sound) {
        this(list, FabricBlockSettings.of(material).requiresTool().strength(hardness, resistance).sounds(sound));
    }

    /**
     * @deprecated as of release 1.2.3, use {@link #addTo(List)} ()} instead
     */
    @Deprecated
    public BlockBase(List<Block> list, Material material, Float strength, BlockSoundGroup sound) {
        this(list, FabricBlockSettings.of(material).requiresTool().strength(strength).sounds(sound));
    }

    /**
     * @deprecated as of release 1.2.3, use {@link #setTransparent()} & {@link #addTo(List)} instead
     */
    @Deprecated
    public BlockBase(List<Block> list, FabricBlockSettings blockSettings, Boolean transparent) {
        this(list, blockSettings);
        if (transparent) this.setTransparent();
    }

    /**
     * @deprecated as of release 1.2.3, use {@link #addTo(List)} ()} instead
     */
    @Deprecated
    public BlockBase(List<Block> list, FabricBlockSettings blockSettings) {
        this(blockSettings);
        this.addTo(list);
    }

    public BlockBase(Material material, Float hardness, Float resistance, BlockSoundGroup sound) {
        this(FabricBlockSettings.of(material).requiresTool().hardness(hardness).resistance(resistance).sounds(sound));
    }

    public BlockBase(Material material, Float strength, BlockSoundGroup sound) {
        this(FabricBlockSettings.of(material).requiresTool().strength(strength).sounds(sound));
    }

    /**
     * @deprecated as of release 1.2.3, use {@link #setTransparent()} instead
     */
    @Deprecated
    public BlockBase(FabricBlockSettings blockSettings, Boolean transparent) {
        this(blockSettings);
        if (transparent) this.setTransparent();
    }

    public BlockBase(FabricBlockSettings blockSettings) {
        super(blockSettings);
    }

    public BlockBase addTo(@NotNull List<Block> list) {
        list.add(this);
        return this;
    }
}
