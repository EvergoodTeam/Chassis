package evergoodteam.chassis.config.option;

import com.google.common.collect.ImmutableList;
import evergoodteam.chassis.client.gui.text.ChassisScreenTexts;
import evergoodteam.chassis.client.gui.widget.CyclingWidget;
import evergoodteam.chassis.client.gui.widget.WidgetBase;
import evergoodteam.chassis.client.gui.widget.interfaces.ConfigWidgetEntry;
import evergoodteam.chassis.config.ConfigBase;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Optional;

public class BooleanOption extends AbstractOption<Boolean> {

    public BooleanOption(String name, Boolean defaultValue) {
        this(name, defaultValue, Text.literal(name), Text.empty());
    }

    public BooleanOption(String name, Boolean defaultValue, Text displayName) {
        this(name, defaultValue, displayName, Text.empty());
    }

    @Override
    public Builder<Boolean, BooleanOption> getBuilder() {
        return new Builder<>(this);
    }

    public BooleanOption(String name, Boolean defaultValue, Text displayName, Text tooltip) {
        super(name, defaultValue, displayName, tooltip);
    }

    @Override
    public ImmutableList<Boolean> getValues() {
        return ImmutableList.of(Boolean.TRUE, Boolean.FALSE);
    }

    @Override
    public Boolean getWrittenValue(ConfigBase config) {
        Optional<String> op = config.getWrittenValue(this);
        return op.map(Boolean::valueOf).orElseGet(this::getDefaultValue);
    }

    @Environment(value = net.fabricmc.api.EnvType.CLIENT)
    @Override
    public WidgetBase getConfigWidget(int width) {
        return new BooleanConfigWidget(this, width);
    }

    @Override
    public void setValueFromString(String newValue) {
        this.setValue(Boolean.valueOf(newValue));
    }

    @Environment(value = net.fabricmc.api.EnvType.CLIENT)
    public static class BooleanConfigWidget extends CyclingWidget<Boolean> implements ConfigWidgetEntry {

        public final BooleanOption option;

        public BooleanConfigWidget(BooleanOption option, int width) {
            super(width, List.of(Boolean.TRUE, Boolean.FALSE), value -> value ? ChassisScreenTexts.ON : ChassisScreenTexts.OFF);
            this.initially(option.getValue())
                    .setOrderedTooltip(option.getTooltip(), this.getEntryWidth());
            this.setAddedHeight(2);
            this.option = option;
            option.addUpdateCallback(new OptionUpdateCallback<>() {
                @Override
                public void onUpdate(Boolean newValue) {
                    initially(newValue);
                }
            });
        }

        @Override
        public void onValueUpdate(Boolean value) {
            option.setValue(value);
        }

        @Override
        public void renderCenteredText(DrawContext context) {
            super.renderCenteredText(context);
            context.drawTextWithShadow(textRenderer, this.option.getDisplayName(), this.x - 142, y + (this.height - 8) / 2, 16777215);
        }
    }
}
