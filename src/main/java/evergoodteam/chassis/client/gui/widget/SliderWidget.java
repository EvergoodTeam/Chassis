package evergoodteam.chassis.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SliderWidget extends WidgetBase {

    protected static final Identifier TEXTURE = new Identifier("textures/gui/slider.png");
    public Double value;
    public Double min;
    public Double max;
    public int sliderWidth = 8;
    public int sliderHeight = 20;

    public SliderWidget(int x, int y, int width, int height, Text message, double value, double min, double max) {
        super(x, y, width, height, message);
        this.value = value;
        this.min = min;
        this.max = max;
    }

    @Override
    public void renderSlider(DrawContext context, int mouseX, int mouseY) {
        context.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        context.drawNineSlicedTexture(TEXTURE, this.getX(), this.getY(), this.getWidth(), this.getHeight(), 20, 4, 200, 20, 0, this.getYImage(isFocused()));

        this.renderBack(context, mouseX, mouseY);

        context.drawNineSlicedTexture(TEXTURE, this.getX() + (int) (this.value * (double) (this.width - 8)), this.getY(), 8, 20, 20, 4, 200, 20, 0, getTextureV());
        context.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    protected int getTextureV() {
        int i = this.hovered ? 3 : 2;
        return i * 20;
    }

    public void renderBack(DrawContext context, int mouseX, int mouseY) {
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.setValueFromMouse(mouseX);
    }

    @Override
    public void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
        this.setValueFromMouse(mouseX);
        super.onDrag(mouseX, mouseY, deltaX, deltaY);
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        super.playDownSound(MinecraftClient.getInstance().getSoundManager());
    }

    @Override
    public int getYImage(boolean hovered) {
        return 0;
    }

    public void setValueFromMouse(double mouseX) {
        this.setValue((mouseX - (double) (this.x + 4)) / (double) (this.width - sliderWidth));
    }

    public void setValueFromMouseY(double mouseY) {
        this.setValue((mouseY - this.y) / this.height);
    }

    public void setValue(double value) {
        double old = this.value;
        this.value = MathHelper.clamp(value, 0.0, 1.0);
        if (old != this.value) {
            this.onValueUpdate(convertToBounds(this.value, this.min, this.max));
        }
        this.updateMessage();
    }

    public void setValueSilently(double value) {
        this.value = MathHelper.clamp(value, 0.0, 1.0);
        this.updateMessage();
    }

    public void updateMessage() {
        this.setMessage(twoDecimalPlaces(convertToBounds(this.value, this.min, this.max)).toString());
    }

    public void onValueUpdate(double value) {
    }

    /**
     * Translates the provided value from min-max bounds to a value inside 0-1 bounds
     *
     * @param value e.g. 5.6
     * @param min   e.g. 2.0
     * @param max   e.g. 8.5
     */
    public static double convertFromBounds(double value, double min, double max) {
        return MathHelper.map(value, min, max, 0.0f, 1.0f);
    }

    /**
     * Translates the provided value from 0-1 bounds to a value inside min-max bounds
     *
     * @param value e.g. 5.6
     * @param min   e.g. 2.0
     * @param max   e.g. 8.5
     */
    public static double convertToBounds(double value, double min, double max) {
        return MathHelper.map(value, 0.0f, 1.0f, min, max);
    }

    public static BigDecimal twoDecimalPlaces(double value) {
        return new BigDecimal(value).setScale(1, RoundingMode.HALF_UP);
    }
}
