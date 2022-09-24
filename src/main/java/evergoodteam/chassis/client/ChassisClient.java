package evergoodteam.chassis.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Set;

import static evergoodteam.chassis.util.Reference.CMI;
import static org.slf4j.LoggerFactory.getLogger;

@Environment(EnvType.CLIENT)
public class ChassisClient implements ClientModInitializer {

    private static final Set<Block> TRANSPARENT_BLOCKS = new HashSet<>();
    private static final Logger LOGGER = getLogger(CMI + "/Client");

    @Override
    public void onInitializeClient() {
        //LOGGER.info("Total transparent blocks: {}", getTransparentBlocks().size());

        for (Block block : getTransparentBlocks()) {
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout());
        }
    }

    public static void addTransparentBlock(Block block) {
        TRANSPARENT_BLOCKS.add(block);
    }

    public static void removeTransparentBlock(Block block) {
        TRANSPARENT_BLOCKS.remove(block);
    }

    public static Set<Block> getTransparentBlocks() {
        return TRANSPARENT_BLOCKS;
    }
}
