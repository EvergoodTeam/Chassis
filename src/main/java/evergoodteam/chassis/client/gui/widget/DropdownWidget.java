package evergoodteam.chassis.client.gui.widget;

import evergoodteam.chassis.client.gui.text.ChassisScreenTexts;
import evergoodteam.chassis.util.gui.ColorUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Widget that supports both children and the ability to open/close a dropdown, where the non-whitelisted elements are shown/hidden.
 * Whitelisted elements will be visible regardless of the dropdown's state.
 *
 * @see #addToWhitelist(Element)
 * @see #setVisibility(boolean)
 */
public class DropdownWidget extends WidgetParentBase {

    public static int arrowSection = 13;
    private boolean open = true;
    private int closedHeight;
    private int openHeight;
    private final List<Element> whitelist = new ArrayList<>();

    public DropdownWidget(int x, int y, int width, int height, Text message) {
        this(x, y, width, height + arrowSection, 20, message);
    }

    public DropdownWidget(int x, int y, int width, int height, int closedHeight, Text message) {
        this(null, x, y, width, height + arrowSection, closedHeight, message);
    }

    public DropdownWidget(WidgetUpdateCallback updateCallback, int x, int y, int width, int height, int closedHeight, Text message) {
        super(updateCallback, x, y, width, height + arrowSection, message);
        this.openHeight = height;
        this.closedHeight = closedHeight;
    }

    public int getClosedHeight() {
        return this.closedHeight;
    }

    /**
     * Adds the specified element to a whitelist that isn't hidden when the dropdown is closed
     */
    public void addToWhitelist(Element element) {
        whitelist.add(element);
    }

    /**
     * Draws an up/down arrow ("⊼", "⊽" by default) if the dropdown is closed/open respectively.
     *
     * @see ChassisScreenTexts
     */
    public void renderArrow(DrawContext context, int mouseX, int mouseY) {
        //context.drawVerticalLine(x + width/2, y, y + height + 100, 0xFF_00FF00);
        //context.drawHorizontalLine(x, x + width, y + this.height - arrowSection, 0xFF_FF0000);

        context.drawTextWithShadow(textRenderer, open ? ChassisScreenTexts.ARROW_UP : ChassisScreenTexts.ARROW_DOWN, x + width / 2 - 2, y + height - 10, this.isMouseOver(mouseX, mouseY) ? -1 : ColorUtils.AWHITE);
    }

    @Override
    public void onPress() {
        toggleVisibility();
    }

    /**
     * Toggles the dropdown state.
     */
    public void toggleVisibility() {
        setVisibility(!open);
    }

    public boolean isOpen() {
        return this.open;
    }

    /**
     * Manually sets the dropdown state, can be used to set an initial state after initializing the widget.
     */
    public DropdownWidget setVisibility(boolean open) {
        this.open = open;
        this.getChildren().stream().filter(child -> !whitelist.contains(child)).forEach(child -> {
            child.active = open;
            child.enabled = open;
        });
        updateHeight();
        return this;
    }

    /**
     * Updates the widget height based on the dropdown state.
     */
    public void updateHeight() {
        this.height = open ? openHeight : closedHeight;
        this.height += arrowSection;
    }
}
