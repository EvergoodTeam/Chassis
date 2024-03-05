package evergoodteam.chassis.client.gui.widget.interfaces;

import evergoodteam.chassis.client.gui.widget.AbstractWidget;
import evergoodteam.chassis.client.gui.widget.DropdownWidget;
import evergoodteam.chassis.client.gui.widget.WidgetBase;
import evergoodteam.chassis.util.gui.ColorUtils;
import net.minecraft.client.gui.DrawContext;

public interface ConfigWidgetEntry {

    int verticalSpace = 2;
    int cfgWidth = 300;
    int halfWidth = cfgWidth / 2;
    int cfgHeight = 20 + verticalSpace * 2;
    int resetWidth = 40;
    int buttonsWidth = 100 + 2 + resetWidth;

    default void onReset(){}

    default boolean hasExtendedHeight(){
        return true;
    }

    default int getExtendedHeight(){
        return verticalSpace * 2;
    }

    // Accounts for widgets resetting by using coordinates from the entry and not the widget itself
    default void drawHoveredHighlight(WidgetBase widget, DrawContext context, int centerX, int y, double mouseX, double mouseY){
        widget.drawRectWithOutline(context, centerX - halfWidth, y, cfgWidth, cfgHeight, ColorUtils.TWHITE);
    }

    default int getResetWidgetAddY(){
        return verticalSpace;
    }

    /**
     * Returns true if mouse is on the left or the right of the main widget and its reset widget
     */
    default boolean isMouseOverTooltip(WidgetBase widget, double mouseX, double mouseY){
        return widget.isMouseOver(mouseX, mouseY, widget.getX() - halfWidth, widget.getY() - verticalSpace, cfgWidth - buttonsWidth - AbstractWidget.space, cfgHeight)
                || widget.isMouseOver(mouseX, mouseY, widget.getX() + buttonsWidth, widget.getY() - verticalSpace, AbstractWidget.space, cfgHeight);
    }
}
