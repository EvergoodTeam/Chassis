package evergoodteam.chassis.config.option;

import evergoodteam.chassis.client.gui.text.ChassisScreenTexts;
import evergoodteam.chassis.client.gui.widget.ColorPickerWidget;
import evergoodteam.chassis.client.gui.widget.WidgetBase;
import evergoodteam.chassis.client.gui.widget.interfaces.ConfigWidgetEntry;
import evergoodteam.chassis.util.gui.ColorAHSB;
import evergoodteam.chassis.util.gui.ColorUtils;
import lombok.extern.log4j.Log4j2;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;

import java.util.Set;

@Log4j2
public class ColorOption extends StringSetOption {

    private boolean open = false;

    public ColorOption(OptionUpdateCallback<String> updateCallback, String name, String defaultColor){
        this(name, defaultColor);
        this.addUpdateCallback(updateCallback);
    }

    public ColorOption(String name, String defaultColor) {
        super(name, defaultColor, Set.of(defaultColor));
    }

    public int getIntColorValue(){
        return ColorUtils.ARGB.getIntFromHexARGB(this.getValue());
    }

    public void setDropdownState(boolean open){
        this.open = open;
    }

    @Override
    public Builder getBuilder() {
        return new Builder(this);
    }

    public static class Builder extends AbstractOption.Builder<String, ColorOption>{

        Builder(ColorOption option) {
            super(option);
        }

        public Builder setDropdownState(boolean open){
            option.setDropdownState(open);
            return this;
        }
    }

    @Environment(value = EnvType.CLIENT)
    @Override
    public WidgetBase getConfigWidget(int width) {
        return new ColorOptionWidget(this, width);
    }

    @Environment(value = EnvType.CLIENT)
    public static class ColorOptionWidget extends ColorPickerWidget implements ConfigWidgetEntry {

        private final ColorOption option;

        public ColorOptionWidget(ColorOption option, int width) {
            super(width / 2 - 150, 0, 300, 130, ColorUtils.ARGB.getIntFromHexARGB(option.getValue()), option.getDisplayName());
            this.option = option;
            this.textField.setChangedListener(option::setValue);

            this.setVisibility(option.open);

            initCallbacks();
            updateTextField(option.getValue());
        }

        public void initCallbacks() {

            this.option.addUpdateCallback(new OptionUpdateCallback<>() {

                @Override
                public void onUpdate(String newValue){
                    colorPicker.ashb.setValue(ColorUtils.ARGB.getIntFromHexARGB(newValue));
                    colorPicker.ashb.updateAll();
                    updateTextField(option.getValue());
                }
            });

            this.colorPicker.ashb.setCallback(new ColorAHSB.ColorUpdateCallback() {
                @Override
                public void onRecalculate(int value) {
                    option.setValueSilently(colorPicker.ashb.getHexValue());
                    updateTextField(colorPicker.ashb.getHexValue());
                }

                @Override
                public void onAlphaUpdate(float alpha) {
                    colorPicker.updateAlphaSlider(alpha);
                }

                @Override
                public void onHueUpdate(float hue) {
                    colorPicker.updateHueSlider(hue);
                }

                @Override
                public void onSBUpdate(float saturation, float brightness) {
                    colorPicker.updateSBDot(saturation, brightness);
                }
            });
        }

        public void updateTextField(String text){
            textField.setTextSilently(text);
            if(ColorUtils.Hex.isARGBHex(text)) textField.setOrderedTooltip(ChassisScreenTexts.ARGB);
            else if(ColorUtils.Hex.isRGBHex(text)) textField.setOrderedTooltip(ChassisScreenTexts.RGB);
            else textField.setOrderedTooltip(ChassisScreenTexts.INVALID);
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            super.render(context, mouseX, mouseY, delta);
            this.renderArrow(context, mouseX, mouseY);
        }

        @Override
        public boolean hasExtendedHeight() {
            return false;
        }

        @Override
        public void drawHoveredHighlight(WidgetBase widget, DrawContext context, int centerX, int y, double mouseX, double mouseY) {
            this.drawRectWithOutline(context, this.getX(), this.getY(), this.getWidth(), this.getHeight(), ColorUtils.TWHITE);
        }

        @Override
        public boolean isMouseOverTooltip(WidgetBase widget, double mouseX, double mouseY) {
            return this.isMouseOver(mouseX, mouseY);
        }
    }
}
