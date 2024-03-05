package evergoodteam.chassis.client.gui.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

@Environment(value = EnvType.CLIENT)
public class OverlayWidget extends WidgetBase {

    public int backgroundColor;
    public int outlineColor;

    public OverlayWidget(WidgetUpdateCallback callback, int x, int y, int width, int height, int color) {
        this(callback, x, y, width, height, color, color);
    }

    public OverlayWidget(WidgetUpdateCallback callback, int x, int y, int width, int height, int backgroundColor, int outlineColor) {
        super(callback, x, y, width, height, Text.empty());
        this.backgroundColor = backgroundColor;
        this.outlineColor = outlineColor;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
    }

    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        drawRectWithOutline(context, x, y, width, height, backgroundColor, outlineColor);
    }

    @Override
    public void setFocused(boolean focused) {
    }

    @Override
    public boolean isFocused() {
        return false;
    }
}
