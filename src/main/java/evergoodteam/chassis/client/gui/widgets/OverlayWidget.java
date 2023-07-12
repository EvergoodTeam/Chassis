package evergoodteam.chassis.client.gui.widgets;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;

@Environment(value = EnvType.CLIENT)
public class OverlayWidget extends AbstractWidget {

    private final UpdateCallback updateCallback;
    public int x;
    public int y;
    public int width;
    public int height;
    public int color;
    public int outlineColor;

    public OverlayWidget(UpdateCallback callback, int x, int y, int width, int height, int color) {
        this(callback, x, y, width, height, color, color);
    }

    public OverlayWidget(UpdateCallback callback, int x, int y, int width, int height, int color, int outlineColor) {
        this.updateCallback = callback;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.outlineColor = outlineColor;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
    }

    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta){
        drawRectWithOutline(context, x, y, width, height, color, outlineColor);
    }

    @Environment(value = EnvType.CLIENT)
    public interface UpdateCallback {
        void onUpdate(int x, int y);

        void onSave();
    }

    public UpdateCallback getUpdateCallback(){
        return updateCallback;
    }

    @Override
    public void setFocused(boolean focused) {
    }

    @Override
    public boolean isFocused() {
        return false;
    }
}
