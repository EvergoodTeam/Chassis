package evergoodteam.chassis.util.handlers;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class NetworkHandler {

    private static final NetworkHandler INSTANCE = new NetworkHandler();
    private final Map<Identifier, ClientPlayNetworking.PlayChannelHandler> clientReceiver = new HashMap<>();
    private final Map<Identifier, ServerPlayNetworking.PlayChannelHandler> serverReceiver = new HashMap<>();

    public static NetworkHandler getInstance() {
        return INSTANCE;
    }

    private NetworkHandler() {
    }

    public void registerServerReceiver(Identifier channelName, ServerPlayNetworking.PlayChannelHandler channelHandler) {
        serverReceiver.put(channelName, channelHandler);
    }

    public void registerClientReceiver(Identifier channelName, ClientPlayNetworking.PlayChannelHandler channelHandler) {
        clientReceiver.put(channelName, channelHandler);
    }

    public Map<Identifier, ServerPlayNetworking.PlayChannelHandler> getServerReceivers() {
        return serverReceiver;
    }

    public Map<Identifier, ClientPlayNetworking.PlayChannelHandler> getClientReceivers() {
        return clientReceiver;
    }
}
