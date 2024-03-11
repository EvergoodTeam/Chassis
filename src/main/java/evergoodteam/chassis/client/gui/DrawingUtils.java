package evergoodteam.chassis.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import evergoodteam.chassis.client.ChassisClient;
import evergoodteam.chassis.client.gui.text.GradientText;
import evergoodteam.chassis.client.gui.text.GradientTextRenderer;
import evergoodteam.chassis.util.StringUtils;
import evergoodteam.chassis.util.gui.ColorUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

import java.util.List;
import java.util.stream.Collectors;

@Environment(value = EnvType.CLIENT)
public abstract class DrawingUtils {

    public static final Identifier CORNERS = new Identifier("chassis", "textures/gui/corner.png");
    public static final int SPACER = 2;

    public void drawGradientFrameWithBackground(DrawContext context, int x, int y, int width, int height, int start, int end, int outline) {
        drawGradientRectangle(context, x, y, width, height, start, end);
        drawFrame(context, x, y, width, height, outline);
    }

    public void drawFrameWithBackground(DrawContext context, int x, int y, int width, int height, int background, int outline) {
        drawRectangle(context, x, y, width, height, background);
        drawFrame(context, x, y, width, height, outline);
    }

    public void drawFrame(DrawContext context, int x, int y, int width, int height, int outline) {
        drawFrame(context, x, y, width, height, SPACER, outline);
    }

    public void drawFrame(DrawContext context, int x, int y, int width, int height, int spacer, int color) {
        float[] comps = ColorUtils.ARGB.getFloatARGBComponents(color);
        drawRectOutlineWithoutCorners(context, x + spacer, y + spacer, width - spacer * 2, height - spacer * 2, 9, color);

        drawColoredTexture(context, CORNERS, x + spacer, y + spacer, 0, 0, 9, 9, 19, 19, comps[0], comps[1], comps[2], comps[3]);
        drawColoredTexture(context, CORNERS, x + width - 9 - spacer, y + spacer, 10, 0, 9, 9, 19, 19, comps[0], comps[1], comps[2], comps[3]);
        drawColoredTexture(context, CORNERS, x + width - 9 - spacer, y + height - 9 - spacer, 10, 10, 9, 9, 19, 19, comps[0], comps[1], comps[2], comps[3]);
        drawColoredTexture(context, CORNERS, x + spacer, y + height - 9 - spacer, 0, 10, 9, 9, 19, 19, comps[0], comps[1], comps[2], comps[3]);
    }

    public void drawColoredTexture(DrawContext context, Identifier texture, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight, float alpha, float red, float green, float blue) {
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShader(GameRenderer::getPositionColorTexProgram);
        RenderSystem.enableBlend();
        Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();

        float u1 = (u + 0.0f) / (float) textureWidth;
        float u2 = (u + (float) width) / (float) textureWidth;
        float v1 = (v + 0.0f) / (float) textureHeight;
        float v2 = (v + (float) height) / (float) textureHeight;

        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
        bufferBuilder.vertex(matrix4f, x, y, 0).color(red, green, blue, alpha).texture(u1, v1).next();
        bufferBuilder.vertex(matrix4f, x, y + height, 0).color(red, green, blue, alpha).texture(u1, v2).next();
        bufferBuilder.vertex(matrix4f, x + width, y + height, 0).color(red, green, blue, alpha).texture(u2, v2).next();
        bufferBuilder.vertex(matrix4f, x + width, y, 0).color(red, green, blue, alpha).texture(u2, v1).next();
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        RenderSystem.disableBlend();
    }

    /**
     * Draws a saturation/brightness square with the specified hue, which should be a float within the range (0.0 - 1.0).
     */
    public void drawSaturationBrightnessSquare(DrawContext context, int x, int y, int width, int height, float hue) {
        Matrix4f matrix = context.getMatrices().peek().getPositionMatrix();
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex(matrix, x, y, 0).color(hue, 0f, 1f, 1f).next();
        buffer.vertex(matrix, x, y + height, 0).color(hue, 0f, 0f, 1f).next();
        buffer.vertex(matrix, x + width, y + height, 0).color(hue, 1f, 0f, 1f).next();
        buffer.vertex(matrix, x + width, y, 0).color(hue, 1f, 1f, 1f).next();

        ChassisClient.HSV_PROGRAM.use(); // Converts above hsv (hsb) to rgb
        Tessellator.getInstance().draw();
    }

    /**
     * Draws a hue spectrum, which can be vertical if specified.
     */
    public void drawHueSpectrum(DrawContext context, int x, int y, int width, int height, boolean vertical) {
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        Matrix4f matrix = context.getMatrices().peek().getPositionMatrix();

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        buffer.vertex(matrix, x, y, 0).color(0f, 1f, 1f, 1f).next();
        buffer.vertex(matrix, x, y + height, 0).color(vertical ? 1f : 0f, 1f, 1f, 1f).next();
        buffer.vertex(matrix, x + width, y + height, 0).color(1f, 1f, 1f, 1f).next();
        buffer.vertex(matrix, x + width, y, 0).color(vertical ? 0f : 1f, 1f, 1f, 1f).next();

        /* Magenta top reds bottom, needs 1 - hueValue when interacting
        buffer.vertex(matrix, x, y, 0).color(1f, 1f, 1f, 1f).next();
        buffer.vertex(matrix, x, y + height, 0).color(vertical ? 0f : 1f, 1f, 1f, 1f).next();
        buffer.vertex(matrix, x + width, y + height, 0).color(0f, 1f, 1f, 1f).next();
        buffer.vertex(matrix, x + width, y, 0).color(vertical ? 1f : 0f, 1f, 1f, 1f).next();
        */

        ChassisClient.HSV_PROGRAM.use(); // Converts above hsv (hsb) to rgb
        Tessellator.getInstance().draw();
    }

    public static void drawItemWithCount(MinecraftClient client, DrawContext drawContext, ItemStack stack, float x, float y) {
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

    public void drawWrappedTooltip(TextRenderer textRenderer, DrawContext context, Text text, int x, int y) {
        List<Text> result = StringUtils.wrapString(text.getString(), 18).stream().map(line -> Text.literal(line).setStyle(text.getStyle())).collect(Collectors.toList());
        context.drawTooltip(textRenderer, result, x, y);
    }

    public void drawOutlinedText(TextRenderer textRenderer, DrawContext context, Text text, float x, float y, int color, int outlineColor) {
        int light = 0xF000F0;
        textRenderer.drawWithOutline(text.asOrderedText(), x, y, color, outlineColor, context.getMatrices().peek().getPositionMatrix(), context.getVertexConsumers(), light);
    }

    public static void drawCenteredGradientText(DrawContext context, GradientTextRenderer gradientTextRenderer, Text text, int centerX, int y, int transparency) {
        drawCenteredGradientText(context, gradientTextRenderer, (GradientText) text, centerX, y, transparency);
    }

    /**
     * Attempts to draw text with a scrolling list of colors. If no color points are found, the color held by the text will be used
     */
    public static void drawCenteredGradientText(DrawContext context, GradientTextRenderer gradientTextRenderer, GradientText gradientText, int centerX, int y, int transparency) {
        context.drawTextWithShadow(
                gradientTextRenderer.setPoints(gradientText.getPoints()),
                gradientText,
                (centerX - gradientTextRenderer.getWidth(gradientText.asOrderedText()) / 2),
                y,
                transparency);
    }

    public void drawGradientRectWithOutline(DrawContext context, int x, int y, int width, int height, int start, int end) {
        drawGradientRectangle(context, x, y, width, height, start, end);
        drawGradientRectOutline(context, x, y, width, height, start, end);
    }

    public void drawRectWithOutline(DrawContext context, int x, int y, int width, int height, int color) {
        drawRectWithOutline(context, x, y, width, height, color, color);
    }

    public void drawRectWithOutline(DrawContext context, int x, int y, int width, int height, int rectColor, int outlineColor) {
        drawRectangle(context, x, y, width, height, rectColor);
        drawRectOutline(context, x, y, width, height, outlineColor);
    }

    // Used for the color picker "dot"
    public void drawRoundedRect(DrawContext context, int x, int y, int width, int height, int color) {
        context.drawHorizontalLine(x + 1, x + width - 2, y, color);
        context.drawVerticalLine(x + width - 1, y, y + height - 1, color);
        context.drawHorizontalLine(x + 1, x + width - 2, y + height - 1, color);
        context.drawVerticalLine(x, y, y + height - 1, color);
    }

    public void drawGradientRectangle(DrawContext context, int x, int y, int width, int height, int start, int end) {
        context.chassisFillHorizontalGradient(x, y, x + width, y + height, start, end);
    }

    public void drawRectangle(DrawContext context, int x, int y, int width, int height, int color) {
        context.fill(x, y, x + width, y + height, color);
    }

    public void drawGradientRectOutline(DrawContext context, int x, int y, int width, int height, int start, int end) {
        context.chassisDrawGradientLine(x, x + width - 1, y, start, end);
        context.drawVerticalLine(x + width - 1, y, y + height - 1, end);
        context.chassisDrawGradientLine(x, x + width - 1, y + height - 1, start, end);
        context.drawVerticalLine(x, y, y + height - 1, start);
    }

    /**
     * @param space pixels left out at the corners
     */
    public void drawRectOutlineWithoutCorners(DrawContext context, int x, int y, int width, int height, int space, int color) {
        context.drawHorizontalLine(x + space, x + width - space - 1, y, color);
        context.drawVerticalLine(x, y + space - 1, y + height - space, color);
        context.drawHorizontalLine(x + space, x + width - space - 1, y + height - 1, color);
        context.drawVerticalLine(x + width - 1, y + space - 1, y + height - space, color);
    }

    public void drawRectOutline(DrawContext context, int x, int y, int width, int height, int outlineColor) {
        context.drawHorizontalLine(x, x + width - 1, y, outlineColor);
        context.drawVerticalLine(x + width - 1, y, y + height - 1, outlineColor);
        context.drawHorizontalLine(x, x + width - 1, y + height - 1, outlineColor);
        context.drawVerticalLine(x, y, y + height - 1, outlineColor);
    }
}