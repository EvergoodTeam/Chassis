package evergoodteam.chassis.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;;

import static evergoodteam.chassis.objects.blocks.BlockBase.*;
import static evergoodteam.chassis.objects.items.ItemBase.*;
import static evergoodteam.chassis.util.Reference.*;

@Environment(EnvType.CLIENT)
public class ChassisClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        for(Block block : TRANSPARENT){
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout());
        }

        if(b > 0) LOGGER.info("Found {} Block(s) to register", b);
        if(!COLUMNS.isEmpty()) LOGGER.info("Found {} Column(s)", COLUMNS.size());
        if(!ITEMS.isEmpty()) LOGGER.info("Found {} Item(s) to register", ITEMS.size());

    }
}
