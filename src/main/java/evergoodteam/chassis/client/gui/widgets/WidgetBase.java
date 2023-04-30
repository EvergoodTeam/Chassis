package evergoodteam.chassis.client.gui.widgets;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.OrderableTooltip;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.util.List;

@Environment(value = EnvType.CLIENT)
public class WidgetBase extends AbstractWidget implements OrderableTooltip {

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
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.hovered = this.isMouseOver(mouseX, mouseY);
        if (this.hovered) onHover();
        this.renderBackground(matrices, mouseX, mouseY);
        this.renderButton(matrices, this.x, this.y, this.width, this.height);
        this.renderSlider(matrices, mouseX, mouseY);
        this.renderCenteredText(matrices);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(!this.active) return false;
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

    public boolean isLeftClick(int button) {
        return button == 0;
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

    public void renderSlider(MatrixStack matrices, int mouseX, int mouseY) {
    }

    public void renderBackground(MatrixStack matrices, int mouseX, int mouseY) {
    }

    public void renderCenteredText(MatrixStack matrices) {
        this.renderCenteredText(matrices, this.getMessage(), this.y + (this.height - 8) / 2);
    }

    public void renderCenteredText(MatrixStack matrices, Text message, int y){
        this.renderCenteredText(matrices, message, this.x + (this.width / 2), y);
    }

    public void renderCenteredText(MatrixStack matrices, Text text, int x, int y){
        String message = truncateString(text.getString());
        drawCenteredText(matrices, textRenderer, Text.literal(message).fillStyle(this.getMessage().getStyle()), x, y, 16777215);
    }

    public String truncateString(String string){
        String result = string;
        boolean firstIter = true;

        while (textRenderer.getWidth(result) > this.width - 8) {
            result = result.substring(0, Math.max(result.length() - (firstIter ? 2 : 5), 0)).trim();
            result += "...";
            firstIter = false;
        }

        return result;
    }

    @Environment(value = EnvType.CLIENT)
    public static interface PressAction {
        void onPress(WidgetBase var1);
    }

    @Environment(value = EnvType.CLIENT)
    public static interface RenderAction {

        void onRender(WidgetBase var1);
    }

    @Override
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
