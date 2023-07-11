package evergoodteam.chassis.client.gui;

import evergoodteam.chassis.client.gui.text.GradientText;
import evergoodteam.chassis.client.gui.text.GradientTextRenderer;
import evergoodteam.chassis.util.gui.ColorUtils;
import lombok.extern.log4j.Log4j2;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

@Environment(value = EnvType.CLIENT)
@Log4j2
public abstract class DrawingUtils {

    /**
     * Attempts to draw text with a scrolling list of colors. If no color points are found, the color held by the text will be fed to the gradientTextRenderer.
     */
    public static void drawCenteredGradientText(DrawContext context, GradientTextRenderer gradientTextRenderer, Text text, int centerX, int y, int transparency) {
        if (text instanceof GradientText) {
            drawCenteredGradientText(context, gradientTextRenderer, (GradientText) text, centerX, y, transparency);
        } else {
            int color = ColorUtils.getDecimalFromHex(text.getStyle().getColor().getHexCode());
            GradientText gradientText = GradientText.copyOf(text).setColorPoints(color);
            drawCenteredGradientText(context, gradientTextRenderer, gradientText, centerX, y, transparency);
        }
    }

    public static void drawCenteredGradientText(DrawContext context, GradientTextRenderer gradientTextRenderer, GradientText gradientText, int centerX, int y, int transparency) {
        context.drawTextWithShadow(
                gradientTextRenderer.feedPoints(gradientText.getPoints()),
                gradientText,
                (centerX - gradientTextRenderer.getWidth(gradientText.asOrderedText()) / 2),
                y,
                transparency);
    }

    public void drawRectWithOutline(DrawContext context, int x, int y, int width, int height, int color) {
        drawRectWithOutline(context, x, y, width, height, color, color);
    }

    public void drawRectWithOutline(DrawContext context, int x, int y, int width, int height, int rectColor, int outlineColor) {
        context.fill(x, y, x + width, y + height, rectColor);

        context.drawHorizontalLine(x, x + width - 1, y, outlineColor);
        context.drawVerticalLine(x + width - 1, y, y + height - 1, outlineColor);
        context.drawHorizontalLine(x, x + width - 1, y + height - 1, outlineColor);
        context.drawVerticalLine(x, y, y + height - 1, outlineColor);
    }
}
