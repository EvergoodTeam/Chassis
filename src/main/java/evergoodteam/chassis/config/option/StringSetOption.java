package evergoodteam.chassis.config.option;

import com.google.common.collect.ImmutableSet;
import evergoodteam.chassis.client.gui.widget.CyclingWidget;
import evergoodteam.chassis.client.gui.widget.WidgetBase;
import evergoodteam.chassis.client.gui.widget.interfaces.ConfigWidgetEntry;
import evergoodteam.chassis.config.ConfigBase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class StringSetOption extends AbstractOption<String> {

    private final Set<String> bounds;

    public StringSetOption(String name, String defaultValue, Set<String> values) {
        this(name, defaultValue, values, Text.literal(name), Text.empty());
    }

    public StringSetOption(String name, String defaultValue, Set<String> values, Text displayName) {
        this(name, defaultValue, values, displayName, Text.empty());
    }

    public StringSetOption(String name, String defaultValue, Set<String> values, Text displayName, Text tooltip) {
        super(name, defaultValue, displayName, tooltip);
        this.bounds = values;
    }

    @Override
    public Builder<String, ? extends StringSetOption> getBuilder() {
        return new Builder<>(this);
    }

    @Override
    public ImmutableSet<String> getValues() {
        return ImmutableSet.copyOf(bounds);
    }

    @Override
    public String getWrittenValue(ConfigBase config) {
        Optional<String> op = config.getWrittenValue(this);
        return op.orElseGet(this::getDefaultValue);
    }

    @Environment(value = EnvType.CLIENT)
    @Override
    public WidgetBase getConfigWidget(int width) {
        return new StringConfigWidget(this, width);
    }

    @Override
    public void setValueFromString(String newValue) {
        this.setValue(newValue);
    }

    @Environment(value = EnvType.CLIENT)
    public static class StringConfigWidget extends CyclingWidget<String> implements ConfigWidgetEntry {

        public final StringSetOption option;

        public StringConfigWidget(StringSetOption option, int width) {
            super(width, List.copyOf(option.bounds));
            this.initially(option.getValue())
                    .setOrderedTooltip(option.getTooltip(), this.getEntryWidth());
            this.setAddedHeight(2);
            this.option = option;
            option.addUpdateCallback(new OptionUpdateCallback<>() {
                @Override
                public void onUpdate(String newValue) {
                    initially(newValue);
                }
            });
        }

        @Override
        public void onValueUpdate(String value) {
            option.setValue(value);
        }

        @Override
        public void renderCenteredText(DrawContext context) {
            super.renderCenteredText(context);
            context.drawTextWithShadow(textRenderer, option.getDisplayName(), this.x - 142, y + (this.height - 8) / 2, 16777215);
        }
    }
}
