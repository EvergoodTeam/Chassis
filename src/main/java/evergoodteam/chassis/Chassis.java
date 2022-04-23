package evergoodteam.chassis;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

import evergoodteam.chassis.objects.blocks.BlockBase;
import evergoodteam.chassis.util.handlers.RegistryHandler;
import evergoodteam.chassis.objects.assets.LootJson;
import evergoodteam.chassis.objects.groups.ItemGroupBase;
import evergoodteam.chassis.objects.resourcepacks.ResourcePackBase;
import evergoodteam.chassis.util.handlers.ListHandler;
import evergoodteam.chassis.configs.ConfigHandler;

import static evergoodteam.chassis.configs.ConfigBase.*;
import static evergoodteam.chassis.util.Reference.*;

public class Chassis implements ModInitializer {

    public static final Block TEST_BLOCK = new BlockBase(FabricBlockSettings.of(Material.METAL).requiresTool().hardness(1.5f).resistance(6.0f).sounds(BlockSoundGroup.METAL).nonOpaque(), true);

    @Override
    public void onInitialize() {

        LOGGER.info("Starting up");

        //testFeatures();
    }

/*
    private static void testFeatures(){

        CHASSIS_CONFIGS.OPTIONS.put("hideResourcePack", false);
        CHASSIS_CONFIGS.addProperties();

        ItemGroupBase testGroup = new ItemGroupBase("chassis", "testgroup", TEST_BLOCK);
        RegistryHandler.registerBoth("chassis", "testblock", TEST_BLOCK);

        //ListHandler.addAssetInjection(MODID);

        ListHandler.addLoot(MODID, "testblock", LootJson.createBlockBreakLootJson("chassis", "testblock"));

        ResourcePackBase chassisRP = ResourcePackBase.RESOURCE_PACKS.get("chassis");

        chassisRP.createGlobalTag("testblock")
                .createBlockstate("testblock")
                .createBlockModels("testblock", "testblock", "all")
                .createBlockDropLootTable("testblock")
                .createRequiredToolTag("pickaxe", new String[]{"testblock"})
                .createMiningLevelTag("stone", new String[]{"testblock"})
                .createTexture(true, "https://i.imgur.com/BAStRdD.png", "testblock"); // Truly a legendary URL

        if(ConfigHandler.getBooleanOption(CHASSIS_CONFIGS, "hideResourcePack", false)) chassisRP.hide();
    }
*/

}
