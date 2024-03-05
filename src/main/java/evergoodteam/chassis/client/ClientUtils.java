package evergoodteam.chassis.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ClientUtils {

    public static void copyToClipboard(MinecraftClient client, String string){
        client.keyboard.setClipboard(string);
    }

    public static Entity getCameraEntity(MinecraftClient client){
        Entity entity = client.getCameraEntity();
        return entity != null ? entity : client.player;
    }

    @Nullable
    public static World getWorld(MinecraftClient client) {
        IntegratedServer server = client.getServer();
        return client.world != null && server != null ? server.getWorld(client.world.getRegistryKey()) : client.world;
    }
}
