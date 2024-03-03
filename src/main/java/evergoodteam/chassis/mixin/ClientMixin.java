package evergoodteam.chassis.mixin;

import evergoodteam.chassis.client.ChassisClient;
import evergoodteam.chassis.client.gui.text.GradientTextRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.font.FontManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(MinecraftClient.class)
public class ClientMixin {

    @Shadow
    @Final
    private FontManager fontManager;

    @Inject(method = "<init>(Lnet/minecraft/client/RunArgs;)V", at = @At("TAIL"))
    private void inject(RunArgs args, CallbackInfo ci) {
        Optional<GradientTextRenderer> optional = fontManager.chassisCreateGradientTextRenderer();
        if (ChassisClient.gradientTextRenderer == null && optional.isPresent())
            ChassisClient.gradientTextRenderer = optional.get();
    }
}
