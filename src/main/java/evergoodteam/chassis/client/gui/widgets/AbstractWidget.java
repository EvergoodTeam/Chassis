package evergoodteam.chassis.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import evergoodteam.chassis.client.ChassisClient;
import evergoodteam.chassis.client.gui.DrawingUtils;
import evergoodteam.chassis.client.gui.text.GradientTextRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;

public abstract class AbstractWidget extends DrawingUtils implements Drawable, Element, Selectable {

    public final MinecraftClient client = MinecraftClient.getInstance();
    public final TextRenderer textRenderer = client.textRenderer;
    public final GradientTextRenderer gradientTextRenderer = ChassisClient.gradientTextRenderer;

    public boolean hovered;
    public boolean active = true;
    public boolean enabled = true;
    protected float alpha = 1.0F;

    public void renderButton(DrawContext context, int x, int y, int width, int height) {
        if (enabled) {
            context.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
            context.drawNineSlicedTexture(ClickableWidget.WIDGETS_TEXTURE, x, y, width, height, 20, 4, 200, 20, 0, this.getTextureY());
            context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public int getTextureY() {
        int i = 1;
        if (!this.active) {
            i = 0;
        } else if (this.hovered) {
            i = 2;
        }

        return 46 + i * 20;
    }

    public int getYImage(boolean hovered) {
        if (!this.active) return 0;
        if (hovered) return 2;
        return 1;
    }

    public void playSound(RegistryEntry.Reference<SoundEvent> sound, SoundManager soundManager, float pitch) {
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
