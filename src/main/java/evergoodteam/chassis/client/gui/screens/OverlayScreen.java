package evergoodteam.chassis.client.gui.screens;

import evergoodteam.chassis.client.gui.widgets.OverlayWidget;
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
