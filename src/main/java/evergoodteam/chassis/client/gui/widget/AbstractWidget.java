package evergoodteam.chassis.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import evergoodteam.chassis.client.ChassisClient;
import evergoodteam.chassis.client.gui.DrawingUtils;
import evergoodteam.chassis.client.gui.text.GradientTextRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.navigation.GuiNavigation;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractWidget extends DrawingUtils implements Drawable, Element, Selectable, Widget {

    public final MinecraftClient client = MinecraftClient.getInstance();
    public final TextRenderer textRenderer = client.textRenderer;
    public final GradientTextRenderer gradientTextRenderer = ChassisClient.gradientTextRenderer;

    public int x;
    public int y;
    public int addedHeight = 0;
    public int width;
    public int height;
    public static final int space = 8;

    public boolean hovered;
    public boolean active = true;
    public boolean enabled = true;
    protected float alpha = 1.0F;

    public void init() {
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
    }

    public void renderButton(DrawContext context, int x, int y, int width, int height) {
        if (!isEnabled()) return;
        context.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        context.drawGuiTexture(PressableWidget.TEXTURES.get(this.active, this.getType() != SelectionType.NONE), x, y, width, height);
        context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
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

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void forEachChild(Consumer<ClickableWidget> consumer) {
    }

    public abstract List<? extends AbstractWidget> getChildren();

    /**
     * Additional height to be used for rendering and mouse detection when the widget is used in lists, such as {@link ResettableListWidget},
     * where the Y coordinate is updated constantly.
     */
    public void setAddedHeight(int addedHeight) {
        this.addedHeight = addedHeight;
    }

    public int getAddedHeight() {
        return this.addedHeight;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isLeftClick(int button) {
        return button == 0;
    }

    public void playDownSound(SoundManager soundManager) {
        this.playSound(SoundEvents.UI_BUTTON_CLICK, soundManager, 1.0f);
    }

    public void playSound(RegistryEntry.Reference<SoundEvent> sound, SoundManager soundManager, float pitch) {
        soundManager.play(PositionedSoundInstance.master(sound, pitch));
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        Element.super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return Element.super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return Element.super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return Element.super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return Element.super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return Element.super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return Element.super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return Element.super.charTyped(chr, modifiers);
    }

    @Nullable
    @Override
    public GuiNavigationPath getNavigationPath(GuiNavigation navigation) {
        return Element.super.getNavigationPath(navigation);
    }

    @Override
    public abstract boolean isMouseOver(double mouseX, double mouseY);

    public boolean isMouseOver(double mouseX, double mouseY, double x, double y, double width, double height) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }

    @Override
    public void setFocused(boolean focused) {
        this.hovered = focused;
    }

    @Override
    public boolean isFocused() {
        return this.hovered;
    }

    @Nullable
    @Override
    public GuiNavigationPath getFocusedPath() {
        return Element.super.getFocusedPath();
    }

    @Override
    public ScreenRect getNavigationFocus() {
        return Element.super.getNavigationFocus();
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
