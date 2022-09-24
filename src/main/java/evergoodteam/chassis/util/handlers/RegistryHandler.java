package evergoodteam.chassis.util.handlers;

import evergoodteam.chassis.client.models.ItemModelType;
import evergoodteam.chassis.util.IdentifierParser;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static evergoodteam.chassis.util.Reference.CMI;
import static org.slf4j.LoggerFactory.getLogger;

public class RegistryHandler {

    private static final Logger LOGGER = getLogger(CMI + "/H/Registry");

    public static final Map<String, List<String>> REGISTERED_BLOCKS = new HashMap<>();
    public static final Map<String, List<String>> REGISTERED_ITEMS = new HashMap<>();
    public static final Map<String, ItemModelType> ITEM_TYPES = new HashMap<>(); // Uses Identifier in String

    //region Block registration

    /**
     * Registers the provided block and its item form
     *
     * @param namespace  your modId
     * @param path       name to identify your block from other entries in the same namespace
     * @param block      your block
     * @param itemGroup  tab inside the creative inventory to add the block to
     * @param tooltipKey text shown as a tooltip under the item name
     * @see evergoodteam.chassis.objects.blocks.BlockBase
     */
    public static void registerBlockAndItem(String namespace, String path, Block block, ItemGroup itemGroup, String tooltipKey) {
        registerBlock(namespace, path, block);
        registerBlockItem(namespace, path, block, itemGroup, tooltipKey);
    }

    /**
     * Registers the provided block and its item form
     *
     * @param namespace your modId
     * @param path      name to identify your block from other entries in the same namespace
     * @param block     your block
     * @param itemGroup tab inside the creative inventory to add the block to
     * @see evergoodteam.chassis.objects.blocks.BlockBase
     */
    public static void registerBlockAndItem(String namespace, String path, Block block, ItemGroup itemGroup) {
        registerBlock(namespace, path, block);
        registerBlockItem(namespace, path, block, itemGroup);
    }

    /**
     * Registers the provided block and its item form
     *
     * @param namespace your modId
     * @param path      name to identify your block from other entries in the same namespace
     * @param block     your block
     * @see evergoodteam.chassis.objects.blocks.BlockBase
     */
    public static void registerBlockAndItem(String namespace, String path, Block block) {
        registerBlock(namespace, path, block);
        registerBlockItem(namespace, path, block);
    }

    /**
     * Registers the provided block
     *
     * @param namespace your modId
     * @param path      name to identify your block from other entries in the same namespace
     * @param block     your block
     * @see evergoodteam.chassis.objects.blocks.BlockBase
     */
    public static void registerBlock(String namespace, String path, Block block) {
        REGISTERED_BLOCKS.computeIfAbsent(namespace, k -> new ArrayList<>()).add(path);
        Registry.register(Registry.BLOCK, new Identifier(namespace, path), block);
    }

    private static void registerBlockItem(String namespace, String path, Block block, ItemGroup itemGroup, String tooltipKey) {
        registerItem(namespace, path,
                new BlockItem(block, new FabricItemSettings().group(itemGroup)) {
                    @Override
                    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
                        tooltip.add(Text.translatable(tooltipKey));
                    }
                }, false);
    }

    // TODO: [NU] count them anyway
    private static void registerBlockItem(String namespace, String path, Block block, ItemGroup itemGroup) {
        registerItem(namespace, path, new BlockItem(block, new FabricItemSettings().group(itemGroup)), false);
    }

    private static void registerBlockItem(String namespace, String path, Block block) {
        registerItem(namespace, path, new BlockItem(block, new FabricItemSettings()), false);
    }
    //endregion

    //region Item categories and registration

    /**
     * Registers the provided item, specifying that it needs a handheld model (tool model)
     *
     * @param namespace your modId
     * @param path      name to identify your item from other entries in the same namespace
     * @param item      your item
     * @see evergoodteam.chassis.objects.items.ItemBase
     */
    public static void registerHandheldItem(String namespace, String path, Item item) {
        ITEM_TYPES.put(IdentifierParser.getString(namespace, path), ItemModelType.HANDHELD);
        registerItem(namespace, path, item, true);
    }

    /**
     * Registers the provided item
     *
     * @param namespace your modId
     * @param path      name to identify your item from other entries in the same namespace
     * @param item      your item
     * @see evergoodteam.chassis.objects.items.ItemBase
     */
    public static void registerGeneratedItem(String namespace, String path, Item item) {
        ITEM_TYPES.put(IdentifierParser.getString(namespace, path), ItemModelType.GENERATED);
        registerItem(namespace, path, item, true);
    }

    private static void registerItem(String namespace, String path, Item item, Boolean count) {
        if (count) REGISTERED_ITEMS.computeIfAbsent(namespace, k -> new ArrayList<>()).add(path);
        Registry.register(Registry.ITEM, new Identifier(namespace, path), item);
    }
    //endregion
}
