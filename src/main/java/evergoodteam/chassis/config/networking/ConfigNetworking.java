package evergoodteam.chassis.config.networking;

import evergoodteam.chassis.common.Result;
import evergoodteam.chassis.config.ConfigBase;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationNetworking;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static evergoodteam.chassis.util.Reference.CMI;

public class ConfigNetworking {

    private static final Logger LOGGER = LoggerFactory.getLogger(CMI + "/C/Network");
    public final ConfigBase config;
    public final Identifier identifier;
    private boolean enabled = true;

    public ConfigNetworking(ConfigBase config) {
        this.config = config;
        this.identifier = config.getIdentifier();
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    public void registerClientReceiverAndResponse() {
        if (enabled) {
            ClientConfigurationNetworking.registerGlobalReceiver(this.identifier.withSuffixedPath("sync"), (client, handler, buf, responseSender) -> {

                Identifier requestIdentifier = Identifier.tryParse(buf.readString());
                String request = buf.readString();
                // TODO: account for user changing the option to desired but the option needs a restart: add a flag to options that turns on when a restart is needed and
                String raw = ConfigBase.getConfig(requestIdentifier).getWriter().getSerializer().getMappedStoredServerUserOptions().toString();
                //LOGGER.info("RAW: {}", raw);
                //LOGGER.info("RECEIVED: {}", request);

                if (!request.equals(raw))
                    responseSender.sendPacket(requestIdentifier.withSuffixedPath("handshake"), PacketByteBufs.create().writeEnumConstant(Result.FAILURE));
                else
                    responseSender.sendPacket(requestIdentifier.withSuffixedPath("handshake"), PacketByteBufs.create().writeEnumConstant(Result.SUCCESS));
            });
        }
    }

    public void registerServerConnectionListener() {
        if (enabled) {
            ServerConfigurationConnectionEvents.CONFIGURE.register((handler, server) -> {
                //LOGGER.info("Server sent config handshake for " + this.identifier);
                ServerConfigurationNetworking.send(handler, this.identifier.withSuffixedPath("sync"),
                        PacketByteBufs.create()
                                .writeString(this.identifier.toString())
                                .writeString(this.config.getWriter().getSerializer().getMappedStoredServerUserOptions().toString()));
            });
        }
    }

    public void registerHandshakeReceiver() {
        if (enabled) {
            ServerConfigurationNetworking.registerGlobalReceiver(this.identifier.withSuffixedPath("handshake"), (player, handler, buf, responseSender) -> {
                Result result = buf.readEnumConstant(Result.class);
                switch (result) {

                    case FAILURE -> {
                        handler.disconnect(Text.of("Config is mismatched between server and client!"));
                        LOGGER.warn("Config mismatch for {}, disconnecting", this.identifier);
                    }

                    case ERROR -> {
                        handler.disconnect(Text.of("Unable to complete config handshake"));
                        LOGGER.error("Handshake failed due to error");
                    }

                    case SUCCESS -> {
                        //LOGGER.info("Handshake succeeded with no issues");
                    }
                }
            });
        }
    }
}