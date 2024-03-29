package evergoodteam.chassis.client.gui.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import evergoodteam.chassis.client.gui.widget.WidgetBase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

// TODO: [NU] shouldn't be a screen, only a Drawable, see SplashOverlay (not really, see ChatScreen)
@Environment(value = EnvType.CLIENT)
public class OverlayScreen extends Screen implements Element {

    protected static final Identifier CROSSHAIR = new Identifier("hud/crosshair");
    private WidgetBase overlayWidget;
    private Screen parent;

    public OverlayScreen(Screen parent, WidgetBase widget) {
        this(widget);
        this.parent = parent;
    }

    public OverlayScreen(WidgetBase widget) {
        super(Text.literal("Overlay"));
        this.overlayWidget = widget;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);

        super.render(context, mouseX, mouseY, delta);
        overlayWidget.render(context, mouseX, mouseY, delta);
    }

    public void drawCrosshair(DrawContext context) {
        RenderSystem.blendFuncSeparate(
                GlStateManager.SrcFactor.ONE_MINUS_DST_COLOR, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO
        );
        context.drawGuiTexture(CROSSHAIR, (context.getScaledWindowWidth() - 15) / 2, (context.getScaledWindowHeight() - 15) / 2, 15, 15);
        RenderSystem.defaultBlendFunc();
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (isLeftClick(button) && insideOverlay(mouseX, mouseY)) {

            // Drag the preview around, keeping the cursor at the same distance from previous XY
            int distanceX = (int) (mouseX - overlayWidget.x);
            int distanceY = (int) (mouseY - overlayWidget.y);
            overlayWidget.x = (int) (mouseX + deltaX - distanceX);
            overlayWidget.y = (int) (mouseY + deltaY - distanceY);

            if (overlayWidget.hasUpdateCallback())
                overlayWidget.getWidgetUpdateCallback().onPositionUpdate(overlayWidget.x, overlayWidget.y);

            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
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
            if (overlayWidget.hasUpdateCallback()) overlayWidget.getWidgetUpdateCallback().onSave();
            if (parent != null) client.setScreen(parent);
            else client.setScreen(null);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
