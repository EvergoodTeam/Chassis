package evergoodteam.chassis;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Chassis implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger("Chassis");

    @Override
    public void onInitialize() {

        LOGGER.info("Starting up, should get a friend calling anytime now");
    }
}
