package evergoodteam.chassis.client.gui.text;

import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j2;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * Modified version of {@link TextRenderer} with changes to support individual letter coloring
 */
@Log4j2
public class GradientTextRenderer extends TextRenderer {

    private static final Vector3f FORWARD_SHIFT = new Vector3f(0.0F, 0.0F, 0.03F);
    public final Function<Identifier, FontStorage> fontStorageAccessor;
    public final TextHandler handler;
    final boolean validateAdvance;
    private List<Integer> points;

    public GradientTextRenderer(Function<Identifier, FontStorage> fontStorageAccessor, TextHandler handler) {
        this(fontStorageAccessor, handler, false);
    }

    public GradientTextRenderer(Function<Identifier, FontStorage> fontStorageAccessor, TextHandler handler, boolean validateAdvance) {
        super(fontStorageAccessor, validateAdvance);
        this.fontStorageAccessor = fontStorageAccessor;
        this.validateAdvance = validateAdvance;
        this.handler = handler;
    }

    public FontStorage getFontStorage(Identifier id) {
        return this.fontStorageAccessor.apply(id);
    }

    public int getWidth(OrderedText text) {
        return MathHelper.ceil(this.handler.getWidth(text));
    }

    public GradientTextRenderer attemptFeedingPoints(Text text) {
        if (text instanceof GradientText) this.points = ((GradientText) text).getPoints();
        return this;
    }

    public GradientTextRenderer feedPoints(List<Integer> points) {
        this.points = points;
        return this;
    }

    // Redirect drawing without AW
    @Override
    public int draw(OrderedText text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumers, TextLayerType layerType, int backgroundColor, int light) {
        return this.drawInternal(text, x, y, color, shadow, matrix, vertexConsumers, layerType, backgroundColor, light);
    }

    public int drawInternal(OrderedText text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumerProvider, TextRenderer.TextLayerType layerType, int backgroundColor, int light) {
        color = GradientTextRenderer.tweakTransparency(color);
        Matrix4f matrix4f = new Matrix4f(matrix);
        if (shadow) {
            this.drawLayer(text, x, y, color, true, matrix, vertexConsumerProvider, layerType, backgroundColor, light);
            matrix4f.translate(FORWARD_SHIFT);
        }

        x = this.drawLayer(text, x, y, color, false, matrix4f, vertexConsumerProvider, layerType, backgroundColor, light);
        return (int) x + (shadow ? 1 : 0);
    }

    public static int tweakTransparency(int argb) {
        if ((argb & 0xFC000000) == 0) {
            return argb | 0xFF000000;
        }
        return argb;
    }

    private float drawLayer(OrderedText text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumerProvider, TextRenderer.TextLayerType layerType, int underlineColor, int light) {
        GradientTextRenderer.GradientDrawer drawer = new GradientTextRenderer.GradientDrawer(vertexConsumerProvider, x, y, color, shadow, matrix, layerType, light, this.points);
        text.accept(drawer);
        return drawer.drawLayer(underlineColor, x);
    }

    public void drawGlyph(GlyphRenderer glyphRenderer, boolean bold, boolean italic, float weight, float x, float y, Matrix4f matrix, VertexConsumer vertexConsumer, float red, float green, float blue, float alpha, int light) {
        glyphRenderer.draw(italic, x, y, matrix, vertexConsumer, red, green, blue, alpha, light);
        if (bold) {
            glyphRenderer.draw(italic, x + weight, y, matrix, vertexConsumer, red, green, blue, alpha, light);
        }
    }

    @Environment(EnvType.CLIENT)
    public class GradientDrawer implements CharacterVisitor {
        final VertexConsumerProvider vertexConsumers;
        private final boolean shadow;
        private final float brightnessMultiplier;
        private final float red;
        private final float green;
        private final float blue;
        private final float alpha;
        private final Matrix4f matrix;
        private final TextRenderer.TextLayerType layerType;
        private final int light;
        float x;
        float y;
        @Nullable
        private List<GlyphRenderer.Rectangle> rectangles;
        public List<Integer> gradients;
        private int index = 0;

        private void addRectangle(GlyphRenderer.Rectangle rectangle) {
            if (this.rectangles == null) {
                this.rectangles = Lists.newArrayList();
            }

            this.rectangles.add(rectangle);
        }

        public GradientDrawer(VertexConsumerProvider vertexConsumers, float x, float y, int color, boolean shadow, Matrix4f matrix, TextRenderer.TextLayerType layerType, int light, List<Integer> points) {
            this.vertexConsumers = vertexConsumers;
            this.x = x;
            this.y = y;
            this.shadow = shadow;
            this.brightnessMultiplier = shadow ? 0.25F : 1.0F;
            this.red = (float) (color >> 16 & 255) / 255.0F * this.brightnessMultiplier;
            this.green = (float) (color >> 8 & 255) / 255.0F * this.brightnessMultiplier;
            this.blue = (float) (color & 255) / 255.0F * this.brightnessMultiplier;
            this.alpha = (float) (color >> 24 & 255) / 255.0F;
            this.matrix = matrix;
            this.layerType = layerType;
            this.light = light;
            this.gradients = points;
        }

        public boolean accept(int i, Style style, int j) {
            FontStorage fontStorage = GradientTextRenderer.this.getFontStorage(style.getFont());
            Glyph glyph = fontStorage.getGlyph(j, GradientTextRenderer.this.validateAdvance);
            GlyphRenderer glyphRenderer = style.isObfuscated() && j != 32 ? fontStorage.getObfuscatedGlyphRenderer(glyph) : fontStorage.getGlyphRenderer(j);

            boolean bl = style.isBold();
            float f = this.alpha;

            TextColor textColor = style.getColor();

            float g;
            float h;
            float l;

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
                g = (float) (k >> 16 & 255) / 255.0F * this.brightnessMultiplier;
                h = (float) (k >> 8 & 255) / 255.0F * this.brightnessMultiplier;
                l = (float) (k & 255) / 255.0F * this.brightnessMultiplier;
            } else {
                g = this.red;
                h = this.green;
                l = this.blue;
            }

            float n;
            float m;
            if (!(glyphRenderer instanceof EmptyGlyphRenderer)) {
                m = bl ? glyph.getBoldOffset() : 0.0F;
                n = this.shadow ? glyph.getShadowOffset() : 0.0F;
                VertexConsumer vertexConsumer = this.vertexConsumers.getBuffer(glyphRenderer.getLayer(this.layerType));
                GradientTextRenderer.this.drawGlyph(glyphRenderer, bl, style.isItalic(), m, this.x + n, this.y + n, this.matrix, vertexConsumer, g, h, l, f, this.light);
            }

            m = glyph.getAdvance(bl);
            n = this.shadow ? 1.0F : 0.0F;
            if (style.isStrikethrough()) {
                this.addRectangle(new GlyphRenderer.Rectangle(this.x + n - 1.0F, this.y + n + 4.5F, this.x + n + m, this.y + n + 4.5F - 1.0F, 0.01F, g, h, l, f));
            }

            if (style.isUnderlined()) {
                this.addRectangle(new GlyphRenderer.Rectangle(this.x + n - 1.0F, this.y + n + 9.0F, this.x + n + m, this.y + n + 9.0F - 1.0F, 0.01F, g, h, l, f));
            }

            this.x += m;
            return true;
        }

        public float drawLayer(int underlineColor, float x) {
            if (underlineColor != 0) {
                float f = (float) (underlineColor >> 24 & 255) / 255.0F;
                float g = (float) (underlineColor >> 16 & 255) / 255.0F;
                float h = (float) (underlineColor >> 8 & 255) / 255.0F;
                float i = (float) (underlineColor & 255) / 255.0F;
                this.addRectangle(new GlyphRenderer.Rectangle(x - 1.0F, this.y + 9.0F, this.x + 1.0F, this.y - 1.0F, 0.01F, g, h, i, f));
            }

            if (this.rectangles != null) {
                GlyphRenderer glyphRenderer = GradientTextRenderer.this.getFontStorage(Style.DEFAULT_FONT_ID).getRectangleRenderer();
                VertexConsumer vertexConsumer = this.vertexConsumers.getBuffer(glyphRenderer.getLayer(this.layerType));
                Iterator var9 = this.rectangles.iterator();

                while (var9.hasNext()) {
                    GlyphRenderer.Rectangle rectangle = (GlyphRenderer.Rectangle) var9.next();
                    glyphRenderer.drawRectangle(rectangle, this.matrix, vertexConsumer, this.light);
                }
            }

            return this.x;
        }
    }
}
