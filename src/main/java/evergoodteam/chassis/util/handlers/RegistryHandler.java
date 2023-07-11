package evergoodteam.chassis.util.handlers;


import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
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

    // ItemGroup

    public static void addToItemGroup(Item item, RegistryKey<ItemGroup> registryKey) {
        ItemGroupEvents.modifyEntriesEvent(registryKey).register(content -> {
            content.add(item);
        });
    }
    //

    //region Block registration

    /**
     * Registers the provided block and its item form
     *
     * @param namespace  your modId
     * @param path       name to identify your block from other entries in the same namespace
     * @param block      your block
     * @param tooltipKey text shown as a tooltip under the item name
     * @see evergoodteam.chassis.objects.blocks.BlockBase
     */
    public static void registerBlockAndItem(String namespace, String path, Block block, String tooltipKey) {
        registerBlock(namespace, path, block);
        registerBlockItem(namespace, path, block, tooltipKey);
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
        Registry.register(Registries.BLOCK, new Identifier(namespace, path), block);
    }

    private static void registerBlockItem(String namespace, String path, Block block, String tooltipKey) {
        registerItem(namespace, path,
                new BlockItem(block, new FabricItemSettings()) {
                    @Override
                    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
                        tooltip.add(Text.translatable(tooltipKey));
                    }
                }, false);
    }

    private static void registerBlockItem(String namespace, String path, Block block) {
        registerItem(namespace, path, new BlockItem(block, new FabricItemSettings()), false);
    }
    //endregion

    //region Item categories and registration

    public static void registerItem(String namespace, String path, Item item) {
        registerItem(namespace, path, item, true);
    }

    private static void registerItem(String namespace, String path, Item item, Boolean count) {
        if (count) REGISTERED_ITEMS.computeIfAbsent(namespace, k -> new ArrayList<>()).add(path);
        Registry.register(Registries.ITEM, new Identifier(namespace, path), item);
    }
    //endregion
}
