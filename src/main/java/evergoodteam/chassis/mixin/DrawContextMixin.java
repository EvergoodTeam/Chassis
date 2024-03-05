package evergoodteam.chassis.mixin;

import evergoodteam.chassis.client.gui.ChassisDrawContext;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipBackgroundRenderer;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.ColorHelper;
import org.joml.Matrix4f;
import org.joml.Vector2ic;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(DrawContext.class)
public abstract class DrawContextMixin implements ChassisDrawContext {

    @Final
    @Shadow
    private MatrixStack matrices;
    @Final
    @Shadow
    private VertexConsumerProvider.Immediate vertexConsumers;

    @Shadow
    private void tryDraw() {}

    @Shadow
    public abstract int getScaledWindowWidth();

    @Shadow
    public abstract int getScaledWindowHeight();

    @Shadow
    @Deprecated
    public abstract void draw(Runnable drawCallback);

    @Override
    public void chassisDrawTooltipWithoutJump(TextRenderer textRenderer, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner) {
        TooltipComponent tooltipComponent2;
        int r;
        if (components.isEmpty()) {
            return;
        }
        int i = 0;
        int j = components.size() == 1 ? -2 : 0;
        for (TooltipComponent tooltipComponent : components) {
            int k = tooltipComponent.getWidth(textRenderer);
            if (k > i) {
                i = k;
            }
            j += tooltipComponent.getHeight();
        }

        int l = i;
        int m = j;
        Vector2ic vector2ic = positioner.getPosition(this.getScaledWindowWidth(), this.getScaledWindowHeight(), x, y, l, m);
        int n = vector2ic.x();
        int o = vector2ic.y();
        this.matrices.push();

        this.draw(() -> TooltipBackgroundRenderer.render((DrawContext) (Object) this, n, o, l, m, 400));
        this.matrices.translate(0.0f, 0.0f, 400.0f);
        int q = o;
        for (r = 0; r < components.size(); ++r) {
            tooltipComponent2 = components.get(r);
            tooltipComponent2.drawText(textRenderer, n, q, this.matrices.peek().getPositionMatrix(), this.vertexConsumers);
            q += tooltipComponent2.getHeight();
        }
        q = o;
        for (r = 0; r < components.size(); ++r) {
            tooltipComponent2 = components.get(r);
            tooltipComponent2.drawItems(textRenderer, n, q, (DrawContext) (Object) this);
            q += tooltipComponent2.getHeight();
        }

        this.matrices.pop();
    }

    @Override
    public void chassisFillHorizontalGradient(RenderLayer layer, int startX, int startY, int endX, int endY, int colorStart, int colorEnd, int z) {
        VertexConsumer vertexConsumer = this.vertexConsumers.getBuffer(layer);
        this.chassisFillHorizontalGradient(vertexConsumer, startX, startY, endX, endY, z, colorStart, colorEnd);
        this.tryDraw();
    }

    @Override
    public void chassisFillHorizontalGradient(VertexConsumer vertexConsumer, int startX, int startY, int endX, int endY, int z, int colorStart, int colorEnd) {
        float f = (float) ColorHelper.Argb.getAlpha(colorStart) / 255.0F;
        float g = (float) ColorHelper.Argb.getRed(colorStart) / 255.0F;
        float h = (float) ColorHelper.Argb.getGreen(colorStart) / 255.0F;
        float i = (float) ColorHelper.Argb.getBlue(colorStart) / 255.0F;
        float j = (float) ColorHelper.Argb.getAlpha(colorEnd) / 255.0F;
        float k = (float) ColorHelper.Argb.getRed(colorEnd) / 255.0F;
        float l = (float) ColorHelper.Argb.getGreen(colorEnd) / 255.0F;
        float m = (float) ColorHelper.Argb.getBlue(colorEnd) / 255.0F;
        Matrix4f matrix4f = this.matrices.peek().getPositionMatrix();
        vertexConsumer.vertex(matrix4f, (float) startX, (float) startY, (float) z).color(g, h, i, f).next();
        vertexConsumer.vertex(matrix4f, (float) startX, (float) endY, (float) z).color(g, h, i, f).next();
        vertexConsumer.vertex(matrix4f, (float) endX, (float) endY, (float) z).color(k, l, m, j).next();
        vertexConsumer.vertex(matrix4f, (float) endX, (float) startY, (float) z).color(k, l, m, j).next();
    }
}
