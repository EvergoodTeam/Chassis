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

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (isLeftClick(button) && insideBounds(mouseX, mouseY)) {

            // Drag the preview around, keeping the cursor at the same distance from previous XY
            int distanceX = (int) (mouseX - x);
            int distanceY = (int) (mouseY - y);
            x = (int) (mouseX + deltaX - distanceX);
            y = (int) (mouseY + deltaY - distanceY);

            updateCallback.onUpdate(x, y);

            return true;
        }
        return false;
    }

    public boolean insideBounds(double mouseX, double mouseY) {
        return mouseX >= (double) this.x && mouseY >= (double) this.y && mouseX < (double) (this.x + this.width) && mouseY < (double) (this.y + this.height);
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
