package evergoodteam.chassis.configs.widgets.util;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

public abstract class DrawingUtils extends DrawableHelper {

    public void drawRectWithOutline(MatrixStack matrices, int x, int y, int width, int height, int color) {
        drawRectWithOutline(matrices, x, y, width, height, color, color);
    }

    public void drawRectWithOutline(MatrixStack matrices, int x, int y, int width, int height, int rectColor, int outlineColor) {
        fill(matrices, x, y, x + width, y + height, rectColor);

        this.drawHorizontalLine(matrices, x, x + width - 1, y, outlineColor);
        this.drawVerticalLine(matrices, x + width - 1, y, y + height - 1, outlineColor);
        this.drawHorizontalLine(matrices, x, x + width - 1, y + height - 1, outlineColor);
        this.drawVerticalLine(matrices, x, y, y + height - 1, outlineColor);
    }
}
