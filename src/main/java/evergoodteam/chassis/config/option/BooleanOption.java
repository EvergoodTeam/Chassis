package evergoodteam.chassis.config.option;

import com.google.common.collect.ImmutableList;
import evergoodteam.chassis.client.gui.text.ChassisScreenTexts;
import evergoodteam.chassis.client.gui.widget.CyclingWidget;
import evergoodteam.chassis.client.gui.widget.WidgetBase;
import evergoodteam.chassis.config.ConfigBase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.List;

public class BooleanOption extends AbstractOption<Boolean> {

    public BooleanOption(String name, Boolean defaultValue) {
        this(name, defaultValue, Text.literal(name), Text.empty());
    }

    public BooleanOption(String name, Boolean defaultValue, Text displayName) {
        this(name, defaultValue, displayName, Text.empty());
    }

    public BooleanOption(String name, Boolean defaultValue, Text displayName, Text tooltip) {
        super(name, defaultValue, displayName, tooltip);
    }

    @Override
    public Collection<Boolean> getValues() {
        return ImmutableList.of(Boolean.TRUE, Boolean.FALSE);
    }

    @Override
    public Boolean getWrittenValue(ConfigBase config) {
        String written = config.getWrittenValue(this.getName());
        return written != null ? Boolean.valueOf(written) : getDefaultValue();
    }

    @Environment(value = net.fabricmc.api.EnvType.CLIENT)
    @Override
    public WidgetBase getConfigWidget(int width) {
        return new BooleanConfigWidget(this, width);
    }

    @Override
    public BooleanOption setEnvType(EnvType type) {
        super.setEnvType(type);
        return this;
    }

    @Override
    public BooleanOption setComment(String comment) {
        super.setComment(comment);
        return this;
    }

    @Override
    public BooleanOption setDisplayName(Text displayName) {
        super.setDisplayName(displayName);
        return this;
    }

    @Override
    public BooleanOption setTooltip(Text tooltip) {
        super.setTooltip(tooltip);
        return this;
    }

    @Override
    public void updateValueFromString(String newValue) {
        this.setValue(Boolean.valueOf(newValue));
    }

    @Override
    public BooleanOption hideDefault(Boolean bool) {
        super.hideDefault(bool);
        return this;
    }

    @Environment(value = net.fabricmc.api.EnvType.CLIENT)
    public static class BooleanConfigWidget extends CyclingWidget<Boolean> {

        public final BooleanOption option;

        public BooleanConfigWidget(BooleanOption option, int width) {
            super(width, List.of(Boolean.TRUE, Boolean.FALSE), new CyclingWidget.UpdateOptionValue<>(option), value -> value ? ChassisScreenTexts.ON : ChassisScreenTexts.OFF);
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
