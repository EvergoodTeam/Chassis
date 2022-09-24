package evergoodteam.chassis.configs.options;

import evergoodteam.chassis.configs.ConfigBase;
import evergoodteam.chassis.configs.widgets.SliderWidget;
import evergoodteam.chassis.configs.widgets.WidgetBase;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.Collection;

public class DoubleSliderOption implements OptionBase<Double>, OptionBase.Interval<Double> {

    private final String name;
    private String comment;
    private Double value;
    private final Double min;
    private final Double max;
    private final Double defaultValue;
    private Boolean defaultHidden;

    private Text tooltip;
    private DoubleSlider widget;

    public DoubleSliderOption(String name, double min, double max, int defaultValue) {
        this(name, min, max, defaultValue, Text.empty());
    }

    public DoubleSliderOption(String name, double min, double max, double defaultValue, Text tooltip) {
        this.name = name;
        this.comment = "";
        this.value = defaultValue;
        this.min = min;
        this.max = max;
        this.defaultValue = defaultValue;
        this.defaultHidden = false;
        this.tooltip = tooltip;
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
    public String getName() {
        return name;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public Collection<Double> getValues() {
        return null;
    }

    @Override
    public Double getWrittenValue(ConfigBase config) {
        return Double.valueOf(config.getWrittenValue(name));
    }

    @Override
    public Double getDefaultValue() {
        return defaultValue;
    }


    @Override
    public Boolean defaultHidden() {
        return defaultHidden;
    }

    @Override
    public Text getTooltip() {
        return tooltip;
    }

    @Override
    public WidgetBase getConfigWidget(int width) {
        this.widget = new DoubleSlider(this, width);
        return this.widget;
    }

    @Override
    public void setValue(Double newValue) {
        this.value = newValue;
    }

    @Override
    public DoubleSliderOption setComment(String comment) {
        this.comment = comment;
        return this;
    }

    @Override
    public DoubleSliderOption setTooltip(Text tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    @Override
    public void updateValueFromString(String newValue) {
        this.setValue(Double.valueOf(newValue));
    }

    @Override
    public DoubleSliderOption hideDefault(Boolean bool) {
        this.defaultHidden = bool;
        return this;
    }

    public static class DoubleSlider extends SliderWidget {

        public final DoubleSliderOption option;

        public DoubleSlider(DoubleSliderOption option, int width) {
            this(width / 2, 0, 100, 20, option);
        }

        public DoubleSlider(int x, int y, int width, int height, DoubleSliderOption option) {
            super(x, y, width, height, option);
            this.option = option;
            this.setTooltip(option.getTooltip());
        }

        @Override
        public void onValueUpdate() {
            Double result = Double.valueOf(twoDecimalPlaces(MathHelper.lerpFromProgress(this.value, 0.0, 1.0, option.min, option.max)));
            this.option.setValue(result);
        }

        @Override
        public void renderBackground(MatrixStack matrices, int mouseX, int mouseY) {
            if (this.isMouseOver(mouseX, mouseY, this.x - 150, this.y - 2, 300, 24)) {
                this.drawRectWithOutline(matrices, this.x - 150, this.y - 2, 300, 24, 0x2B_FFFFFF);
            }
        }
    }
}
