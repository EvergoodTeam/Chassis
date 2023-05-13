package evergoodteam.chassis;

import evergoodteam.chassis.client.gui.text.GradientText;
import evergoodteam.chassis.configs.options.*;
import evergoodteam.chassis.datagen.providers.*;
import evergoodteam.chassis.objects.blocks.BlockBase;
import evergoodteam.chassis.objects.blocks.PillarBase;
import evergoodteam.chassis.objects.groups.ItemGroupBase;
import evergoodteam.chassis.objects.items.ItemBase;
import evergoodteam.chassis.util.handlers.RegistryHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.data.client.Models;
import net.minecraft.data.server.RecipeProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.EnchantWithLevelsLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static evergoodteam.chassis.util.Reference.*;

public class ChassisTestFeatures {
/*
    private static final Logger LOGGER = LoggerFactory.getLogger(CMI + "/Testing");
    static final List<Block> BLOCKS = new ArrayList<>();
    static final List<Item> ITEMS = new ArrayList<>();
    static final Block TEST_BLOCK = new BlockBase(FabricBlockSettings.of(Material.METAL).requiresTool().hardness(1.5f).resistance(6.0f).sounds(BlockSoundGroup.METAL).nonOpaque())
            .addTo(BLOCKS)
            .setTransparent();
    static final Item TEST_ITEM = new ItemBase(new FabricItemSettings().maxCount(65))
            .addTo(ITEMS);
    static final Block BIRCH = new PillarBase(FabricBlockSettings.copyOf(Blocks.BIRCH_LOG));
    static final ItemGroup TEST_GROUP = ItemGroupBase.createItemGroup("chassis", "testgroup", TEST_BLOCK);


    public static void initProviderRegistry() {
        CHASSIS_RESOURCES.providerRegistry = () -> {
            CHASSIS_RESOURCES
                    .addProvider(ChassisLanguageProvider.create(CHASSIS_RESOURCES)
                            .build(translationBuilder -> {
                                translationBuilder.add(TEST_BLOCK, "Test Block");
                                translationBuilder.add(TEST_ITEM, "Test Item");
                                translationBuilder.add(TEST_GROUP, "Test Group");
                            }))
                    .addProvider(ChassisTextureProvider.create(CHASSIS_RESOURCES)
                            .addTexture("https://i.imgur.com/BAStRdD.png", true, "testblock")
                            .addTexture("https://i.imgur.com/BAStRdD.png", false, "testitem")
                    )
                    .addProvider(ChassisLootTableProvider.create(LootContextTypes.CHEST, CHASSIS_RESOURCES)
                            .build(new Identifier("chassis", "chests/chest_chestplate"), builder -> builder
                                    .pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F))
                                            .with(ItemEntry.builder(Items.DIAMOND_CHESTPLATE))
                                            .apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(20.0F, 39.0F))))
                            )
                            .build(new Identifier("chassis", "chests/chest_sword"), builder -> builder
                                    .pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F))
                                            .with(ItemEntry.builder(Items.DIAMOND)
                                                    .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                                            .with(ItemEntry.builder(Items.DIAMOND_SWORD))
                                            .apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(20.0F, 39.0F))))
                            )
                    )
                    .addProvider(ChassisBlockLootTableProvider.create(CHASSIS_RESOURCES)
                            .buildBlock(TEST_BLOCK.getLootTableId(), builder -> builder
                                    .addDrop(TEST_BLOCK))
                            .buildBlock(BIRCH.getLootTableId(), builder -> builder.addDrop(BIRCH)))
                    .addProvider(ChassisAdvancementProvider.create(CHASSIS_RESOURCES)
                            .build(new Identifier("chassis", "acaciaboat"), builder -> builder
                                    .display(Items.ACACIA_BOAT, Text.literal("Acacia Boat"),
                                            Text.literal("description"),
                                            new Identifier("textures/gui/advancements/backgrounds/husbandry.png"),
                                            AdvancementFrame.TASK,
                                            false,
                                            true,
                                            false)
                                    .criterion("inventory_changed", InventoryChangedCriterion.Conditions.items(Items.ACACIA_BOAT))
                            )
                            .build(new Identifier("chassis", "birchlog"), builder -> builder
                                    .display(Blocks.BIRCH_LOG, Text.literal("Birch Log"),
                                            Text.literal("description"),
                                            new Identifier("textures/gui/advancements/backgrounds/husbandry.png"),
                                            AdvancementFrame.TASK,
                                            false,
                                            true,
                                            false)
                                    .criterion("inventory_changed", InventoryChangedCriterion.Conditions.items(Items.BIRCH_LOG))
                            )
                    )
                    .addProvider(ChassisTagProvider.create(Registry.ITEM, CHASSIS_RESOURCES)
                            .build(TagKey.of(Registry.ITEM_KEY, new Identifier("chassis:smelly_items")), builder -> builder
                                    .add(Items.SLIME_BALL)
                                    .add(Items.ROTTEN_FLESH)
                                    .addOptionalTag(ItemTags.DIRT))
                    )
                    .addProvider(ChassisRecipeProvider.create(CHASSIS_RESOURCES)
                            .build(exporter -> {
                                RecipeProvider.offerSmelting(exporter, List.of(Items.ANDESITE), Items.ACACIA_BOAT, 0.45F, 300, "example");
                            })
                            .build(exporter -> {
                                RecipeProvider.offerShapelessRecipe(exporter, Items.ACACIA_BOAT, Items.ANDESITE, null, 15);
                            }))
                    .addProvider(ChassisModelProvider.create(CHASSIS_RESOURCES)
                            .buildBlock(consumer -> {
                                consumer.registerSimpleCubeAll(TEST_BLOCK);
                                consumer.registerLog(BIRCH).log(BIRCH);
                            })
                            .buildItem(consumer -> {
                                consumer.register(TEST_ITEM, Models.GENERATED);
                            })
                    )
                    .runProviders();
        };
    }

    public static void init() {

        CHASSIS_CONFIGS.setDisplayTitle(GradientText.literal("Chassis")
                .setColorPoints(50, "264653", "2a9d8f", "e9c46a", "f4a261", "e76f51", "264653")
                .setScrollDelay(4));
        CHASSIS_CONFIGS.networking.registerJoinListener();
        CHASSIS_CONFIGS.networking.registerServerReceiver();

        CategoryOption EMPTY = new CategoryOption(CHASSIS_CONFIGS, "Empty Category", "");

        CategoryOption GENERAL = new CategoryOption(CHASSIS_CONFIGS, "Lorem ipsum dolor sit amet, consectetur adipiscing elit and some other long stuff.", "General Options")
                .setDisplayName(GradientText.literal("Lorem ipsum dolor sit amet, consectetur adipiscing elit and some other long stuff.")
                        .setColorPoints("f72585", "7209b7", "3a0ca3", "4361ee", "4cc9f0", "f72585")
                        .setScrollDelay(4));
        CategoryOption TEST = new CategoryOption(CHASSIS_CONFIGS, "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", "This long description is cut because of its extensive length, which we can't fit in the provided box or the config .properties file, which means we have to make multiple lines!")
                .setDisplayName(GradientText.literal("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
                        .setColorPoints(200, "d00000", "ffbe0b", "70e000", "3a86ff", "3a0ca3", "ae2d68", "d00000")
                        .setScrollDelay(1));

        CHASSIS_CONFIGS.addCategory(EMPTY)
                .addBooleanProperty(
                        new BooleanOption("genericBoolean",
                                false,
                                Text.literal("Boolean"),
                                Text.literal("Tooltip0"))
                                .setEnvType(EnvType.CLIENT))
                .addCategory(GENERAL
                        .setComment("General options")
                        .addBooleanProperty(CHASSIS_RESOURCES.getHideResourcePackProperty())
                        .addBooleanProperty(new BooleanOption("boolean", false, Text.literal("Boolean"), Text.literal("Boolean Tooltip"))
                                .setComment("Boolean comment"))
                        .addDoubleProperty(new DoubleSliderOption("double", 3, 8, 4.5, Text.literal("Slider"), Text.literal("Double Tooltip"))
                                .setComment("Double comment"))
                        .addIntegerSliderProperty(new IntegerSliderOption("integerSlider", 3, 8, 5, Text.literal("Integer Slider"), Text.literal("Integer Tooltip"))
                                .setComment("Integer comment"))
                        .addStringProperty(new StringSetOption("string", "test2", Set.of("test1", "test2", "test3", "test4"), Text.literal("String Set"), Text.literal("String Set Tooltip")))
                )
                .addCategory(TEST
                        .addDoubleProperty(new DoubleSliderOption("second_Double", 0, 100, 53.1, Text.literal("Another Slider"), Text.empty())
                                .setComment("Secondary double, should have default values here ->"))
                )
                .registerProperties();

        // Blocks/Items
        RegistryHandler.registerBlockAndItem("chassis", "testblock", TEST_BLOCK, TEST_GROUP);
        RegistryHandler.registerHandheldItem("chassis", "testitem", TEST_ITEM);
        RegistryHandler.registerBlockAndItem("chassis", "birch", BIRCH, TEST_GROUP);
    }*/
}
