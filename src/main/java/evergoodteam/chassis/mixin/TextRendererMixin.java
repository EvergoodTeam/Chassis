package evergoodteam.chassis.mixin;

import evergoodteam.chassis.client.ChassisClient;
import evergoodteam.chassis.client.gui.text.GradientTextRenderer;
import net.minecraft.client.font.FontStorage;
import net.minecraft.client.font.TextHandler;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(TextRenderer.class)
public class TextRendererMixin {

    @Shadow @Final private TextHandler handler;

    @Inject(method = "<init>(Ljava/util/function/Function;Z)V", at = @At("TAIL"))
    public void inject(Function<Identifier, FontStorage> fontStorageAccessor, boolean validateAdvance, CallbackInfo ci){
        ChassisClient.gradientTextRenderer = new GradientTextRenderer(fontStorageAccessor, handler);
    }
}
