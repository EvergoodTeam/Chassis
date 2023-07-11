package evergoodteam.chassis.configs.options;

import evergoodteam.chassis.client.gui.widgets.CyclingWidget;
import evergoodteam.chassis.client.gui.widgets.WidgetBase;
import evergoodteam.chassis.configs.ConfigBase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.List;
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
    public Set<String> getValues() {
        return bounds;
    }

    @Override
    public String getWrittenValue(ConfigBase config) {
        String written = config.getWrittenValue(this.getName());
        return written != null ? written : getDefaultValue();
    }

    @Environment(value = EnvType.CLIENT)
    @Override
    public WidgetBase getConfigWidget(int width) {
        return new StringConfigWidget(this, width);
    }

    @Override
    public StringSetOption setEnvType(EnvType type) {
        super.setEnvType(type);
        return this;
    }

    @Override
    public StringSetOption setComment(String comment) {
        super.setComment(comment);
        return this;
    }

    @Override
    public StringSetOption setDisplayName(Text displayName) {
        super.setDisplayName(displayName);
        return this;
    }

    @Override
    public StringSetOption setTooltip(Text tooltip) {
        super.setTooltip(tooltip);
        return this;
    }

    @Override
    public void updateValueFromString(String newValue) {
        this.setValue(newValue);
    }

    @Override
    public StringSetOption hideDefault(Boolean bool) {
        super.hideDefault(bool);
        return this;
    }

    @Environment(value = EnvType.CLIENT)
    public static class StringConfigWidget extends CyclingWidget<String> {

        public final StringSetOption option;

        public StringConfigWidget(StringSetOption option, int width) {
            super(width, List.copyOf(option.bounds), new CyclingWidget.UpdateOptionValue<>(option));
            this.initially(option.getValue()).setTooltip(option.getTooltip());
            this.option = option;
        }

        @Override
        public void renderCenteredText(DrawContext context) {
            super.renderCenteredText(context);
            context.drawTextWithShadow(textRenderer, this.option.getDisplayName(), this.x - 142, y + (this.height - 8) / 2, 16777215);
        }

        @Override
        public void renderBackground(DrawContext context, int mouseX, int mouseY) {
            if (this.isMouseOver(mouseX, mouseY, this.x - 150, this.y - 2, 300, 24)) {
                this.drawRectWithOutline(context, this.x - 150, this.y - 2, 300, 24, 0x2B_FFFFFF);
            }
        }
    }
}
