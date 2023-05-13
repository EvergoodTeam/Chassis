package evergoodteam.chassis.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SliderWidget extends WidgetBase {

    public Double value;
    public Double min;
    public Double max;

    public SliderWidget(int x, int y, int width, int height, Text message, double value, double min, double max) {
        super(x, y, width, height, message);
        this.value = value;
        this.min = min;
        this.max = max;
    }

    @Override
    public void renderSlider(MatrixStack matrices, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, ClickableWidget.WIDGETS_TEXTURE);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        int i = (this.isMouseOver(mouseX, mouseY) ? 2 : 1) * 20;
        this.drawTexture(matrices, this.x + (int) (this.value * (double) (this.width - 8)), this.y, 0, 46 + i, 4, 20);
        this.drawTexture(matrices, this.x + (int) (this.value * (double) (this.width - 8)) + 4, this.y, 196, 46 + i, 4, 20);
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
        this.setValue((mouseX - (double) (this.x + 4)) / (double) (this.width - 8));
    }

    public void setValue(double value) {
        double old = this.value;
        this.value = MathHelper.clamp(value, 0.0, 1.0);
        if (old != this.value) {
            this.onValueUpdate();
        }
        this.updateMessage();
    }

    public void updateMessage() {
        this.setMessage(twoDecimalPlaces(convertToBounds(this.value, this.min, this.max)));
    }

    public void onValueUpdate() {
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

    public static String twoDecimalPlaces(double value) {
        return new BigDecimal(value).setScale(1, RoundingMode.HALF_UP).toString();
    }
}
