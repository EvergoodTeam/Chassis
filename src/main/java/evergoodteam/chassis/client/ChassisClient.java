package evergoodteam.chassis.client;

import evergoodteam.chassis.client.gui.text.GradientTextRenderer;
import evergoodteam.chassis.configs.ConfigBase;
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

    static final Logger LOGGER = getLogger(CMI + "/Client");
    static final Set<Block> TRANSPARENT_BLOCKS = new HashSet<>();
    public static GradientTextRenderer gradientTextRenderer;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Chassis client initialization");

        for (Block block : TRANSPARENT_BLOCKS) {
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout());
        }

        ConfigBase.CONFIGURATIONS.forEach((string, config) -> config.networking.registerClientReceiver());
    }

    public static void addTransparentBlock(Block block){
        TRANSPARENT_BLOCKS.add(block);
    }

    public static Set<Block> getTransparentBlocks(){
        return TRANSPARENT_BLOCKS;
    }
}
