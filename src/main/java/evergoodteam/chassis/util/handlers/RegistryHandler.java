package evergoodteam.chassis.util.handlers;

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
import java.util.List;

public class RegistryHandler {

    public static void registerBlock(String namespace, String path, Block block){

        Registry.register(Registry.BLOCK, new Identifier(namespace, path), block);
    }

    public static void registerBlockItem(String namespace, String path, Block block, ItemGroup itemGroup){

        Registry.register(Registry.ITEM, new Identifier(namespace, path), new BlockItem(block, new FabricItemSettings().group(itemGroup)));
    }
/*
    private static Block registerBlock(String namespace, String path, Block block, ItemGroup group, String tooltipKey) {

        registerBlockItem(namespace, path, block, group, tooltipKey);
        return Registry.register(Registry.BLOCK, new Identifier(namespace, path), block);
    }
*/
    private static void registerBlockItem(String namespace, String path, Block block, ItemGroup itemGroup, String tooltipKey) {
        Registry.register(Registry.ITEM, new Identifier(namespace, path),
            new BlockItem(block, new FabricItemSettings().group(itemGroup)) {
                @Override
                public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
                    tooltip.add(new TranslatableText(tooltipKey));
                }
            });
    }
/*
    private static Block registerBlock(String namespace, String path, Block block, ItemGroup group) {
        registerBlockItem(namespace, path, block, group);
        return Registry.register(Registry.BLOCK, new Identifier(namespace, path), block);
    }

    private static Item registerBlockItem(String namespace, String path, Block block, ItemGroup group) {
        return Registry.register(Registry.ITEM, new Identifier(namespace, path), new BlockItem(block, new FabricItemSettings().group(group)));
    }*/
}
