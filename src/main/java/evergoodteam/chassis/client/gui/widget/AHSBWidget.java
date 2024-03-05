package evergoodteam.chassis.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import evergoodteam.chassis.util.gui.ColorAHSB;
import evergoodteam.chassis.util.gui.ColorUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.text.Text;

/**
 * HSV/HSB colour picker widget with a saturation/brightness square and hue/alpha sliders
 */
@Environment(value = EnvType.CLIENT)
public class AHSBWidget extends WidgetParentBase {

    public static final Text SATURATION = Text.translatable("config.chassis.ahsb.sb");
    public static final Text HUE = Text.translatable("config.chassis.ahsb.hue");
    public static final Text ALPHA = Text.translatable("config.chassis.ahsb.alpha");
    private static final int alphaWidth = 20;
    private static final int hueWidth = 20;
    private static final int pickerWidth = 150 - space * 2;
    private static final int width = alphaWidth + space + hueWidth + space + pickerWidth;
    private static final int height = 100;

    public ColorAHSB ashb;
    public AlphaSlider alphaSlider;
    public HueSlider hueSlider;
    public SquarePicker sbPicker;

    public AHSBWidget(int x, int color) {
        this(x, 0, width, height, color);
    }

    AHSBWidget(int x, int y, int width, int height, int color) {
        super(x, y, width, height, Text.empty());
        this.ashb = new ColorAHSB(color);

        this.alphaSlider = new AlphaSlider(this, 0, 0, alphaWidth, height, Text.empty(), 1 - this.ashb.getAlpha());
        this.hueSlider = new HueSlider(this, 0, 0, hueWidth, height, Text.empty(), this.ashb.getHue()); // 1 - hue for top magenta bottom reds
        this.sbPicker = new SquarePicker(this, 0, 0, pickerWidth, height);
        this.setOrder(alphaSlider, hueSlider, sbPicker);

        this.children.add(this.alphaSlider);
        this.children.add(this.hueSlider);
        this.children.add(this.sbPicker);
    }

    /**
     * Sets the order in which the different components should be arranged
     */
    public void setOrder(WidgetBase first, WidgetBase second, WidgetBase third) {
        first.setX(this.x);
        second.setX(first.getX() + first.getWidth() + space);
        third.setX(second.getX() + second.getWidth() + space);
    }

    public void updateAlphaSlider(float alpha) {
        alphaSlider.setValueSilently(1 - alpha);
    }

    public void updateHueSlider(float hue) { // 1 - hue for top magenta bottom reds
        hueSlider.setValueSilently(hue);
    }

    public void updateSBDot(float saturation, float brightness) {
        sbPicker.setValuesSilently(saturation, 1 - brightness);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
    }

    public boolean isMouseOverComps(double mouseX, double mouseY) {
        if (!this.active) {
            return false;
        }
        return alphaSlider.isMouseOver(mouseX, mouseY) || hueSlider.isMouseOver(mouseX, mouseY) || sbPicker.isMouseOver(mouseX, mouseY);
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
    }

    public static class SquarePicker extends SquarePickerWidget {

        public AHSBWidget parent;

        public SquarePicker(AHSBWidget parent, int x, int y, int width, int height) {
            super(x, y, width, height, Text.empty(), 0, 0, 1);
            this.parent = parent;
            this.valueX = parent.ashb.getSaturation();
            this.valueY = (1 - parent.ashb.getBrightness());
            this.setOrderedTooltip(SATURATION, this.width);
            this.updateMessage();
        }

        @Override
        public void renderSlider(DrawContext context, int mouseX, int mouseY) {
            super.renderSlider(context, mouseX, mouseY);
            drawRectOutline(context, this.x, this.y, this.width, this.height, 0xFF_000000);
            drawSaturationBrightnessSquare(context, this.x + 1, this.y + 1, this.width - 2, this.height - 2, parent.ashb.getHue());
        }

        @Override
        public void onValueUpdate(double newValueX, double newValueY) {
            parent.ashb.setSB((float) newValueX, (float) (1 - newValueY));
        }

        @Override
        public void updateMessage() {
            this.setMessage("%s%% %s%%".formatted(twoDecimalPlaces(this.valueX * 100), twoDecimalPlaces((1 - this.valueY) * 100)));
        }
    }

    public static class HueSlider extends AlphaSlider {

        public HueSlider(AHSBWidget parent, int x, int y, int width, int height, Text message, double value) {
            super(parent, x, y, width, height, message, value, 0, 1); // Flip min max for top magenta bottom reds
            this.setOrderedTooltip(HUE, this.width);
        }

        @Override
        public void renderBack(DrawContext context, int mouseX, int mouseY) {
            drawHueSpectrum(context, this.getX() + 1, this.getY() + 1, hueWidth - 2, height - 2, true);
        }

        @Override
        public void onValueUpdate(double newValue) { // Flipped min/max accounted at constructor
            parent.ashb.setHue((float) newValue);
        }

        @Override
        public void updateMessage() {
            this.setMessage(String.valueOf((int) (parent.ashb.getHue() * 360)));
        }

        // Write regardless of bounds
        @Override
        public void renderCenteredText(DrawContext context, Text text, int x, int y) {
            context.drawCenteredTextWithShadow(textRenderer, text, x, y, ColorUtils.WHITE);
        }
    }

    public static class AlphaSlider extends VerticalSliderWidget {

        public AHSBWidget parent;

        public AlphaSlider(AHSBWidget parent, int x, int y, int width, int height, Text message, double value) {
            this(parent, x, y, width, height, message, value, 1, 0);
        }

        public AlphaSlider(AHSBWidget parent, int x, int y, int width, int height, Text message, double value, double min, double max) {
            super(x, y, width, height, message, value, min, max);
            this.parent = parent;
            this.setOrderedTooltip(ALPHA, textRenderer.getWidth(ALPHA.asOrderedText()));
            this.updateMessage();
        }

        @Override
        public void renderBack(DrawContext context, int mouseX, int mouseY) {
            context.fillGradient(this.x + 1, this.y + 1, this.x + 20 - 1, this.y + 100 - 1, ColorUtils.ARGB.convertARGBToOpaque(parent.ashb.getValue()), ColorUtils.ARGB.convertARGBToTransparent(parent.ashb.getValue()));
        }

        @Override
        public void onValueUpdate(double newValue) { // Flipped min/max accounted at constructor
            parent.ashb.setAlpha((float) newValue);
        }

        @Override
        public void updateMessage() {
            this.setMessage(Text.literal(ColorUtils.Hex.getAlphaFromFloat(parent.ashb.getAlpha())));
        }
    }
}