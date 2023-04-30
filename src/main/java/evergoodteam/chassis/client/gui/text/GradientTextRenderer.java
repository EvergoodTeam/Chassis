package evergoodteam.chassis.client.gui.text;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.*;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

/**
 * Modified version of {@link TextRenderer} with changes to support individual letter coloring
 */
public class GradientTextRenderer {

    public static final Vec3f FORWARD_SHIFT = new Vec3f(0.0f, 0.0f, 0.03f);
    public final Function<Identifier, FontStorage> fontStorageAccessor;
    public final TextHandler handler;
    private List<Integer> points;

    public GradientTextRenderer(Function<Identifier, FontStorage> fontStorageAccessor, TextHandler handler) {
        this.fontStorageAccessor = fontStorageAccessor;
        this.handler = handler;
    }

    public FontStorage getFontStorage(Identifier id) {
        return this.fontStorageAccessor.apply(id);
    }

    public int getWidth(OrderedText text) {
        return MathHelper.ceil(this.handler.getWidth(text));
    }

    public int drawWithShadow(MatrixStack matrices, Text text, @Nullable List<Integer> points, float x, float y, int transparency) {
        if (text instanceof GradientText) this.points = ((GradientText) text).getPoints();
        else this.points = points;
        return this.draw(text, x, y, transparency, matrices.peek().getPositionMatrix(), true);
    }

    public int draw(Text text, float x, float y, int color, Matrix4f matrix, boolean shadow) {
        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
        int i = this.draw(text, x, y, color, shadow, matrix, immediate, false, 0, LightmapTextureManager.MAX_LIGHT_COORDINATE);
        immediate.draw();
        return i;
    }

    public int draw(Text text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumers, boolean seeThrough, int backgroundColor, int light) {
        return this.drawInternal(text, x, y, color, shadow, matrix, vertexConsumers, seeThrough, backgroundColor, light);
    }

    public int drawInternal(Text text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumerProvider, boolean seeThrough, int backgroundColor, int light) {
        color = tweakTransparency(color);
        Matrix4f matrix4f = matrix.copy();
        if (shadow) {
            this.drawLayer(text, x, y, color, true, matrix, vertexConsumerProvider, seeThrough, backgroundColor, light);
            matrix4f.addToLastColumn(FORWARD_SHIFT);
        }
        x = this.drawLayer(text, x, y, color, false, matrix4f, vertexConsumerProvider, seeThrough, backgroundColor, light);
        return (int) x + (shadow ? 1 : 0);
    }

    public static int tweakTransparency(int argb) {
        if ((argb & 0xFC000000) == 0) {
            return argb | 0xFF000000;
        }
        return argb;
    }

    public float drawLayer(Text text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumerProvider, boolean seeThrough, int underlineColor, int light) {
        CustomDrawer drawer = new CustomDrawer(vertexConsumerProvider, x, y, color, shadow, matrix, seeThrough, light, this.points);
        text.asOrderedText().accept(drawer);
        return drawer.drawLayer(underlineColor, x);
    }

    public void drawGlyph(GlyphRenderer glyphRenderer, boolean bold, boolean italic, float weight, float x, float y, Matrix4f matrix, VertexConsumer vertexConsumer, float red, float green, float blue, float alpha, int light) {
        glyphRenderer.draw(italic, x, y, matrix, vertexConsumer, red, green, blue, alpha, light);
        if (bold) {
            glyphRenderer.draw(italic, x + weight, y, matrix, vertexConsumer, red, green, blue, alpha, light);
        }
    }

    @Environment(value = EnvType.CLIENT)
    public class CustomDrawer implements CharacterVisitor {

        VertexConsumerProvider vertexConsumers;
        public boolean shadow;
        public float brightnessMultiplier;
        public float red;
        public float green;
        public float blue;
        public float alpha;
        public Matrix4f matrix;
        public TextRenderer.TextLayerType layerType;
        public int light;
        float x;
        float y;
        public List<GlyphRenderer.Rectangle> rectangles;
        public List<Integer> gradients;
        private int index = 0;

        public CustomDrawer(VertexConsumerProvider vertexConsumers, float x, float y, int placeholder, boolean shadow, Matrix4f matrix, boolean seeThrough, int light, List<Integer> gradients) {
            this(vertexConsumers, x, y, placeholder, shadow, matrix, seeThrough ? TextRenderer.TextLayerType.SEE_THROUGH : TextRenderer.TextLayerType.NORMAL, light);
            this.gradients = gradients;
        }

        public CustomDrawer(VertexConsumerProvider vertexConsumers, float x, float y, int color, boolean shadow, Matrix4f matrix, boolean seeThrough, int light) {
            this(vertexConsumers, x, y, color, shadow, matrix, seeThrough ? TextRenderer.TextLayerType.SEE_THROUGH : TextRenderer.TextLayerType.NORMAL, light);
        }

        public CustomDrawer(VertexConsumerProvider vertexConsumers, float x, float y, int color, boolean shadow, Matrix4f matrix, TextRenderer.TextLayerType layerType, int light) {
            this.vertexConsumers = vertexConsumers;
            this.x = x;
            this.y = y;
            this.shadow = shadow;
            this.brightnessMultiplier = shadow ? 0.25f : 1.0f;
            this.red = (float) (color >> 16 & 0xFF) / 255.0f * this.brightnessMultiplier;
            this.green = (float) (color >> 8 & 0xFF) / 255.0f * this.brightnessMultiplier;
            this.blue = (float) (color & 0xFF) / 255.0f * this.brightnessMultiplier;
            this.alpha = (float) (color >> 24 & 0xFF) / 255.0f;
            this.matrix = matrix;
            this.layerType = layerType;
            this.light = light;
        }

        public void addRectangle(GlyphRenderer.Rectangle rectangle) {
            if (this.rectangles == null) {
                this.rectangles = Lists.newArrayList();
            }
            this.rectangles.add(rectangle);
        }

        @Override
        public boolean accept(int i, Style style, int j) {

            float n;
            float l;
            float h;
            float g;

            FontStorage fontStorage = GradientTextRenderer.this.getFontStorage(style.getFont());
            Glyph glyph = fontStorage.getGlyph(j);
            GlyphRenderer glyphRenderer = style.isObfuscated() && j != 32 ? fontStorage.getObfuscatedGlyphRenderer(glyph) : fontStorage.getGlyphRenderer(j);

            boolean bl = style.isBold();
            float f = this.alpha;

            TextColor textColor;

            if (gradients == null || gradients.isEmpty())
                textColor = Style.EMPTY.withColor(Formatting.WHITE).getColor();
            else if (gradients.size() == 1) textColor = TextHelper.getColor(gradients.get(0));
            else {
                if (index == gradients.size()) index = 0;
                textColor = Style.EMPTY.withColor(gradients.get(index)).getColor();
                index++;
            }

            if (textColor != null) {
                int k = textColor.getRgb();
                g = (float) (k >> 16 & (LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE | 0xF)) / 255.0f * this.brightnessMultiplier;
                h = (float) (k >> 8 & (LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE | 0xF)) / 255.0f * this.brightnessMultiplier;
                l = (float) (k & (LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE | 0xF)) / 255.0f * this.brightnessMultiplier;
            } else {
                g = this.red;
                h = this.green;
                l = this.blue;
            }

            if (!(glyphRenderer instanceof EmptyGlyphRenderer)) {
                float m = bl ? glyph.getBoldOffset() : 0.0f;
                n = this.shadow ? glyph.getShadowOffset() : 0.0f;
                VertexConsumer vertexConsumer = this.vertexConsumers.getBuffer(glyphRenderer.getLayer(this.layerType));
                GradientTextRenderer.this.drawGlyph(glyphRenderer, bl, style.isItalic(), m, this.x + n, this.y + n, this.matrix, vertexConsumer, g, h, l, f, this.light);
            }

            // Lines
            float m = glyph.getAdvance(bl);
            float f2 = n = this.shadow ? 1.0f : 0.0f;
            if (style.isStrikethrough()) {
                this.addRectangle(new GlyphRenderer.Rectangle(this.x + n - 1.0f, this.y + n + 4.5f, this.x + n + m, this.y + n + 4.5f - 1.0f, 0.01f, g, h, l, f));
            }
            if (style.isUnderlined()) {
                this.addRectangle(new GlyphRenderer.Rectangle(this.x + n - 1.0f, this.y + n + 9.0f, this.x + n + m, this.y + n + 9.0f - 1.0f, 0.01f, g, h, l, f));
            }
            this.x += m;
            return true;
        }

        public float drawLayer(int underlineColor, float x) {
            if (underlineColor != 0) {
                float f = (float) (underlineColor >> 24 & 0xFF) / 255.0f;
                float g = (float) (underlineColor >> 16 & 0xFF) / 255.0f;
                float h = (float) (underlineColor >> 8 & 0xFF) / 255.0f;
                float i = (float) (underlineColor & 0xFF) / 255.0f;
                this.addRectangle(new GlyphRenderer.Rectangle(x - 1.0f, this.y + 9.0f, this.x + 1.0f, this.y - 1.0f, 0.01f, g, h, i, f));
            }
            if (this.rectangles != null) {
                GlyphRenderer glyphRenderer = GradientTextRenderer.this.getFontStorage(Style.DEFAULT_FONT_ID).getRectangleRenderer();
                VertexConsumer vertexConsumer = this.vertexConsumers.getBuffer(glyphRenderer.getLayer(this.layerType));
                for (GlyphRenderer.Rectangle rectangle : this.rectangles) {
                    glyphRenderer.drawRectangle(rectangle, this.matrix, vertexConsumer, this.light);
                }
            }
            return this.x;
        }
    }
}
