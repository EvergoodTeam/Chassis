package evergoodteam.chassis.mixin;

import net.minecraft.resource.ResourcePackProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import lombok.extern.log4j.Log4j2;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.google.common.collect.ImmutableSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourcePackManager;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Set;

import evergoodteam.chassis.objects.resourcepacks.ClientResourcePackProvider;
import evergoodteam.chassis.objects.resourcepacks.ServerResourcePackProvider;

import static evergoodteam.chassis.objects.resourcepacks.ResourcePackBase.NAMESPACES;
import static evergoodteam.chassis.util.Reference.*;

@Log4j2
@Mixin(ResourcePackManager.class)
public class ResourcePackManagerMixin {

    @Redirect(method = "<init>(Lnet/minecraft/resource/ResourcePackProfile$Factory;[Lnet/minecraft/resource/ResourcePackProvider;)V",
            at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableSet;copyOf([Ljava/lang/Object;)Lcom/google/common/collect/ImmutableSet;"))
    private <E> ImmutableSet<Object> injectResourcePack(E[] elements) {

        boolean isClient = FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;

        boolean providerPresent = false;

        for (int i = 0; i < elements.length; i++) {
            E element = elements[i];

            if (element instanceof ClientResourcePackProvider) {
                isClient = true;
                providerPresent = true;
                break;
            }

            if (element instanceof ServerResourcePackProvider) {
                providerPresent = true;
                break;
            }
        }


        if (!providerPresent) {

            LOGGER.info("Registering {} ResourcePack(s)", NAMESPACES.size()); // TODO: ?

            if (isClient) return ImmutableSet.copyOf(ArrayUtils.add(elements, new ClientResourcePackProvider(NAMESPACES)));

            else return ImmutableSet.copyOf(ArrayUtils.add(elements, new ServerResourcePackProvider(NAMESPACES)));
        }

        return ImmutableSet.copyOf(elements);
    }
}
