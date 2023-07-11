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

public class IntegerSliderOption extends AbstractOption<Integer> implements AbstractOption.Interval<Integer> {

    private final Integer min;
    private final Integer max;

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
    public Collection<Integer> getValues() {
        return List.of(min, max);
    }

    @Override
    public Integer getWrittenValue(ConfigBase config) {
        String written = config.getWrittenValue(this.getName());
        return written != null ? Integer.valueOf(written) : getDefaultValue();
    }

    @Environment(value = EnvType.CLIENT)
    @Override
    public WidgetBase getConfigWidget(int width) {
        return new IntConfigSlider(this, width);
    }

    @Override
    public IntegerSliderOption setEnvType(EnvType type) {
        super.setEnvType(type);
        return this;
    }

    @Override
    public IntegerSliderOption setComment(String comment) {
        super.setComment(comment);
        return this;
    }

    @Override
    public IntegerSliderOption setDisplayName(Text displayName) {
        super.setDisplayName(displayName);
        return this;
    }

    @Override
    public IntegerSliderOption setTooltip(Text tooltip) {
        super.setTooltip(tooltip);
        return this;
    }

    @Override
    public void updateValueFromString(String newValue) {
        this.setValue(Integer.valueOf(newValue));
    }

    @Override
    public IntegerSliderOption hideDefault(Boolean bool) {
        super.hideDefault(bool);
        return this;
    }

    @Environment(value = EnvType.CLIENT)
    public static class IntConfigSlider extends SliderWidget {

        public final IntegerSliderOption option;

        public IntConfigSlider(IntegerSliderOption option, int width) {
            this(width / 2, 0, 100, 20, option);
        }

        public IntConfigSlider(int x, int y, int width, int height, IntegerSliderOption option) {
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
            Integer result = MathHelper.floor(convertToBounds(this.value, this.min, this.max));
            this.option.setValue(result);
        }

        public void updateMessage() {
            Integer result = MathHelper.floor(convertToBounds(this.value, this.min, this.max));
            this.setMessage(String.valueOf(result));
        }

        @Override
        public void renderBackground(DrawContext context, int mouseX, int mouseY) {
            if (this.isMouseOver(mouseX, mouseY, this.x - 150, this.y - 2, 300, 24)) {
                this.drawRectWithOutline(context, this.x - 150, this.y - 2, 300, 24, 0x2B_FFFFFF);
            }
        }
    }
}
