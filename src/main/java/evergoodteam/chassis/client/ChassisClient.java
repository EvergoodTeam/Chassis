package evergoodteam.chassis.client;

import evergoodteam.chassis.client.gui.shader.ShaderWrapper;
import evergoodteam.chassis.client.gui.text.GradientTextRenderer;
import evergoodteam.chassis.util.handlers.RegistryHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;

import static evergoodteam.chassis.util.Reference.CMI;
import static org.slf4j.LoggerFactory.getLogger;

@Environment(EnvType.CLIENT)
public class ChassisClient implements ClientModInitializer {

    private final Logger LOGGER = getLogger(CMI + "/Client");
    public static GradientTextRenderer gradientTextRenderer;
    public static final ShaderWrapper HSV_PROGRAM = new ShaderWrapper(new Identifier("chassis", "hsv2rgb"), VertexFormats.POSITION_COLOR);

    @Override
    public void onInitializeClient() {
        LOGGER.info("Chassis client initialization");

        RegistryHandler.getConfigurations().forEach((string, config) -> config.getNetworkHandler().registerClientReceiverAndResponse());

        for (Block block : RegistryHandler.getTransparentBlocks()) {
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout());
        }
    }
}
