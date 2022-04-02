package evergoodteam.chassis.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;;

import static evergoodteam.chassis.util.Reference.*;

@Environment(EnvType.CLIENT)
public class ChassisClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        if(!BLOCKS.isEmpty()) LOGGER.info("Found " + BLOCKS.size() + " blocks to add, of which " + COLUMNS.size() + " types are columns");

    }
}
