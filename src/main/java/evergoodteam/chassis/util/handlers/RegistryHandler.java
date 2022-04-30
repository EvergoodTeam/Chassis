package evergoodteam.chassis.util.handlers;

import evergoodteam.chassis.util.IdentifierParser;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistryHandler {

    public static final Map<String, List<String>> REGISTERED_BLOCKS = new HashMap<>();
    public static final Map<String, List<String>> REGISTERED_ITEMS = new HashMap<>();
    public static final Map<String, List<String>> ITEM_TYPES = new HashMap<>(); // Uses Identifier in String

    static {
        ITEM_TYPES.put("generated", new ArrayList<String>());
        ITEM_TYPES.put("handheld", new ArrayList<String>());
    }

    //region Block Registration

    /**
     * Registers the Block and its Item version of the provided Block
     *
     * @param namespace  your ModId
     * @param path       name to identify your Block from other entries in the same namespace
     * @param block      your Block
     * @param itemGroup  tab inside the Creative Inventory to which add the Block
     * @param tooltipKey text shown as a Tooltip under the Item
     * @see evergoodteam.chassis.objects.blocks.BlockBase
     */
    public static void registerBlockAndItem(String namespace, String path, Block block, ItemGroup itemGroup, String tooltipKey) {

        registerBlock(namespace, path, block);
        registerBlockItem(namespace, path, block, itemGroup, tooltipKey);
    }

    /**
     * Registers the Block and its Item version of the provided Block
     *
     * @param namespace your ModId
     * @param path      name to identify your Block from other entries in the same namespace
     * @param block     your Block
     * @param itemGroup tab inside the Creative Inventory to which add the Block
     * @see evergoodteam.chassis.objects.blocks.BlockBase
     */
    public static void registerBlockAndItem(String namespace, String path, Block block, ItemGroup itemGroup) {

        registerBlock(namespace, path, block);
        registerBlockItem(namespace, path, block, itemGroup);
    }

    /**
     * Registers the Block and its Item version of the provided Block
     *
     * @param namespace your ModId
     * @param path      name to identify your Block from other entries in the same namespace
     * @param block     your Block
     * @see evergoodteam.chassis.objects.blocks.BlockBase
     */
    public static void registerBlockAndItem(String namespace, String path, Block block) {
        registerBlock(namespace, path, block);
        registerBlockItem(namespace, path, block);
    }

    /**
     * Registers a Block
     *
     * @param namespace your ModId
     * @param path      name to identify your Block from other entries in the same namespace
     * @param block     your Block
     * @see evergoodteam.chassis.objects.blocks.BlockBase
     */
    public static void registerBlock(String namespace, String path, Block block) {
        REGISTERED_BLOCKS.computeIfAbsent(namespace, k -> new ArrayList<>()).add(path);
        Registry.register(Registry.BLOCK, new Identifier(namespace, path), block);
    }

    private static void registerBlockItem(String namespace, String path, Block block, ItemGroup itemGroup, String tooltipKey) {
        Registry.register(Registry.ITEM, new Identifier(namespace, path),
                new BlockItem(block, new FabricItemSettings().group(itemGroup)) {
                    @Override
                    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
                        tooltip.add(new TranslatableText(tooltipKey));
                    }
                });
    }

    private static void registerBlockItem(String namespace, String path, Block block, ItemGroup itemGroup) {
        Registry.register(Registry.ITEM, new Identifier(namespace, path), new BlockItem(block, new FabricItemSettings().group(itemGroup)));
    }

    private static void registerBlockItem(String namespace, String path, Block block) {
        Registry.register(Registry.ITEM, new Identifier(namespace, path), new BlockItem(block, new FabricItemSettings()));
    }
    //endregion

    //region Item Categorization and Registration

    /**
     * Registers a Tool
     *
     * @param namespace your ModId
     * @param path      name to identify your Item from other entries in the same namespace
     * @param item      your Item
     * @see evergoodteam.chassis.objects.items.ItemBase
     */
    public static void registerHandheldItem(String namespace, String path, Item item) {
        ITEM_TYPES.get("handheld").add(IdentifierParser.getString(namespace, path));
        registerItem(namespace, path, item);
    }

    /**
     * Registers a generic Item
     *
     * @param namespace your ModId
     * @param path      name to identify your Item from other entries in the same namespace
     * @param item      your Item
     * @see evergoodteam.chassis.objects.items.ItemBase
     */
    public static void registerGeneratedItem(String namespace, String path, Item item) {
        ITEM_TYPES.get("generated").add(IdentifierParser.getString(namespace, path));
        registerItem(namespace, path, item);
    }

    private static void registerItem(String namespace, String path, Item item) {
        REGISTERED_ITEMS.computeIfAbsent(namespace, k -> new ArrayList<>()).add(path);
        Registry.register(Registry.ITEM, new Identifier(namespace, path), item);
    }
    //endregion
}
