package evergoodteam.chassis.client;

import evergoodteam.chassis.client.gui.text.GradientTextRenderer;
import evergoodteam.chassis.util.handlers.NetworkHandler;
import evergoodteam.chassis.util.handlers.RegistryHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
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

        NetworkHandler.getInstance().getClientReceivers().forEach(ClientPlayNetworking::registerGlobalReceiver);

        for (Block block : RegistryHandler.getTransparentBlocks()) {
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout());
        }

        RegistryHandler.getConfigurations().forEach((string, config) -> config.networking.registerClientReceiver());
    }
}
