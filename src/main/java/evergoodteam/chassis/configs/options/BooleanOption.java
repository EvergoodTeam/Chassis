package evergoodteam.chassis.configs.options;

import com.google.common.collect.ImmutableList;
import evergoodteam.chassis.configs.ConfigBase;
import evergoodteam.chassis.configs.screen.ChassisScreenTexts;
import evergoodteam.chassis.configs.widgets.CyclingWidget;
import evergoodteam.chassis.configs.widgets.WidgetBase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.List;

public class BooleanOption implements OptionBase<Boolean> {

    private final String name;
    private String comment;
    private Boolean value;
    private final Boolean defaultValue;
    private Boolean defaultHidden;

    private Text display;
    private Text tooltip;
    //private CyclingWidget<Boolean> widget;

    public BooleanOption(String name, Boolean defaultValue) {
        this(name, defaultValue, Text.literal(name), Text.empty());
    }

    public BooleanOption(String name, Boolean defaultValue, Text displayName) {
        this(name, defaultValue, displayName, Text.empty());
    }

    public BooleanOption(String name, Boolean defaultValue, Text displayName, Text tooltip) {
        this.name = name;
        this.comment = "";
        this.value = defaultValue;
        this.defaultValue = defaultValue;
        this.defaultHidden = false;
        this.display = displayName;
        this.tooltip = tooltip;
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
    public Text getTooltip() {
        return tooltip;
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public Collection<Boolean> getValues() {
        return ImmutableList.of(Boolean.TRUE, Boolean.FALSE);
    }

    @Override
    public Boolean getWrittenValue(ConfigBase config) {
        return Boolean.valueOf(config.getWrittenValue(name));
    }

    @Override
    public Boolean getDefaultValue() {
        return defaultValue;
    }

    @Override
    public Boolean defaultHidden() {
        return defaultHidden;
    }

    @Environment(value = EnvType.CLIENT)
    @Override
    public WidgetBase getConfigWidget(int width) {
        return new BooleanConfigWidget(this, width);
    }

    @Override
    public void setValue(Boolean newValue) {
        this.value = newValue;
    }

    @Override
    public BooleanOption setComment(String comment) {
        this.comment = comment;
        return this;
    }

    @Override
    public BooleanOption setTooltip(Text tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    @Override
    public void updateValueFromString(String newValue) {
        this.setValue(Boolean.valueOf(newValue));
    }

    @Override
    public BooleanOption hideDefault(Boolean bool) {
        this.defaultHidden = bool;
        return this;
    }

    @Environment(value = EnvType.CLIENT)
    public static class BooleanConfigWidget extends CyclingWidget<Boolean> {

        public BooleanConfigWidget(BooleanOption option, int width) {
            super(width, List.of(Boolean.TRUE, Boolean.FALSE), new CyclingWidget.UpdateOptionValue<>(option), value -> value ? ChassisScreenTexts.ON : ChassisScreenTexts.OFF);
            this.initially(option.getValue()).setTooltip(option.getTooltip());
        }

        @Override
        public void renderBackground(MatrixStack matrices, int mouseX, int mouseY) {
            if (this.isMouseOver(mouseX, mouseY, this.x - 150, this.y - 2, 300, 24)) {
                this.drawRectWithOutline(matrices, this.x - 150, this.y - 2, 300, 24, 0x2B_FFFFFF);
            }
        }
    }
}
