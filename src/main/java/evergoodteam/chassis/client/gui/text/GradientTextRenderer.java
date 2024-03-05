package evergoodteam.chassis.client.gui.text;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.text.CharacterVisitor;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * Modified version of {@link TextRenderer} with changes to support individual letter coloring.
 */
public class GradientTextRenderer extends TextRenderer {

    private static final Vector3f FORWARD_SHIFT = new Vector3f(0.0F, 0.0F, 0.03F);
    public final Function<Identifier, FontStorage> fontStorageAccessor;
    public final TextHandler handler;
    public final boolean validateAdvance;
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

    public GradientTextRenderer setPoints(@Nullable List<Integer> points) {
        this.points = points;
        return this;
    }

    public List<Integer> getPoints() {
        return this.points;
    }

    @Override
    public float drawLayer(OrderedText text, float x, float y, int fallbackColor, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumerProvider, TextRenderer.TextLayerType layerType, int underlineColor, int light) {
        GradientTextRenderer.GradientDrawer drawer = new GradientTextRenderer.GradientDrawer(vertexConsumerProvider, x, y, fallbackColor, shadow, matrix, layerType, light, this.points);
        text.accept(drawer);
        return drawer.drawLayer(underlineColor, x);
    }

    @Environment(EnvType.CLIENT)
    public class GradientDrawer implements CharacterVisitor {
        final VertexConsumerProvider vertexConsumers;
        private final boolean shadow;
        private final float brightnessMultiplier;
        private final float r;
        private final float g;
        private final float b;
        private final float alpha;
        private final Matrix4f matrix;
        private final TextRenderer.TextLayerType layerType;
        private final int light;
        float x;
        float y;
        @Nullable
        private List<GlyphRenderer.Rectangle> rectangles;
        public List<Integer> colorPoints;
        private int index = 0;

        public GradientDrawer(VertexConsumerProvider vertexConsumers, float x, float y, int fallbackColor, boolean shadow, Matrix4f matrix, TextRenderer.TextLayerType layerType, int light, List<Integer> points) {
            this.vertexConsumers = vertexConsumers;
            this.x = x;
            this.y = y;
            this.shadow = shadow;
            this.brightnessMultiplier = shadow ? 0.25F : 1.0F;
            this.r = (float) (fallbackColor >> 16 & 255) / 255.0F * this.brightnessMultiplier;
            this.g = (float) (fallbackColor >> 8 & 255) / 255.0F * this.brightnessMultiplier;
            this.b = (float) (fallbackColor & 255) / 255.0F * this.brightnessMultiplier;
            this.alpha = (float) (fallbackColor >> 24 & 255) / 255.0F;
            this.matrix = matrix;
            this.layerType = layerType;
            this.light = light;
            this.colorPoints = points;
        }

        public boolean accept(int i, Style style, int j) {
            FontStorage fontStorage = GradientTextRenderer.this.getFontStorage(style.getFont());
            Glyph glyph = fontStorage.getGlyph(j, GradientTextRenderer.this.validateAdvance);
            GlyphRenderer glyphRenderer = style.isObfuscated() && j != 32 ? fontStorage.getObfuscatedGlyphRenderer(glyph) : fontStorage.getGlyphRenderer(j);

            TextColor textColor = style.getColor();
            boolean isBold = style.isBold();
            float red = this.r;
            float green = this.g;
            float blue = this.b;

            if (colorPoints != null && !colorPoints.isEmpty()) {
                if (index == colorPoints.size()) index = 0;
                textColor = TextHelper.getTextColor(colorPoints.get(index)); // set textColor as a derivative of colorPoints
                index++;
            }

            // update rgb and apply brightness multiplier, regardless of it being a derivative, otherwise normal formatted Text will just be white
            if (textColor != null) {
                int rgb = textColor.getRgb();
                red = (float) (rgb >> 16 & 255) / 255.0F * this.brightnessMultiplier;
                green = (float) (rgb >> 8 & 255) / 255.0F * this.brightnessMultiplier;
                blue = (float) (rgb & 255) / 255.0F * this.brightnessMultiplier;
            }

            float widthOffset;
            float heightOffset;
            if (!(glyphRenderer instanceof EmptyGlyphRenderer)) {
                heightOffset = isBold ? glyph.getBoldOffset() : 0.0F;
                widthOffset = this.shadow ? glyph.getShadowOffset() : 0.0F;

                VertexConsumer vertexConsumer = this.vertexConsumers.getBuffer(glyphRenderer.getLayer(this.layerType));
                GradientTextRenderer.this.drawGlyph(glyphRenderer, isBold, style.isItalic(), heightOffset, this.x + widthOffset, this.y + widthOffset, this.matrix, vertexConsumer, red, green, blue, alpha, this.light);
            }

            heightOffset = glyph.getAdvance(isBold);
            widthOffset = this.shadow ? 1.0F : 0.0F;
            if (style.isStrikethrough()) {
                this.addRectangle(new GlyphRenderer.Rectangle(this.x + widthOffset - 1.0F, this.y + widthOffset + 4.5F, this.x + widthOffset + heightOffset, this.y + widthOffset + 4.5F - 1.0F, 0.01F, red, green, blue, alpha));
            }
            if (style.isUnderlined()) {
                this.addRectangle(new GlyphRenderer.Rectangle(this.x + widthOffset - 1.0F, this.y + widthOffset + 9.0F, this.x + widthOffset + heightOffset, this.y + widthOffset + 9.0F - 1.0F, 0.01F, red, green, blue, alpha));
            }

            this.x += heightOffset;
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

        public void addRectangle(GlyphRenderer.Rectangle rectangle) {
            if (this.rectangles == null) {
                this.rectangles = Lists.newArrayList();
            }

            this.rectangles.add(rectangle);
        }
    }
}
