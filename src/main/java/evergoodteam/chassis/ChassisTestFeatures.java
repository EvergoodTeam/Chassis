package evergoodteam.chassis;
/*
import evergoodteam.chassis.client.gui.text.GradientText;
import evergoodteam.chassis.common.block.BlockBase;
import evergoodteam.chassis.common.block.PillarBase;
import evergoodteam.chassis.common.group.ItemGroupBase;
import evergoodteam.chassis.common.item.ItemBase;
import evergoodteam.chassis.config.option.*;
import evergoodteam.chassis.datagen.providers.*;
import evergoodteam.chassis.util.handlers.RegistryHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.Models;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.EnchantWithLevelsLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static evergoodteam.chassis.util.Reference.*;
*/
public class ChassisTestFeatures {
/*
    private static final Logger LOGGER = LoggerFactory.getLogger(CMI + "/Testing");
    static final List<Block> BLOCKS = new ArrayList<>();
    static final List<Item> ITEMS = new ArrayList<>();

    static final Block TEST_BLOCK = new BlockBase(FabricBlockSettings.create().requiresTool().hardness(1.5f).resistance(6.0f).sounds(BlockSoundGroup.METAL).nonOpaque())
            .addTo(BLOCKS)
            .setTransparent();
    static final Item TEST_ITEM = new ItemBase(new FabricItemSettings().maxCount(65))
            .addTo(ITEMS);
    static final Block BIRCH = new PillarBase(FabricBlockSettings.copyOf(Blocks.BIRCH_LOG));
    static final ItemGroupBase TEST_GROUP = new ItemGroupBase("chassis", "testgroup", Items.ACACIA_DOOR);
    static final ItemGroupBase ITEM_GROUP_BASE = new ItemGroupBase(new Identifier("chassis", "testgrouptwo")).buildItemGroup(builder -> {
        builder.icon(() -> new ItemStack(Items.ACACIA_LEAVES))
                .displayName(Text.literal("Very much testing"));
    });

    public static void initProviderRegistry() {
        CHASSIS_RESOURCES.providerRegistry = () -> {
            CHASSIS_RESOURCES
                    .addProvider(ChassisLanguageProvider.create(CHASSIS_RESOURCES)
                            .build(translationBuilder -> {
                                translationBuilder.add(TEST_BLOCK, "Test Block");
                                translationBuilder.add(TEST_ITEM, "Test Item");
                                //translationBuilder.add(TEST_GROUP, "Test Group");
                            }))
                    .addProvider(ChassisTextureProvider.create(CHASSIS_RESOURCES)
                            .addTexture("https://i.imgur.com/BAStRdD.png", true, "testblock")
                            .addTexture("https://i.imgur.com/BAStRdD.png", false, "testitem")
                            .addTexture("https://i.imgur.com/BAStRdD.png", false, "testthingother")
                    )
                    .addProvider(ChassisLootTableProvider.create(LootContextTypes.CHEST, CHASSIS_RESOURCES)
                            .build(new Identifier("chassis", "chests/chest_chestplate"), builder -> builder
                                    .pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F))
                                            .with(ItemEntry.builder(Items.DIAMOND_CHESTPLATE))
                                            .apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(20.0F, 39.0F)))))
                            .build(new Identifier("chassis", "chests/chest_sword"), builder -> builder
                                    .pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F))
                                            .with(ItemEntry.builder(Items.DIAMOND)
                                                    .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                                            .with(ItemEntry.builder(Items.DIAMOND_SWORD))
                                            .apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(20.0F, 39.0F))))))
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
                                    .criterion("inventory_changed", InventoryChangedCriterion.Conditions.items(Items.ACACIA_BOAT)))
                            .build(new Identifier("chassis", "birchlog"), builder -> builder
                                    .display(Blocks.BIRCH_LOG, Text.literal("Birch Log"),
                                            Text.literal("description"),
                                            new Identifier("textures/gui/advancements/backgrounds/husbandry.png"),
                                            AdvancementFrame.TASK,
                                            false,
                                            true,
                                            false)
                                    .criterion("inventory_changed", InventoryChangedCriterion.Conditions.items(Items.BIRCH_LOG)))
                    )
                    .addProvider(ChassisTagProvider.create(RegistryKeys.ITEM, CHASSIS_RESOURCES)
                            .build(TagKey.of(RegistryKeys.ITEM, new Identifier("chassis:smelly_items")), builder -> builder
                                    .add(Items.SLIME_BALL)
                                    .add(Items.ROTTEN_FLESH)
                                    .addOptionalTag(ItemTags.DIRT)))
                    .addProvider(ChassisRecipeProvider.create(CHASSIS_RESOURCES)
                            .build(exporter -> {
                                RecipeProvider.offerSmelting(exporter, List.of(Items.ANDESITE), RecipeCategory.MISC, Items.ACACIA_BOAT, 0.45F, 300, "example");
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
        RegistryHandler registryHandler = new RegistryHandler(MODID);

        CHASSIS_CONFIGS.setDisplayTitle(GradientText.copyOf(Text.literal("Chassis"))
                .setColorPoints(50, "264653", "2a9d8f", "e9c46a", "f4a261", "e76f51", "264653")
                .setScrollDelay(4));
        CHASSIS_CONFIGS.getNetworkHandler().registerServerConnectionListener();
        CHASSIS_CONFIGS.getNetworkHandler().registerHandshakeReceiver();

        CategoryOption EMPTY = new CategoryOption(CHASSIS_CONFIGS, "Empty Category", "").getBuilder()
                .useFrame(true)
                .setDisplayName(Text.literal("Empty Category Red").setStyle(Style.EMPTY.withColor(Formatting.RED))).build();

        CategoryOption GENERAL = new CategoryOption(CHASSIS_CONFIGS, "Lorem ipsum dolor sit amet, consectetur adipiscing elit and some other long stuff.", "General Options").getBuilder()
                .useFrame(true)
                .setBackgroundColor(0x2B_aedcff)
                .setOutlineColor(0xB6_fe9443)
                .setDisplayName(GradientText.copyOf(Text.literal("Lorem ipsum dolor sit amet, consectetur adipiscing elit long long long stuff"))
                        .setColorPoints("fe9443", "aedcff", "dd6102", "fe9443")
                        .setScrollDelay(1)).build();

        CategoryOption TEST = new CategoryOption(CHASSIS_CONFIGS,
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                "This long description is cut because of its extensive length, which we can't fit in the provided box or the config .properties file, which means we have to make multiple lines!").getBuilder()
                .setDisplayName(GradientText.copyOf(Text.literal("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG"))
                        .setColorPoints("d00000", "ffbe0b", "70e000", "3a86ff", "3a0ca3", "ae2d68", "d00000")
                        .setScrollDelay(1)).build();

        CHASSIS_CONFIGS.addCategory(EMPTY)
                .addStringProperty(new ColorOption("color", "ff000000").getBuilder()
                        .setEnvType(EnvType.SERVER)
                        .setDisplayName(Text.literal("Test Colour")).build())
                .addBooleanProperty(
                        new BooleanOption("genericBoolean", false, Text.literal("Boolean"), Text.literal("Tooltip0")).getBuilder()
                                .setEnvType(EnvType.CLIENT).build())
                .addCategory(GENERAL.getBuilder()
                        .setComment("General options").build()
                        //.addBooleanProperty(CHASSIS_RESOURCES.getHideResourcePackProperty())
                        .addBooleanOption(new BooleanOption("boolean", false, Text.literal("Boolean"), Text.literal("Boolean Tooltip")).getBuilder()
                                .setComment("Boolean comment").build())
                        .addDoubleOption(new DoubleSliderOption("double", 3, 8, 4.5, Text.literal("Slider"), Text.literal("Double Tooltip")).getBuilder()
                                .setComment("Double comment").build())
                        .addIntegerOption(new IntegerSliderOption("integerSlider", 3, 8, 5, Text.literal("Integer Slider"), Text.literal("Integer Tooltip")).getBuilder()
                                .setComment("Integer comment").build())
                        .addStringSetOption(new StringSetOption("string", "test2", Set.of("test1", "test2", "test3", "test4"), Text.literal("String Set"), Text.literal("String Set Tooltip")).getBuilder()
                                .setComment("Hello")
                                .setTooltip(Text.literal("Not the first tooltip")).build())
                )
                .addCategory(TEST
                        .addDoubleOption(new DoubleSliderOption("second_Double", 0, 100, 53.1, Text.literal("Another Slider"), Text.empty()).getBuilder()
                                .setComment("Secondary double, should have default values here ->").build())
                        .addStringSetOption(new ColorOption("extraSpaceBelow", "ffffff"))
                )
                .registerProperties();

        // Blocks/Items

        registryHandler.registerBlockWithItem("chassis", "testblock", TEST_BLOCK);
        registryHandler.registerItem("chassis", "testitem", TEST_ITEM);
        registryHandler.registerBlockWithItem("chassis", "birch", BIRCH);

        registryHandler.registerItemGroup(TEST_GROUP);

        registryHandler.addToItemGroup(TEST_BLOCK.asItem(), TEST_GROUP);
        registryHandler.addToItemGroup(TEST_ITEM, TEST_GROUP);
        registryHandler.addToItemGroup(BIRCH.asItem(), TEST_GROUP);
    }
*/
}
