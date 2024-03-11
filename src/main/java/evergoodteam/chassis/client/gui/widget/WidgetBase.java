package evergoodteam.chassis.client.gui.widget;

import com.google.common.collect.ImmutableList;
import evergoodteam.chassis.util.gui.ColorUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Environment(value = EnvType.CLIENT)
public class WidgetBase extends AbstractWidget {

    public Text message;
    public int truncateWidth = 8;
    public List<OrderedText> orderedTooltip;
    private final WidgetUpdateCallback updateCallback;
    protected final List<WidgetBase> children = new ArrayList<>();

    public WidgetBase(int x, int y, int width, int height, Text message) {
        this(null, x, y, width, height, message);
    }

    public WidgetBase(WidgetUpdateCallback callback, int x, int y, int width, int height, Text message) {
        this.updateCallback = callback;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.message = message;

        this.init();
    }

    public void setMessage(String message) {
        setMessage(Text.literal(message));
    }

    public void setMessage(Text message) {
        this.message = message;
    }

    public Text getMessage() {
        return this.message;
    }

    @Override
    public List<WidgetBase> getChildren() {
        return this.children;
    }

    public List<OrderedText> getOrderedTooltip() {
        return this.orderedTooltip != null ? this.orderedTooltip : ImmutableList.of();
    }

    /**
     * Sets this widget's tooltip, setting its maximum width to 80% of the width of this widget.
     */
    public void setOrderedTooltip(Text tooltip) {
        setOrderedTooltip(tooltip, (this.width / 10) * 8);
    }

    public void setOrderedTooltip(Text tooltip, int width) {
        setOrderedTooltip(wrapLines(client, tooltip, width));
    }

    public void setOrderedTooltip(List<OrderedText> tooltip) {
        this.orderedTooltip = tooltip;
    }

    public List<OrderedText> wrapLines(MinecraftClient client, Text text, int width) {
        return client.textRenderer.wrapLines(text, width);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (!isEnabled()) return;
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
        if (isLeftClick(button) && isMouseOver(mouseX, mouseY)) {
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
        if (hasUpdateCallback()) this.updateCallback.onPress(this);
    }

    public boolean hasUpdateCallback() {
        return getWidgetUpdateCallback() != null;
    }

    @Nullable
    public WidgetBase.WidgetUpdateCallback getWidgetUpdateCallback() {
        return updateCallback;
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

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        if (!this.active) {
            return false;
        }
        return isMouseOver(mouseX, mouseY, this.x, this.y, this.width, this.height);
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
        context.drawCenteredTextWithShadow(textRenderer, Text.literal(message).fillStyle(this.getMessage().getStyle()), x, y, ColorUtils.WHITE);
    }

    public boolean isTruncatable(Text text) {
        return textRenderer.getWidth(text) > textRenderer.getWidth(this.truncateString(text.getString()));
    }

    public String truncateString(String string) {
        return truncateString(string, this.truncateWidth);
    }

    public String truncateString(String string, int truncateWidth) {
        String result = string;

        while (textRenderer.getWidth(result) > this.width - truncateWidth) {
            result = result.substring(0, result.length() - 5).trim();
            result += "...";
        }

        return result;
    }

    public interface PressAction extends WidgetUpdateCallback {

        @Override
        void onPress(WidgetBase widget);
    }

    public interface WidgetUpdateCallback {

        default void onPress(WidgetBase widget) {
        }

        default void onPositionUpdate(int x, int y) {
        }

        default void onSave() {
        }
    }
}
