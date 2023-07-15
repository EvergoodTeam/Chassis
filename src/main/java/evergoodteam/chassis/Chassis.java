package evergoodteam.chassis;

import evergoodteam.chassis.util.handlers.NetworkHandler;
import lombok.extern.log4j.Log4j2;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static evergoodteam.chassis.util.Reference.CMI;

@Log4j2
public class Chassis implements ModInitializer {

    static final Logger LOGGER = LoggerFactory.getLogger(CMI);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Chassis");

        NetworkHandler.getInstance().getServerReceivers().forEach(ServerPlayNetworking::registerGlobalReceiver);

        ChassisTestFeatures.init();
        //ChassisTestFeatures.initProviderRegistry();
    }
}
