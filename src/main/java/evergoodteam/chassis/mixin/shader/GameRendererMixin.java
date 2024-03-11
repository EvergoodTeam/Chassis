package evergoodteam.chassis.mixin.shader;

import com.mojang.datafixers.util.Pair;
import evergoodteam.chassis.client.gui.shader.ShaderWrapper;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.ShaderStage;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.resource.ResourceFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.function.Consumer;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    /// net.fabricmc.fabric.mixin.client.rendering.shader.GameRendererMixin
    @Inject(
            method = "loadPrograms",
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void injectShaders(ResourceFactory factory, CallbackInfo ci, List<ShaderStage> stages, List<Pair<ShaderProgram, Consumer<ShaderProgram>>> programs) {
        ShaderWrapper.forEachProgram(loader ->
                programs.add(Pair.of(loader.getLeft().apply(factory), loader.getRight()))
        );
    }
}
