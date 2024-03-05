package evergoodteam.chassis.config.option;

import com.google.common.collect.ImmutableList;
import evergoodteam.chassis.client.gui.widget.SliderWidget;
import evergoodteam.chassis.client.gui.widget.WidgetBase;
import evergoodteam.chassis.client.gui.widget.interfaces.ConfigWidgetEntry;
import evergoodteam.chassis.config.ConfigBase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.Optional;

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
    public Builder<Double, DoubleSliderOption> getBuilder() {
        return new Builder<>(this);
    }

    @Override
    public ImmutableList<Double> getValues() {
        return ImmutableList.of(min, max);
    }

    @Override
    public Double getWrittenValue(ConfigBase config) {
        Optional<String> op = config.getWrittenValue(this);
        return op.map(Double::valueOf).orElseGet(this::getDefaultValue);
    }

    @Environment(value = EnvType.CLIENT)
    @Override
    public WidgetBase getConfigWidget(int width) {
        return new DoubleConfigSlider(this, width);
    }

    @Override
    public void setValueFromString(String newValue) {
        this.setValue(Double.valueOf(newValue));
    }

    @Environment(value = EnvType.CLIENT)
    public static class DoubleConfigSlider extends SliderWidget implements ConfigWidgetEntry {

        public final DoubleSliderOption option;

        public DoubleConfigSlider(DoubleSliderOption option, int width) {
            this(width / 2, 0, 100, 20, option);
        }

        public DoubleConfigSlider(int x, int y, int width, int height, DoubleSliderOption option) {
            super(x, y, width, height, Text.literal(String.valueOf(option.getValue())),
                    convertFromBounds(option.getValue(), option.getMin(), option.getMax()),
                    option.getMin(), option.getMax());
            this.setAddedY(2);
            this.option = option;
            this.setOrderedTooltip(option.getTooltip());
        }

        @Override
        public void onReset() {
            setValueSilently(convertFromBounds(option.getValue(), this.min, this.max));
        }

        @Override
        public void renderCenteredText(DrawContext context) {
            super.renderCenteredText(context);
            context.drawTextWithShadow(textRenderer, this.option.getDisplayName(), this.x - 142, y + (this.height - 8) / 2, 16777215);
        }

        @Override
        public void onValueUpdate(double newValue) {
            option.setValue(twoDecimalPlaces(newValue).doubleValue());
        }
    }
}
