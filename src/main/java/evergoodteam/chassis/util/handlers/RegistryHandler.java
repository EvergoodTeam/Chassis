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
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static evergoodteam.chassis.util.Reference.CMI;
import static org.slf4j.LoggerFactory.getLogger;

public class RegistryHandler {

    private static final Logger LOGGER = getLogger(CMI + "/H/Registry");
    public final Map<Block, RegistryKey<Block>> BLOCKS = new HashMap<>();
    public final Map<Item, RegistryKey<Item>> ITEMS = new HashMap<>();
    public final Map<ItemGroup, RegistryKey<ItemGroup>> ITEMGROUPS = new HashMap<>();
    public final String modid;

    public RegistryHandler(String modid) {
        this.modid = modid;
    }

    // ItemGroup

    public void registerItemGroup(String namespace, String path, ItemGroup itemGroup) {
        RegistryKey<ItemGroup> registryKey = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(namespace, path));
        ITEMGROUPS.put(Registry.register(Registries.ITEM_GROUP, registryKey, itemGroup), registryKey);
    }

    public void addToItemGroup(Item item, RegistryKey<ItemGroup> registryKey) {
        ItemGroupEvents.modifyEntriesEvent(registryKey).register(content -> {
            content.add(item);
        });
    }
    //

    //region Block

    /**
     * Registers the provided block and its item form
     */
    public void registerBlockWithItem(String namespace, String path, Block block, Text tooltip) {
        registerBlock(namespace, path, block);
        registerBlockItem(namespace, path, block, tooltip);
    }

    /**
     * Registers the provided block and its item form
     */
    public void registerBlockWithItem(String namespace, String path, Block block) {
        registerBlock(namespace, path, block);
        registerBlockItem(namespace, path, block);
    }

    /**
     * Registers the provided block
     *
     * @param namespace your modId
     * @param path      name to identify your block from other entries in the same namespace
     * @param block     your block
     * @see evergoodteam.chassis.common.blocks.BlockBase
     */
    public void registerBlock(String namespace, String path, Block block) {
        RegistryKey<Block> registryKey = RegistryKey.of(RegistryKeys.BLOCK, new Identifier(namespace, path));
        BLOCKS.put(Registry.register(Registries.BLOCK, registryKey, block), registryKey);
    }

    public void registerBlockItem(String namespace, String path, Block block, Text tooltip) {
        registerItem(namespace, path,
                new BlockItem(block, new FabricItemSettings()) {
                    @Override
                    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltipList, TooltipContext context) {
                        tooltipList.add(tooltip);
                    }
                });
    }

    public void registerBlockItem(String namespace, String path, Block block) {
        registerItem(namespace, path, block.asItem());
    }
    //endregion

    //region Item

    public void registerItem(String namespace, String path, Item item) {
        RegistryKey<Item> registryKey = RegistryKey.of(RegistryKeys.ITEM, new Identifier(namespace, path));
        ITEMS.put(Registry.register(Registries.ITEM, registryKey, item), registryKey);
    }
    //endregion
}
