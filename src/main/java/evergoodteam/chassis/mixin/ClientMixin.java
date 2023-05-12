package evergoodteam.chassis.mixin;

import evergoodteam.chassis.client.ChassisClient;
import lombok.extern.log4j.Log4j2;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.font.FontManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Log4j2
@Mixin(MinecraftClient.class)
public class ClientMixin {

    @Shadow
    @Final
    private FontManager fontManager;

    @Inject(method = "<init>(Lnet/minecraft/client/RunArgs;)V", at = @At("TAIL"))
    private void inject(RunArgs args, CallbackInfo ci) {
        if (ChassisClient.gradientTextRenderer == null) {
            ChassisClient.gradientTextRenderer = fontManager.createGradientTextRenderer().get();
        }
    }
}
