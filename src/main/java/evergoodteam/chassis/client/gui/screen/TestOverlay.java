package evergoodteam.chassis.client.gui.screen;

import evergoodteam.chassis.client.gui.widget.WidgetBase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ParentElement;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.RenderLayer;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.List;

@Deprecated
@Environment(EnvType.CLIENT)
public class TestOverlay extends Overlay implements ParentElement {
    public MinecraftClient client;
    public WidgetBase overlayWidget;
    private Screen parent;
    public int width;
    public int height;

    public TestOverlay(MinecraftClient client, Screen parent, WidgetBase widget) {
        this.client = client;
        this.parent = parent;
        this.overlayWidget = widget;
        this.width = client.getWindow().getScaledWidth();
        this.height = client.getWindow().getScaledHeight();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.width = client.getWindow().getScaledWidth();
        this.height = client.getWindow().getScaledHeight();

        context.fill(RenderLayer.getGuiOverlay(), 0, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight(), 0X42_FFB3CB);

        /*
        if(parent != null) {
            parent.resize(client, this.width, this.height);
            parent.render(context, mouseX, mouseY, delta);
        }*/

        // textRenderer is rendering text above the overlay
        //context.fill(0, 0, this.width, this.height, 0x6D_FFB3CB);
        //super.render(context, mouseX, mouseY, delta);
        overlayWidget.render(context, mouseX, mouseY, delta);
    }

    @Override
    public List<? extends Element> children() {
        return null;
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
        return ParentElement.super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean isDragging() {
        return false;
    }

    @Override
    public void setDragging(boolean dragging) {

    }

    public boolean insideOverlay(double mouseX, double mouseY) {
        return mouseX >= (double) overlayWidget.x && mouseY >= (double) overlayWidget.y && mouseX < (double) (overlayWidget.x + overlayWidget.width) && mouseY < (double) (overlayWidget.y + overlayWidget.height);
    }

    public boolean isLeftClick(int button) {
        return button == 0;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            if (overlayWidget.hasUpdateCallback()) overlayWidget.getWidgetUpdateCallback().onSave();
            client.setOverlay(null);
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public Element getFocused() {
        return null;
    }

    @Override
    public void setFocused(@Nullable Element focused) {

    }

    /*
    @Override
    public boolean isFocused() {
        return true;
    }*/
}
