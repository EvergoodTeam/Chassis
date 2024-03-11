package evergoodteam.chassis.client.gui.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ParentElement;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * Copy of {@link WidgetBase} with the necessary utilities for children support:
 * implements {@link ParentElement}, which is used for proper entry detection by {@link ResettableListWidget}.
 */
@Environment(value = EnvType.CLIENT)
public class WidgetParentBase extends WidgetBase implements ParentElement {

    @Nullable
    private Element focused;
    private boolean dragging;

    public WidgetParentBase(int x, int y, int width, int height, Text message) {
        super(null, x, y, width, height, message);
    }

    public WidgetParentBase(WidgetUpdateCallback callback, int x, int y, int width, int height, Text message) {
        super(callback, x, y, width, height, message);
    }

    @Override
    public void setAddedY(int addedY) {
        super.setAddedY(addedY);
        this.getChildren().forEach(widget -> widget.setAddedY(addedY));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        //if (!this.active) return false;
        if (!ParentElement.super.mouseClicked(mouseX, mouseY, button)) {
            return super.mouseClicked(mouseX, mouseY, button);
        }
        return ParentElement.super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return ParentElement.super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return ParentElement.super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return ParentElement.super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return ParentElement.super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return ParentElement.super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return ParentElement.super.charTyped(chr, modifiers);
    }

    @Override
    public List<? extends Element> children() {
        return this.getChildren();
    }

    /**
     * Account for parent having other parents
     */
    @Override
    public Optional<Element> hoveredElement(double mouseX, double mouseY) {
        for (Element element : this.children()) {
            if (!element.isMouseOver(mouseX, mouseY)) continue;
            if (element instanceof ParentElement parent) return parent.hoveredElement(mouseX, mouseY);
            return Optional.of(element);
        }
        return Optional.empty();
    }

    @Override
    public final boolean isDragging() {
        return this.dragging;
    }

    @Override
    public final void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    @Override
    @Nullable
    public Element getFocused() {
        return this.focused;
    }

    @Override
    public void setFocused(@Nullable Element focused) {
        if (this.focused != null) {
            this.focused.setFocused(false);
        }
        if (focused != null) {
            focused.setFocused(true);
        }
        this.focused = focused;
    }
}
