package evergoodteam.chassis.configs.options;

import com.google.common.collect.ImmutableList;
import evergoodteam.chassis.configs.ConfigBase;
import evergoodteam.chassis.configs.screen.ChassisScreenTexts;
import evergoodteam.chassis.configs.widgets.CyclingWidget;
import evergoodteam.chassis.configs.widgets.WidgetBase;
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

    private Text tooltip;
    private CyclingWidget<Boolean> widget;

    public BooleanOption(String key, Boolean defaultValue) {
        this(key, defaultValue, Text.empty());
    }

    public BooleanOption(String key, Boolean defaultValue, Text tooltip) {
        this.name = key;
        this.comment = "";
        this.value = defaultValue;
        this.defaultValue = defaultValue;
        this.defaultHidden = false;
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

    @Override
    public WidgetBase getConfigWidget(int width) {
        this.widget = new CyclingWidget<>(width, List.of(Boolean.TRUE, Boolean.FALSE), new CyclingWidget.UpdateOptionValue<>(this), value -> value ? ChassisScreenTexts.ON : ChassisScreenTexts.OFF) {

            @Override
            public void renderBackground(MatrixStack matrices, int mouseX, int mouseY) {
                if (this.isMouseOver(mouseX, mouseY, this.x - 150, this.y - 2, 300, 24)) {
                    this.drawRectWithOutline(matrices, this.x - 150, this.y - 2, 300, 24, 0x2B_FFFFFF);
                }
            }
        };
        this.widget.initially(this.value).setTooltip(this.tooltip);
        return this.widget;
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
}
