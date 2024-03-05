package evergoodteam.chassis.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class SquarePickerWidget extends SliderWidget {

    public double valueX = 0, valueY = 0;

    public SquarePickerWidget(int x, int y, int width, int height, Text message, double value, double min, double max) {
        super(x, y, width, height, message, value, min, max);
        this.sliderWidth = 5;
        this.sliderHeight = 5;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (!isEnabled()) return;
        super.render(context, mouseX, mouseY, delta);
        renderDot(context, mouseX, mouseY);
    }

    @Override
    public void renderSlider(DrawContext context, int mouseX, int mouseY) {
        context.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        context.drawGuiTexture(TEXTURE, this.getX(), this.getY(), this.getWidth(), this.getHeight());
        context.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    // TODO: renders above menu cutoff but not buttons, behaves like TextRenderer
    public void renderDot(DrawContext context, int mouseX, int mouseY) {
        drawRoundedRect(context, x + (int) (this.valueX * (this.width - 1)) - 2, y + (int) (this.valueY * (this.height - 1)) - 2, 5, 5, -1);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.setValuesFromMouse(mouseX, mouseY);
    }

    @Override
    public void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
        this.setValuesFromMouse(mouseX, mouseY);
        //super.onDrag(mouseX, mouseY, deltaX, deltaY);
    }

    public void setValuesFromMouse(double mouseX, double mouseY) {
        setValues(((mouseX - x) / width), ((mouseY - y) / height));
    }

    public void setValues(double valueX, double valueY) {
        int toUpdate = 0;

        double oldX = this.valueX;
        this.valueX = MathHelper.clamp(valueX, 0.0, 1.0);
        if (oldX != this.valueX) toUpdate++;

        double oldY = this.valueY;
        this.valueY = MathHelper.clamp(valueY, 0.0, 1.0);
        if (oldY != this.valueY) toUpdate++;

        if (toUpdate != 0) this.onValueUpdate(this.valueX, this.valueY);
        this.updateMessage();
    }

    public void onValueUpdate(double newValueX, double newValueY){
    }

    public void setValuesSilently(double valueX, double valueY) {
        this.valueX = MathHelper.clamp(valueX, 0.0, 1.0);
        this.valueY = MathHelper.clamp(valueY, 0.0, 1.0);
        this.updateMessage();
    }

    @Override
    public void updateMessage() {
        this.setMessage("%s%% %s%%".formatted(twoDecimalPlaces(this.valueX * 100), twoDecimalPlaces(this.valueY * 100)));
    }
}
