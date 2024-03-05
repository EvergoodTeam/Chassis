package evergoodteam.chassis.client.gui;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.tooltip.HoveredTooltipPositioner;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.text.OrderedText;

import java.util.List;
import java.util.stream.Collectors;

public interface ChassisDrawContext {

    /**
     * @see #chassisDrawTooltipWithoutJump(TextRenderer, List, int, int, TooltipPositioner)
     */
    default void chassisDrawOrderedTooltipWithoutJump(TextRenderer textRenderer, List<? extends OrderedText> text, int x, int y) {
        this.chassisDrawTooltipWithoutJump(textRenderer, text.stream().map(TooltipComponent::of).collect(Collectors.toList()), x, y, HoveredTooltipPositioner.INSTANCE);
    }

    /**
     * Draws a tooltip WITHOUT separating the first line from the others by 2 pixels.
     */
    void chassisDrawTooltipWithoutJump(TextRenderer textRenderer, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner);

    /**
     * @see #chassisDrawGradientLine(RenderLayer, int, int, int, int, int, int)
     */
    default void chassisDrawGradientLine(int x1, int x2, int y, int start, int end) {
        this.chassisDrawGradientLine(x1, x2, y, 0, start, end);
    }

    /**
     * @see #chassisDrawGradientLine(RenderLayer, int, int, int, int, int, int)
     */
    default void chassisDrawGradientLine(int x1, int x2, int y, int z, int start, int end) {
        this.chassisDrawGradientLine(RenderLayer.getGui(), x1, x2, y, z, start, end);
    }

    /**
     * Draws a line filled with a horizontal gradient.
     */
    default void chassisDrawGradientLine(RenderLayer layer, int x1, int x2, int y, int z, int start, int end) {
        if (x2 < x1) {
            int i = x1;
            x1 = x2;
            x2 = i;
        }

        this.chassisFillHorizontalGradient(layer, x1, y, x2 + 1, y + 1, start, end, z);
    }

    default void chassisFillHorizontalGradient(int startX, int startY, int endX, int endY, int colorStart, int colorEnd) {
        this.chassisFillHorizontalGradient(startX, startY, endX, endY, 0, colorStart, colorEnd);
    }

    default void chassisFillHorizontalGradient(int startX, int startY, int endX, int endY, int z, int colorStart, int colorEnd) {
        this.chassisFillHorizontalGradient(RenderLayer.getGui(), startX, startY, endX, endY, colorStart, colorEnd, z);
    }

    void chassisFillHorizontalGradient(RenderLayer layer, int startX, int startY, int endX, int endY, int colorStart, int colorEnd, int z);

    void chassisFillHorizontalGradient(VertexConsumer vertexConsumer, int startX, int startY, int endX, int endY, int z, int colorStart, int colorEnd);
}
