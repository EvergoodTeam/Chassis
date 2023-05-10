package evergoodteam.chassis.objects.blocks;

import net.minecraft.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface BlockSettings {

    Set<Block> TRANSPARENT_BLOCKS = new HashSet<>();

    static void addTransparentBlock(Block block) {
        TRANSPARENT_BLOCKS.add(block);
    }

    static void removeTransparentBlock(Block block) {
        TRANSPARENT_BLOCKS.remove(block);
    }

    static Set<Block> getTransparentBlocks() {
        return TRANSPARENT_BLOCKS;
    }

    // TODO: return type, abstract?
    default Block setTransparent() {
        if (this != null) addTransparentBlock((Block) this);
        return (Block) this;
    }
}
