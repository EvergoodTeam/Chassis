package evergoodteam.chassis.client;

import evergoodteam.chassis.objects.blocks.BlockSettings;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import org.slf4j.Logger;

import static evergoodteam.chassis.util.Reference.CMI;
import static org.slf4j.LoggerFactory.getLogger;

@Environment(EnvType.CLIENT)
public class ChassisClient implements ClientModInitializer {

    private static final Logger LOGGER = getLogger(CMI + "/Client");

    @Override
    public void onInitializeClient() {
        //LOGGER.info("Total transparent blocks: {}", getTransparentBlocks().size());

        for (Block block : BlockSettings.getTransparentBlocks()) {
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout());
        }
    }
}
