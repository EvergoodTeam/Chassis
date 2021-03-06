package evergoodteam.chassis;
/*
import evergoodteam.chassis.configs.ConfigHandler;
import evergoodteam.chassis.objects.assets.RecipeJson;
import evergoodteam.chassis.objects.blocks.BlockBase;
import evergoodteam.chassis.objects.groups.ItemGroupBase;
import evergoodteam.chassis.objects.items.ItemBase;
import evergoodteam.chassis.util.handlers.InjectionHandler;
import evergoodteam.chassis.util.handlers.RegistryHandler;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
*/

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static evergoodteam.chassis.util.Reference.*;

public class Chassis implements ModInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CMI);

    @Override
    public void onInitialize() {

        LOGGER.info("Initializing Chassis");

        //testFeatures();
    }

    /*
    static final List<Block> BLOCKS = new ArrayList<>();
    static final Block TEST_BLOCK = new BlockBase(BLOCKS, FabricBlockSettings.of(Material.METAL).requiresTool().hardness(1.5f).resistance(6.0f).sounds(BlockSoundGroup.METAL).nonOpaque(), true);
    static final Item TEST_ITEM = new ItemBase(new FabricItemSettings().maxCount(65));

    private static void testFeatures() {

        // Configs
        CHASSIS_CONFIGS.addProperty("hideResourcePack", false, "Hides the Chassis ResourcePack from the GUI")
                .addProperty("testProperty", true, "Test comment")
                .addProperty("testProperty2", true)
                .addProperty("testProperty3", true, "Test comment 3")
                .registerProperties();

        // Blocks/Items
        ItemGroup testGroup = new ItemGroupBase("chassis", "testgroup", TEST_BLOCK).group;
        RegistryHandler.registerBlockAndItem("chassis", "testblock", TEST_BLOCK, testGroup);
        RegistryHandler.registerGeneratedItem("chassis", "testitem", TEST_ITEM);

        // Asset injection
        InjectionHandler.addModelInjection(MODID);
        InjectionHandler.addRecipe(MODID, "testrecipe", RecipeJson.create3x3RecipeJson("item", new Identifier(MODID, "testitem"), new Identifier(MODID, "testblock"), 1));
        InjectionHandler.addRecipe(MODID, "testrecipeshapeless", RecipeJson.createShapelessRecipeJson("item", new Identifier(MODID, "testitem"), new Identifier(MODID, "testblock"), 9));

        // Resourcepack injection
        CHASSIS_RESOURCES.createGlobalTag("testblock")
                .createBlockstate("testblock")
                //.createBlockModels("testblock", "testblock", "all")
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
    */
}
