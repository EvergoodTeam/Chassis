package evergoodteam.chassis;

import evergoodteam.chassis.configs.ConfigHandler;
import evergoodteam.chassis.objects.assets.LootJson;
import evergoodteam.chassis.objects.blocks.BlockBase;
import evergoodteam.chassis.objects.groups.ItemGroupBase;
import evergoodteam.chassis.objects.items.ItemBase;
import evergoodteam.chassis.util.handlers.ListHandler;
import evergoodteam.chassis.util.handlers.RegistryHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static evergoodteam.chassis.util.Reference.*;

public class Chassis implements ModInitializer {

    static final List<Block> BLOCKS = new ArrayList<>();
    static final Block TEST_BLOCK = new BlockBase(BLOCKS, FabricBlockSettings.of(Material.METAL).requiresTool().hardness(1.5f).resistance(6.0f).sounds(BlockSoundGroup.METAL).nonOpaque(), true);
    static final Item TEST_ITEM = new ItemBase(new FabricItemSettings().maxCount(65));

    private static void testFeatures() {

        CHASSIS_CONFIGS.options.put("hideResourcePack", false);
        CHASSIS_CONFIGS.addProperties();

        ItemGroupBase testGroup = new ItemGroupBase("chassis", "testgroup", TEST_BLOCK);
        RegistryHandler.registerBlockAndItem("chassis", "testblock", TEST_BLOCK);
        RegistryHandler.registerItem("chassis", "testitem", TEST_ITEM);

        //ListHandler.addAssetInjection(MODID);

        ListHandler.addLoot(MODID, "testblock", LootJson.createBlockBreakLootJson("chassis", "testblock"));

        CHASSIS_RESOURCES.createGlobalTag("testblock") // TODO: Make fast lock for each
                        .createBlockstate("testblock")
                        .createBlockModels("testblock", "testblock", "all")
                        .createBlockDropLootTable("testblock")
                        .createRequiredToolTag("pickaxe", new String[]{"testblock"})
                        .createMiningLevelTag("stone", new String[]{"testblock"})
                        .createTexture(true, "https://i.imgur.com/BAStRdD.png", "testblock") // Truly a legendary URL

                        .createItemModel("testitem", "testitem")
                        .createTexture(false, "https://i.imgur.com/BAStRdD.png", "testitem")

                        .createLang("en_us", new HashMap<>() {{
                            put("block.chassis.testblock", "Test Block");
                            put("item.chassis.testitem", "Test Item");
                            put("itemGroup.chassis.testgroup", "Test Group");
                        }});

        if (ConfigHandler.getBooleanOption(CHASSIS_CONFIGS, "hideResourcePack", false)) CHASSIS_RESOURCES.hide();
    }

    @Override
    public void onInitialize() {

        LOGGER.info("Starting up");

        testFeatures();
    }
}
