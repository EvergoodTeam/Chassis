package evergoodteam.chassis;

import evergoodteam.chassis.configs.ConfigHandler;
import evergoodteam.chassis.objects.assets.LootJson;
import evergoodteam.chassis.objects.blocks.BlockBase;
import evergoodteam.chassis.objects.groups.ItemGroupBase;
import evergoodteam.chassis.objects.resourcepacks.ResourcePackBase;
import evergoodteam.chassis.util.handlers.ListHandler;
import evergoodteam.chassis.util.handlers.RegistryHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

import java.util.ArrayList;
import java.util.List;

import static evergoodteam.chassis.util.Reference.*;

public class Chassis implements ModInitializer {

    public static final List<Block> BLOCKS = new ArrayList<>();

    public static final Block TEST_BLOCK = new BlockBase(BLOCKS, FabricBlockSettings.of(Material.METAL).requiresTool().hardness(1.5f).resistance(6.0f).sounds(BlockSoundGroup.METAL).nonOpaque(), true);

    private static void testFeatures() {

        CHASSIS_CONFIGS.options.put("hideResourcePack", false);
        CHASSIS_CONFIGS.addProperties();

        ItemGroupBase testGroup = new ItemGroupBase("chassis", "testgroup", TEST_BLOCK);
        RegistryHandler.registerBoth("chassis", "testblock", TEST_BLOCK);

        //ListHandler.addAssetInjection(MODID);

        ListHandler.addLoot(MODID, "testblock", LootJson.createBlockBreakLootJson("chassis", "testblock"));

        ResourcePackBase chassisRP = ResourcePackBase.getResourcePack("chassis", "chassis");

        chassisRP.createGlobalTag("testblock")
                .createBlockstate("testblock")
                .createBlockModels("testblock", "testblock", "all")
                .createBlockDropLootTable("testblock")
                .createRequiredToolTag("pickaxe", new String[]{"testblock"})
                .createMiningLevelTag("stone", new String[]{"testblock"})
                .createTexture(true, "https://i.imgur.com/BAStRdD.png", "testblock"); // Truly a legendary URL

        if (ConfigHandler.getBooleanOption(CHASSIS_CONFIGS, "hideResourcePack", false)) chassisRP.hide();
    }

    @Override
    public void onInitialize() {

        LOGGER.info("Starting up");

        testFeatures();
    }
}
