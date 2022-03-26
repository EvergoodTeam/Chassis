package evergoodteam.chassis;


import evergoodteam.chassis.objects.assets.LootJson;
import evergoodteam.chassis.objects.loot.LootBase;
import net.fabricmc.api.ModInitializer;

import static evergoodteam.chassis.util.Reference.LOGGER;


public class Chassis implements ModInitializer {

    @Override
    public void onInitialize() {

        LOGGER.info("Starting up, should get a friend calling anytime now...");

        //LOGGER.info(LootJson.createBlockBreakLootJson("compressor:compressed_basalt"));

        //LootBase.addLoot("compressor", "block/dirt", LootJson.createBlockBreakLootJson("minecraft:diamond"));
    }
}
