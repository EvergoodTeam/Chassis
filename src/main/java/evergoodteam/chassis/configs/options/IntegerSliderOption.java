package evergoodteam.chassis.configs.options;

import evergoodteam.chassis.configs.ConfigBase;
import evergoodteam.chassis.configs.widgets.SliderWidget;
import evergoodteam.chassis.configs.widgets.WidgetBase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.Collection;

public class IntegerSliderOption implements OptionBase<Integer>, OptionBase.Interval<Integer> {

    private final String name;
    private String comment;
    private Integer value;
    private final Integer min;
    private final Integer max;
    private final Integer defaultValue;
    private Boolean defaultHidden;

    private Text display;
    private Text tooltip;
    //private IntSlider widget;

    public IntegerSliderOption(String name, int min, int max, int defaultValue) {
        this(name, min, max, defaultValue, Text.literal(name), Text.empty());
    }

    public IntegerSliderOption(String name, int min, int max, int defaultValue, Text displayName) {
        this(name, min, max, defaultValue, displayName, Text.empty());
    }

    public IntegerSliderOption(String name, int min, int max, int defaultValue, Text displayName, Text tooltip) {
        this.name = name;
        this.comment = "";
        this.value = defaultValue;
        this.min = min;
        this.max = max;
        this.defaultValue = defaultValue;
        this.defaultHidden = false;
        this.display = displayName;
        this.tooltip = tooltip;
    }

    @Override
    public Integer getMin() {
        return this.min;
    }

    @Override
    public Integer getMax() {
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
    public Text getDisplayName() {
        return display;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public Collection<Integer> getValues() {
        return null;
    }

    @Override
    public Integer getWrittenValue(ConfigBase config) {
        return Integer.valueOf(config.getWrittenValue(name));
    }

    @Override
    public Integer getDefaultValue() {
        return defaultValue;
    }

    @Override
    public Boolean defaultHidden() {
        return defaultHidden;
    }

    @Environment(value = EnvType.CLIENT)
    @Override
    public WidgetBase getConfigWidget(int width) {
        return new IntConfigSlider(this, width);
    }

    @Override
    public void setValue(Integer newValue) {
        this.value = newValue;
    }

    @Override
    public Text getTooltip() {
        return tooltip;
    }

    @Override
    public IntegerSliderOption setComment(String comment) {
        this.comment = comment;
        return this;
    }

    @Override
    public IntegerSliderOption setTooltip(Text tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    @Override
    public void updateValueFromString(String newValue) {
        this.setValue(Integer.valueOf(newValue));
    }

    @Override
    public IntegerSliderOption hideDefault(Boolean bool) {
        this.defaultHidden = bool;
        return this;
    }

    @Environment(value = EnvType.CLIENT)
    public static class IntConfigSlider extends SliderWidget {

        public final IntegerSliderOption option;

        public IntConfigSlider(IntegerSliderOption option, int width) {
            this(width / 2, 0, 100, 20, option);
        }

        public IntConfigSlider(int x, int y, int width, int height, IntegerSliderOption option) {
            super(x, y, width, height, option);
            this.option = option;
            this.setTooltip(option.getTooltip());
        }

        @Override
        public void onValueUpdate() {
            Integer result = MathHelper.floor(convertToBounds(this.value, this.min, this.max));
            this.option.setValue(result);
        }

        public void updateMessage() {
            Integer result = MathHelper.floor(convertToBounds(this.value, this.min, this.max));
            this.setMessage(String.valueOf(result));
        }

        @Override
        public void renderBackground(MatrixStack matrices, int mouseX, int mouseY) {
            if (this.isMouseOver(mouseX, mouseY, this.x - 150, this.y - 2, 300, 24)) {
                this.drawRectWithOutline(matrices, this.x - 150, this.y - 2, 300, 24, 0x2B_FFFFFF);
            }
        }
    }
}
