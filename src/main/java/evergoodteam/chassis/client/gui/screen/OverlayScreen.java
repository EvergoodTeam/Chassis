package evergoodteam.chassis.client.gui.screen;

import evergoodteam.chassis.client.gui.widget.OverlayWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

@Environment(value = EnvType.CLIENT)
public class OverlayScreen extends Screen implements Element {

    public OverlayWidget overlayWidget;

    public OverlayScreen(PlayerEntity player, OverlayWidget overlay) {
        super(Text.literal("Overlay screen"));
        this.overlayWidget = overlay;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.renderBackground(context);
        overlayWidget.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (isLeftClick(button) && insideOverlay(mouseX, mouseY)) {

            // Drag the preview around, keeping the cursor at the same distance from previous XY
            int distanceX = (int) (mouseX - overlayWidget.x);
            int distanceY = (int) (mouseY - overlayWidget.y);
            overlayWidget.x = (int) (mouseX + deltaX - distanceX);
            overlayWidget.y = (int) (mouseY + deltaY - distanceY);

            overlayWidget.getUpdateCallback().onUpdate(overlayWidget.x, overlayWidget.y);

            return true;
        }
        return false;
    }

    public boolean insideOverlay(double mouseX, double mouseY) {
        return mouseX >= (double) overlayWidget.x && mouseY >= (double) overlayWidget.y && mouseX < (double) (overlayWidget.x + overlayWidget.width) && mouseY < (double) (overlayWidget.y + overlayWidget.height);
    }

    public boolean isLeftClick(int button) {
        return button == 0;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            overlayWidget.getUpdateCallback().onSave();
            client.setScreen(null);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
