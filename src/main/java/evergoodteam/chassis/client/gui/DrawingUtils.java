package evergoodteam.chassis.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import evergoodteam.chassis.client.gui.text.GradientText;
import evergoodteam.chassis.client.gui.text.GradientTextRenderer;
import evergoodteam.chassis.util.gui.ColorUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

import java.util.List;

@Environment(value = EnvType.CLIENT)
public abstract class DrawingUtils {

    public static void drawItemWithCount(MinecraftClient client, DrawContext drawContext, ItemStack stack, float x, float y){
        drawItemWithCount(client, drawContext, stack, x, y, 1f);
    }

    public static void drawItemWithCount(MinecraftClient client, DrawContext drawContext, ItemStack stack, float x, float y, float scale) {
        MatrixStack matrices = drawContext.getMatrices();
        matrices.push();
        matrices.translate(x, y, 0.f);
        matrices.scale(scale, scale, 1);

        //DiffuseLighting.enableForLevel(matrixStack.peek().getPositionMatrix());
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        drawContext.drawItem(stack, 0, 0);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        drawContext.drawItemInSlot(client.textRenderer, stack, 0, 0);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        matrices.pop();
    }

    /**
     * Attempts to draw text with a scrolling list of colors. If no color points are found, the color held by the text will be fed to the gradientTextRenderer.
     */
    public static void drawCenteredGradientText(DrawContext context, GradientTextRenderer gradientTextRenderer, Text text, int centerX, int y, int transparency) {
        if (text instanceof GradientText) {
            drawCenteredGradientText(context, gradientTextRenderer, (GradientText) text, centerX, y, transparency);
        } else {
            GradientText gradientText = GradientText.copyOf(text);
            TextColor color = text.getStyle().getColor();
            if (color != null) {
                gradientText.setColorPoints(List.of(ColorUtils.getDecimalFromHex(color.getHexCode())));
            }
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
