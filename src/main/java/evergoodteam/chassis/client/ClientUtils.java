package evergoodteam.chassis.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public class ClientUtils {

    public static void copyToClipboard(MinecraftClient client, String string){
        client.keyboard.setClipboard(string);
    }
}
