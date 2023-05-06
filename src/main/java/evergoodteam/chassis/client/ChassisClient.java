package evergoodteam.chassis.client;

import evergoodteam.chassis.client.gui.text.GradientTextRenderer;
import evergoodteam.chassis.configs.ConfigBase;
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

    static final Logger LOGGER = getLogger(CMI + "/Client");
    public static GradientTextRenderer gradientTextRenderer;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Chassis client initialization");

        for (Block block : BlockSettings.getTransparentBlocks()) {
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout());
        }

        ConfigBase.CONFIGURATIONS.forEach((string, config) -> config.networking.registerClientReceiver());
    }
}
