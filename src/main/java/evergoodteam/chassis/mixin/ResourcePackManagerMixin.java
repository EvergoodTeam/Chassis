package evergoodteam.chassis.mixin;

import com.google.common.collect.ImmutableSet;
import evergoodteam.chassis.common.resourcepack.ResourcePackBase;
import evergoodteam.chassis.common.resourcepack.providers.ClientResourcePackProvider;
import evergoodteam.chassis.common.resourcepack.providers.ServerResourcePackProvider;
import evergoodteam.chassis.util.StringUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static evergoodteam.chassis.util.Reference.CMI;
import static org.slf4j.LoggerFactory.getLogger;

@Mixin(ResourcePackManager.class)
public class ResourcePackManagerMixin {

    private static final Logger LOGGER = getLogger(CMI + "/M/Resource");

    @Shadow
    @Final
    @Mutable
    private Set<ResourcePackProvider> providers; // Turned into an ImmutableSet later, treat as such

    @Inject(method = "providePackProfiles", at = @At(value = "HEAD"))
    public void injectProvidePackProfiles(CallbackInfoReturnable<Map<String, ResourcePackProfile>> cir) {

        LOGGER.debug("Resources are being reloaded, attempting to inject provider");

        boolean providerAlreadyExists = false;
        boolean client;
        client = FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;

        for (ResourcePackProvider provider : this.providers) {
            if (provider instanceof ClientResourcePackProvider) {
                providerAlreadyExists = true;
                client = true;
                break;
            }

            if (provider instanceof ServerResourcePackProvider) {
                providerAlreadyExists = true;
                break;
            }
        }

        Map<Identifier, List<ResourcePackBase>> resourceMap = ResourcePackBase.getResourcePacks();
        Set<ResourcePackProvider> providersCopy = new HashSet<>(this.providers);

        if (!providerAlreadyExists) {

            boolean finalClient = client;
            resourceMap.forEach(((identifier, resourcePacks) -> {
                resourcePacks.forEach(resourcePack -> {

                    String friendlyPath = StringUtils.replaceWith(identifier.toString(), "/", ":+");
                    if (friendlyPath.split("/")[0].equals(friendlyPath.split("/")[1]))
                        friendlyPath = friendlyPath.split("/")[0];

                    if (resourcePack.providerRegistry != null && !resourcePack.areProvidersDone())
                        resourcePack.providerRegistry.registerProviders();
                    if (finalClient) {
                        providersCopy.add(new ClientResourcePackProvider(friendlyPath, resourcePack.getName(), resourcePack.getDisplayName(), resourcePack.getMetadataKey(), resourcePack.getHexColor()));
                        //LOGGER.info("Injected our ClientProvider into providers: {}", this.providers);
                    } else {
                        providersCopy.add(new ServerResourcePackProvider(friendlyPath, resourcePack.getName(), resourcePack.getDisplayName(), resourcePack.getMetadataKey()));
                        //LOGGER.info("Injected our ServerProvider into providers: {}", this.providers);
                    }
                });
            }));
        }

        this.providers = ImmutableSet.copyOf(providersCopy);
    }
}
