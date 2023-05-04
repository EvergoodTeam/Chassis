package evergoodteam.chassis.mixin;

import evergoodteam.chassis.client.ChassisClient;
import evergoodteam.chassis.client.gui.text.GradientTextRenderer;
import lombok.extern.log4j.Log4j2;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.font.TextRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Log4j2
@Mixin(MinecraftClient.class)
public class ClientMixin {

    @Inject(method = "<init>(Lnet/minecraft/client/RunArgs;)V", at = @At("TAIL"))
    private void inject(RunArgs args, CallbackInfo ci){
        log.info("HAIIII");
        TextRenderer clientRenderer = MinecraftClient.getInstance().textRenderer;
        ChassisClient.gradientTextRenderer = new GradientTextRenderer(clientRenderer.fontStorageAccessor, clientRenderer.getTextHandler());
    }
}
