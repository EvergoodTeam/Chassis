package evergoodteam.chassis.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class VerticalSliderWidget extends SliderWidget {

    public VerticalSliderWidget(int x, int y, int width, int height, Text message, double value, double min, double max) {
        super(x, y, width, height, message, value, min, max);
        this.sliderWidth = 20;
        this.sliderHeight = 8;
    }

    @Override
    public void renderSlider(DrawContext context, int mouseX, int mouseY) {
        context.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        context.drawNineSlicedTexture(TEXTURE, this.getX(), this.getY(), this.getWidth(), this.getHeight(), 20, 4, 200, 20, 0, this.getYImage(isFocused()));

        this.renderBack(context, mouseX, mouseY);

        context.drawNineSlicedTexture(TEXTURE, this.getX(), this.getY() + (int) (this.value * (double) (this.height - sliderHeight)), sliderWidth, sliderHeight, 20, 4, 200, 20, 0, getTextureV());
        context.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.setValueFromMouseY(mouseY);
    }

    @Override
    public void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
        this.setValueFromMouseY(mouseY);
        //super.onDrag(mouseX, mouseY, deltaX, deltaY);
    }
}
