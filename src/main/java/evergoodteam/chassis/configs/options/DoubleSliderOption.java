package evergoodteam.chassis.configs.options;

import evergoodteam.chassis.client.gui.widgets.SliderWidget;
import evergoodteam.chassis.client.gui.widgets.WidgetBase;
import evergoodteam.chassis.configs.ConfigBase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.Collection;
import java.util.List;

public class DoubleSliderOption extends AbstractOption<Double> implements AbstractOption.Interval<Double> {

    private Double min;
    private Double max;

    public DoubleSliderOption(String name, double min, double max, double defaultValue) {
        this(name, min, max, defaultValue, Text.literal(name), Text.empty());
    }

    public DoubleSliderOption(String name, double min, double max, double defaultValue, Text displayName) {
        this(name, min, max, defaultValue, displayName, Text.empty());
    }

    public DoubleSliderOption(String name, double min, double max, double defaultValue, Text displayName, Text tooltip) {
        super(name, defaultValue, displayName, tooltip);
        this.min = min;
        this.max = max;
    }

    @Override
    public Double getMin() {
        return this.min;
    }

    @Override
    public Double getMax() {
        return this.max;
    }

    @Override
    public void setMin(Double min) {
        this.min = min;
    }

    @Override
    public void setMax(Double max) {
        this.max = max;
    }

    @Override
    public Collection<Double> getValues() {
        return List.of(min, max);
    }

    @Override
    public Double getWrittenValue(ConfigBase config) {
        String written = config.getWrittenValue(this.getName());
        return written != null ? Double.valueOf(written) : getDefaultValue();
    }

    @Environment(value = EnvType.CLIENT)
    @Override
    public WidgetBase getConfigWidget(int width) {
        return new DoubleConfigSlider(this, width);
    }

    @Override
    public DoubleSliderOption setEnvType(EnvType type) {
        super.setEnvType(type);
        return this;
    }

    @Override
    public DoubleSliderOption setComment(String comment) {
        super.setComment(comment);
        return this;
    }

    @Override
    public DoubleSliderOption setDisplayName(Text displayName) {
        super.setDisplayName(displayName);
        return this;
    }

    @Override
    public DoubleSliderOption setTooltip(Text tooltip) {
        super.setTooltip(tooltip);
        return this;
    }

    @Override
    public void updateValueFromString(String newValue) {
        this.setValue(Double.valueOf(newValue));
    }

    @Override
    public DoubleSliderOption hideDefault(Boolean bool) {
        super.hideDefault(bool);
        return this;
    }

    @Environment(value = EnvType.CLIENT)
    public static class DoubleConfigSlider extends SliderWidget {

        public final DoubleSliderOption option;

        public DoubleConfigSlider(DoubleSliderOption option, int width) {
            this(width / 2, 0, 100, 20, option);
        }

        public DoubleConfigSlider(int x, int y, int width, int height, DoubleSliderOption option) {
            super(x, y, width, height, Text.literal(String.valueOf(option.getValue())),
                    convertFromBounds(option.getValue(), option.getMin(), option.getMax()),
                    option.getMin(), option.getMax());
            this.option = option;
            this.setTooltip(option.getTooltip());
        }

        @Override
        public void renderCenteredText(DrawContext context) {
            super.renderCenteredText(context);
            context.drawTextWithShadow(textRenderer, this.option.getDisplayName(), this.x - 142, y + (this.height - 8) / 2, 16777215);
        }

        @Override
        public void onValueUpdate() {
            Double result = Double.valueOf(twoDecimalPlaces(MathHelper.map(this.value, 0.0, 1.0, option.min, option.max)));
            this.option.setValue(result);
        }

        @Override
        public void renderBackground(DrawContext context, int mouseX, int mouseY) {
            if (this.isMouseOver(mouseX, mouseY, this.x - 150, this.y - 2, 300, 24)) {
                this.drawRectWithOutline(context, this.x - 150, this.y - 2, 300, 24, 0x2B_FFFFFF);
            }
        }
    }
}
