package evergoodteam.chassis.mixin;

import evergoodteam.chassis.objects.resourcepacks.ClientResourcePackProvider;
import evergoodteam.chassis.objects.resourcepacks.ResourcePackBase;
import evergoodteam.chassis.objects.resourcepacks.ServerResourcePackProvider;
import lombok.extern.log4j.Log4j2;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Log4j2
@Mixin(ResourcePackManager.class)
public class ResourcePackManagerMixin {

    @Shadow
    @Final
    private Set<ResourcePackProvider> providers;

    @Inject(method = "providePackProfiles", at = @At(value = "HEAD"))
    private void injectProvidePackProfiles(CallbackInfoReturnable<Map<String, ResourcePackProfile>> cir) {

        boolean providerAlreadyExists = false;
        boolean client;
        client = FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;

        for (ResourcePackProvider provider : this.providers) {
            if (provider instanceof ClientResourcePackProvider) {
                client = true;
                providerAlreadyExists = true;
                break;
            }

            if (provider instanceof ServerResourcePackProvider) {
                providerAlreadyExists = true;
                break;
            }
        }

        Map<String, List<ResourcePackBase>> m = ResourcePackBase.RESOURCE_PACKS;

        if (!providerAlreadyExists) {
            if (client) {

                for (int i = 0; i < m.keySet().size(); i++) {

                    for (int j = 0; j < m.get(m.keySet().toArray()[i]).size(); j++) { // sus

                        this.providers.add(new ClientResourcePackProvider(m.keySet().toArray()[i].toString(), m.get(m.keySet().toArray()[i]).get(j).namespace));
                    }
                }

                //log.info("Injected our ClientProvider into providers: {}", this.providers);
            } else {

                for (int i = 0; i < m.keySet().size(); i++) {

                    for (int j = 0; j < m.get(m.keySet().toArray()[i]).size(); j++) {

                        this.providers.add(new ServerResourcePackProvider(m.keySet().toArray()[i].toString(), m.get(m.keySet().toArray()[i]).get(j).namespace));
                    }
                }

                //log.info("Injected our ServerProvider into providers: {}", this.providers);
            }
        }
    }
}
