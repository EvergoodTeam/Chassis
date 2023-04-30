package evergoodteam.chassis.client.gui;

import evergoodteam.chassis.client.gui.text.GradientTextRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.List;

@Environment(value = EnvType.CLIENT)
public abstract class DrawingUtils extends DrawableHelper {

    public static void gradientText(MatrixStack matrices, GradientTextRenderer gradientTextRenderer, Text text, List<Integer> points, int centerX, int y, int transparency) {
        gradientTextRenderer.drawWithShadow(matrices, text, points, (float) (centerX - gradientTextRenderer.getWidth(text.asOrderedText()) / 2), (float) y, transparency);
    }

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
