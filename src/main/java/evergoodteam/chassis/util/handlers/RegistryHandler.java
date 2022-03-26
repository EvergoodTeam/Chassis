package evergoodteam.chassis.util.handlers;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class RegistryHandler {

    public static void registerBlock(String namespace, String path, Block block, ItemGroup itemGroup){

        Registry.register(Registry.BLOCK, new Identifier(namespace, path), block);
        Registry.register(Registry.ITEM, new Identifier(namespace, path), new BlockItem(block, new FabricItemSettings().group(itemGroup)));
    }
}
