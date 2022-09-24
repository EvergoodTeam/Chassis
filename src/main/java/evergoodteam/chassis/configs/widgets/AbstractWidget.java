package evergoodteam.chassis.configs.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import evergoodteam.chassis.configs.widgets.util.DrawingUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public abstract class AbstractWidget extends DrawingUtils implements Drawable, Element, Selectable {

    public final MinecraftClient client = MinecraftClient.getInstance();
    public final TextRenderer textRenderer = client.textRenderer;

    public boolean hovered;
    public boolean active = true;

    public void renderButton(MatrixStack matrices, int x, int y, int width, int height) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, ClickableWidget.WIDGETS_TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        int i = getYImage(this.hovered);
        DrawableHelper.drawTexture(matrices, x, y, 0, 0, 46 + i * 20, width / 2, height, 256, 256);
        DrawableHelper.drawTexture(matrices, x + width / 2, y, 0, 200 - width / 2f, 46 + i * 20, width / 2, height, 256, 256);
    }

    public int getYImage(boolean hovered) {
        if (!this.active) return 0;
        if (hovered) return 2;
        return 1;
    }

    public void playSound(SoundEvent sound, SoundManager soundManager, float pitch) {
        soundManager.play(PositionedSoundInstance.master(sound, pitch));
    }

    public void playDownSound(SoundManager soundManager) {
        this.playSound(SoundEvents.UI_BUTTON_CLICK, soundManager, 1.0f);
    }

    @Override
    public SelectionType getType() {
        if (this.hovered) {
            return SelectionType.HOVERED;
        }
        return SelectionType.NONE;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
    }
}
