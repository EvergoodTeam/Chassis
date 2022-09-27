package evergoodteam.chassis;

import evergoodteam.chassis.client.models.BlockModelType;
import evergoodteam.chassis.client.models.ItemModelType;
import evergoodteam.chassis.configs.options.BooleanOption;
import evergoodteam.chassis.configs.options.DoubleSliderOption;
import evergoodteam.chassis.configs.options.IntegerSliderOption;
import evergoodteam.chassis.configs.options.StringSetOption;
import evergoodteam.chassis.objects.assets.RecipeJson;
import evergoodteam.chassis.objects.blocks.BlockBase;
import evergoodteam.chassis.objects.blocks.PillarBase;
import evergoodteam.chassis.objects.groups.ItemGroupBase;
import evergoodteam.chassis.client.models.ModelBundler;
import evergoodteam.chassis.objects.items.ItemBase;
import evergoodteam.chassis.objects.recipes.RecipeBundler;
import evergoodteam.chassis.util.handlers.RegistryHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static evergoodteam.chassis.util.Reference.*;

public class Chassis implements ModInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CMI);

    @Override
    public void onInitialize() {

        LOGGER.info("Initializing Chassis");

        testFeatures();
    }

    static final List<Block> BLOCKS = new ArrayList<>();
    static final List<Item> ITEMS = new ArrayList<>();
    static final Block TEST_BLOCK = new BlockBase(BLOCKS, FabricBlockSettings.of(Material.METAL).requiresTool().hardness(1.5f).resistance(6.0f).sounds(BlockSoundGroup.METAL).nonOpaque()).setTransparent();
    static final Item TEST_ITEM = new ItemBase(new FabricItemSettings().maxCount(65)).addToList(ITEMS);

    static final Block BIRCH = new PillarBase(FabricBlockSettings.copyOf(Blocks.BIRCH_LOG));

    private static void testFeatures() {

        // Configs
        CHASSIS_CONFIGS.addBooleanProperty(CHASSIS_RESOURCES.getHiddenBoolean().hideDefault(false))
                .addBooleanProperty(new BooleanOption("boolean", false, Text.literal("Boolean"), Text.literal("Boolean Tooltip"))
                        .setComment("Boolean comment"))
                .addDoubleProperty(new DoubleSliderOption("double", 3, 8, 4.5, Text.literal("Double Slider"), Text.literal("Double Tooltip"))
                        .setComment("Double comment"))
                .addIntegerSliderProperty(new IntegerSliderOption("integerSlider", 3, 8, 5, Text.literal("Integer Slider"), Text.literal("Integer Tooltip")))
                .addStringProperty(new StringSetOption("string", "test2", Set.of("test1", "test2", "test3", "test4"), Text.literal("String Set"), Text.literal("String Set Tooltip")))
                .registerProperties();

        // Blocks/Items
        ItemGroup testGroup = ItemGroupBase.createItemGroup("chassis", "testgroup", TEST_BLOCK);
        RegistryHandler.registerBlockAndItem("chassis", "testblock", TEST_BLOCK, testGroup);
        RegistryHandler.registerHandheldItem("chassis", "testitem", TEST_ITEM);
        RegistryHandler.registerBlockAndItem("chassis", "birch", BIRCH, testGroup);

        // Asset injection
        ModelBundler modelBundler = new ModelBundler(MODID)
                .addItem(new Identifier(MODID, "testitem"), ItemModelType.HANDHELD)
                .addBlock(new Identifier(MODID, "testblock"), BlockModelType.ALL)
                .addItem(new Identifier(MODID, "testblock"), ItemModelType.BLOCK)
                .addColumn(new Identifier(MODID, "birch"))
                .addItem(new Identifier(MODID, "birch"), ItemModelType.BLOCK);

        RecipeBundler recipeBundler = new RecipeBundler(MODID)
                .addRecipe(MODID, "testrecipe", RecipeJson.create3x3RecipeJson("item", new Identifier(MODID, "testitem"), new Identifier(MODID, "testblock"), 1))
                .addRecipe(MODID, "testrecipeshapeless", RecipeJson.createShapelessRecipeJson("item", new Identifier(MODID, "testitem"), new Identifier(MODID, "testblock"), 9));

        // ResourcePack
        CHASSIS_RESOURCES.createGlobalTag("testblock")
                .createBlockstate("testblock")
                //.createBlockModels("testblock", "testblock", "all")
                //.createBlockDropLootTable("testblock")
                .createGemOreDropLootTable("testblock", MODID, "testitem")
                .createRequiredToolTag("pickaxe", new String[]{"testblock"})
                .createMiningLevelTag("stone", new String[]{"testblock"})
                .createTexture(true, "https://i.imgur.com/BAStRdD.png", "testblock") // Truly a legendary URL

                //.createItemModel("testitem", "testitem")
                .createTexture(false, "https://i.imgur.com/BAStRdD.png", "testitem")

                .createBlockDropLootTable("birch")
                .createColumnBlockstate("birch")
                //.createBlockModel("birch", "birch", "column")

                .createLang("en_us", new HashMap<>() {{
                    put("block.chassis.testblock", "Test Block");
                    put("item.chassis.testitem", "Test Item");
                    put("itemGroup.chassis.testgroup", "Test Group");
                }})

                .createAdvancement(new Identifier("chassis", "testblock"), null, "Chassis Test", "Chassis is a library", new Identifier("chassis", "textures/block/testblock.png"), "challenge",
                        true, true, false,
                        null,
                        "chassis_test", new Identifier("minecraft:inventory_changed"), new Identifier[]{new Identifier("chassis", "testblock")}, null);
    }
}
