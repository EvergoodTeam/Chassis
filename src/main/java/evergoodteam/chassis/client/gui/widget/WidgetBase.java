package evergoodteam.chassis.client.gui.widget;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.navigation.GuiNavigation;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

@Environment(value = EnvType.CLIENT)
public class WidgetBase extends AbstractWidget implements Widget {

    public int x;
    public int y;
    public int width;
    public int height;
    public Text message;
    public List<OrderedText> tooltip;
    public PressAction onPress;

    public WidgetBase(int x, int y, int width, int height, Text message, PressAction onPress) {
        this(x, y, width, height, message);
        this.onPress = onPress;
    }

    public WidgetBase(int x, int y, int width, int height, Text message) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.message = message;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.hovered = this.isMouseOver(mouseX, mouseY);
        if (this.hovered) onHover();
        this.renderBackground(context, mouseX, mouseY);
        this.renderButton(context, this.x, this.y, this.width, this.height);
        this.renderSlider(context, mouseX, mouseY);
        this.renderCenteredText(context);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!this.active) return false;
        if (isLeftClick(button) && insideBounds(mouseX, mouseY)) {
            onClick(mouseX, mouseY);
            playDownSound(MinecraftClient.getInstance().getSoundManager());
            return true;
        }
        return false;
    }

    public void onClick(double mouseX, double mouseY) {
        this.onPress();
    }

    public void onPress() {
        if (this.onPress != null) this.onPress.onPress(this);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (isLeftClick(button)) {
            onRelease(mouseX, mouseY);
            return true;
        }
        return false;
    }

    public void onRelease(double mouseX, double mouseY) {
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (isLeftClick(button)) {
            onDrag(mouseX, mouseY, deltaX, deltaY);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return super.charTyped(chr, modifiers);
    }

    @Nullable
    @Override
    public GuiNavigationPath getNavigationPath(GuiNavigation navigation) {
        return super.getNavigationPath(navigation);
    }

    public void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
    }

    public void onHover() {
    }

    public boolean isMouseOver(double mouseX, double mouseY, double x, double y, double width, double height) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return insideBounds(mouseX, mouseY);
    }

    @Override
    public void setFocused(boolean focused) {
        this.hovered = true;
    }

    @Override
    public boolean isFocused() {
        return this.hovered;
    }

    @Nullable
    @Override
    public GuiNavigationPath getFocusedPath() {
        return super.getFocusedPath();
    }

    @Override
    public ScreenRect getNavigationFocus() {
        return super.getNavigationFocus();
    }

    public boolean insideBounds(double mouseX, double mouseY) {
        return mouseX >= (double) this.x && mouseY >= (double) this.y && mouseX < (double) (this.x + this.width) && mouseY < (double) (this.y + this.height);
    }

    public Text getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = Text.literal(message);
    }

    public void setMessage(Text message) {
        this.message = message;
    }

    public void renderSlider(DrawContext context, int mouseX, int mouseY) {
    }

    public void renderBackground(DrawContext context, int mouseX, int mouseY) {
    }

    public void renderCenteredText(DrawContext context) {
        this.renderCenteredText(context, this.getMessage(), this.y + (this.height - 8) / 2);
    }

    public void renderCenteredText(DrawContext context, Text message, int y) {
        this.renderCenteredText(context, message, this.x + (this.width / 2), y);
    }

    public void renderCenteredText(DrawContext context, Text text, int x, int y) {
        String message = truncateString(text.getString());
        context.drawCenteredTextWithShadow(textRenderer, Text.literal(message).fillStyle(this.getMessage().getStyle()), x, y, 16777215);
    }

    public String truncateString(String string) {
        String result = string;
        boolean firstIter = true;

        while (textRenderer.getWidth(result) > this.width - 8) {
            result = result.substring(0, Math.max(result.length() - (firstIter ? 2 : 5), 0)).trim();
            result += "...";
            firstIter = false;
        }

        return result;
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

    @Environment(value = EnvType.CLIENT)
    public interface PressAction {
        void onPress(WidgetBase var1);
    }

    @Environment(value = EnvType.CLIENT)
    public interface RenderAction {

        void onRender(WidgetBase var1);
    }

    public List<OrderedText> getOrderedTooltip() {
        return this.tooltip != null ? this.tooltip : ImmutableList.of();
    }

    public void setTooltip(Text tooltip) {
        this.tooltip = wrapLines(client, tooltip);
    }

    public void setTooltip(List<OrderedText> tooltip) {
        this.tooltip = tooltip;
    }

    public List<OrderedText> wrapLines(MinecraftClient client, Text text) {
        return client.textRenderer.wrapLines(text, 200);
    }
}
