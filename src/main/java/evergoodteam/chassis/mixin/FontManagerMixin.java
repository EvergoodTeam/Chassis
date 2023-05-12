package evergoodteam.chassis.mixin;

import evergoodteam.chassis.client.gui.text.GradientTextRenderer;
import evergoodteam.chassis.client.gui.text.GradientTextRendererGetter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.FontManager;
import net.minecraft.client.font.FontStorage;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;
import java.util.Optional;

@Mixin(FontManager.class)
public class FontManagerMixin implements GradientTextRendererGetter {

    @Final
    @Shadow
    Map<Identifier, FontStorage> fontStorages;
    @Shadow
    private Map<Identifier, Identifier> idOverrides;
    @Final
    @Shadow
    private FontStorage missingStorage;

    @Override
    public Optional<GradientTextRenderer> createGradientTextRenderer() {
        TextRenderer clientRenderer = MinecraftClient.getInstance().textRenderer;
        return Optional.of(new GradientTextRenderer((id) ->
                fontStorages.getOrDefault(idOverrides.getOrDefault(id, id), missingStorage),
                clientRenderer.getTextHandler()));
    }
}
