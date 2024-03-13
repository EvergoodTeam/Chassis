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
import net.minecraft.util.math.MathHelper;

import java.util.Optional;

public class IntegerSliderOption extends AbstractOption<Integer> implements AbstractOption.Interval<Integer> {

    private Integer min;
    private Integer max;

    public IntegerSliderOption(String name, int min, int max, int defaultValue) {
        this(name, min, max, defaultValue, Text.literal(name), Text.empty());
    }

    public IntegerSliderOption(String name, int min, int max, int defaultValue, Text displayName) {
        this(name, min, max, defaultValue, displayName, Text.empty());
    }

    public IntegerSliderOption(String name, int min, int max, int defaultValue, Text displayName, Text tooltip) {
        super(name, defaultValue, displayName, tooltip);
        this.min = min;
        this.max = max;
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
    public void setMin(Integer min) {
        this.min = min;
    }

    @Override
    public void setMax(Integer max) {
        this.max = max;
    }

    @Override
    public Builder<Integer, IntegerSliderOption> getBuilder() {
        return new Builder<>(this);
    }

    @Override
    public ImmutableList<Integer> getValues() {
        return ImmutableList.of(min, max);
    }

    @Override
    public Integer getWrittenValue(ConfigBase config) {
        Optional<String> op = config.getWrittenValue(this);
        return op.map(Integer::valueOf).orElseGet(this::getDefaultValue);
    }

    @Environment(value = EnvType.CLIENT)
    @Override
    public WidgetBase getConfigWidget(int width) {
        return new IntConfigSlider(this, width);
    }

    @Override
    public void setValueFromString(String newValue) {
        this.setValue(Integer.valueOf(newValue));
    }

    @Environment(value = EnvType.CLIENT)
    public static class IntConfigSlider extends SliderWidget implements ConfigWidgetEntry {

        public final IntegerSliderOption option;

        public IntConfigSlider(IntegerSliderOption option, int width) {
            this(width / 2, 0, 100, 20, option);
        }

        public IntConfigSlider(int x, int y, int width, int height, IntegerSliderOption option) {
            super(x, y, width, height, Text.literal(String.valueOf(option.getValue())),
                    convertFromBounds(option.getValue(), option.getMin(), option.getMax()),
                    option.getMin(), option.getMax());
            this.setAddedHeight(2);
            this.option = option;
            this.setOrderedTooltip(option.getTooltip(), this.getEntryWidth());
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
            option.setValue(MathHelper.floor(newValue));
        }

        public void updateMessage() {
            Integer result = MathHelper.floor(convertToBounds(this.value, this.min, this.max));
            this.setMessage(String.valueOf(result));
        }
    }
}
