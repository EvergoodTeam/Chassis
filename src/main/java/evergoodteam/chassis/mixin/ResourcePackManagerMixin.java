package evergoodteam.chassis.mixin;

import com.google.common.collect.ImmutableSet;
import evergoodteam.chassis.objects.resourcepacks.ClientResourcePackProvider;
import evergoodteam.chassis.objects.resourcepacks.ResourcePackBase;
import evergoodteam.chassis.objects.resourcepacks.ServerResourcePackProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackProvider;
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

    private static Logger LOGGER = getLogger(CMI + "/Resource");

    @Shadow
    @Final
    @Mutable
    private Set<ResourcePackProvider> providers;

    @Inject(method = "providePackProfiles", at = @At(value = "HEAD"))
    private void injectProvidePackProfiles(CallbackInfoReturnable<Map<String, ResourcePackProfile>> cir) {

        LOGGER.info("Injecting Chassis' resource providers");

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

        Map<String, List<ResourcePackBase>> m = ResourcePackBase.RESOURCE_PACKS;

        Set<ResourcePackProvider> prov = new HashSet<>(this.providers);

        if (!providerAlreadyExists) {
            for (int i = 0; i < m.keySet().size(); i++) {

                String id = m.keySet().toArray()[i].toString();

                for (int j = 0; j < m.get(m.keySet().toArray()[i]).size(); j++) {

                    ResourcePackBase r = m.get(m.keySet().toArray()[i]).get(j);

                    if (client) {
                        prov.add(new ClientResourcePackProvider(id, r.namespace, r.hexDescColor));
                        //LOGGER.info("Injected our ClientProvider into providers: {}", this.providers);
                    } else {
                        prov.add(new ServerResourcePackProvider(id, r.namespace));
                        //LOGGER.info("Injected our ServerProvider into providers: {}", this.providers);
                    }
                }
            }
        }

        this.providers = ImmutableSet.copyOf(prov);
    }
}
