package evergoodteam.chassis.config;

import evergoodteam.chassis.common.Result;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static evergoodteam.chassis.util.Reference.CMI;

public class ConfigNetworking {

    private static final Logger LOGGER = LoggerFactory.getLogger(CMI + "/C/Network");
    public final ConfigBase config;
    public final String namespace;
    private boolean enabled = true;

    public ConfigNetworking(ConfigBase config) {
        this.config = config;
        this.namespace = config.namespace;
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    public void registerClientReceiver() {
        if (enabled)
            ClientPlayNetworking.registerGlobalReceiver(new Identifier(this.config.namespace, "sync"), (client, handler, buf, responseSender) -> {

                String requestNamespace = buf.readString();
                String request = buf.readString();

                client.execute(() -> {
                    String raw = ConfigBase.getConfig(requestNamespace).getHandler().getCommonOptions();
                    //LOGGER.info("RAW: {}", raw);
                    //LOGGER.info("RECEIVED: {}", request);

                    if (!request.equals(raw))
                        responseSender.sendPacket(new Identifier(requestNamespace, "handshake"), PacketByteBufs.create().writeEnumConstant(Result.FAILURE));
                    else
                        responseSender.sendPacket(new Identifier(requestNamespace, "handshake"), PacketByteBufs.create().writeEnumConstant(Result.SUCCESS));
                });
            });
    }

    public void registerJoinListener() {
        if (enabled) ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            //LOGGER.info("Server sent config handshake for " + this.namespace + " to " + handler.player.getName().getString());
            ServerPlayNetworking.send(handler.player, new Identifier(this.namespace, "sync"),
                    PacketByteBufs.create()
                            .writeString(this.namespace)
                            .writeString(this.config.getHandler().getCommonOptions()));
        });
    }

    public void registerServerReceiver() {
        if (enabled)
            ServerPlayNetworking.registerGlobalReceiver(new Identifier(this.namespace, "handshake"), (server, player, handler, buf, responseSender) -> {
                Result result = buf.readEnumConstant(Result.class);
                server.execute(() -> {
                    //LOGGER.info("Handshake response from " + player.getName().getString() + " : " + result.toString());
                    switch (result) {

                        case FAILURE -> {
                            player.networkHandler.disconnect(Text.literal("Config is mismatched between server and client!"));
                            LOGGER.warn("Config mismatch for {}, disconnecting", this.namespace);
                        }

                        case ERROR -> {
                            player.networkHandler.disconnect(Text.literal("Unable to complete config handshake"));
                            LOGGER.error("Handshake failed due to error");
                        }

                        case SUCCESS -> {
                            //LOGGER.info("Handshake succeeded with no issues");
                        }
                    }
                });
            });
    }
}
