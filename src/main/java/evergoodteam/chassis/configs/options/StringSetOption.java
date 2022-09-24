package evergoodteam.chassis.configs.options;

import evergoodteam.chassis.configs.ConfigBase;
import evergoodteam.chassis.configs.widgets.CyclingWidget;
import evergoodteam.chassis.configs.widgets.WidgetBase;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Set;

public class StringSetOption implements OptionBase<String> {

    private final String name;
    private String comment;
    private String value;
    private final Set<String> bounds;
    private final String defaultValue;
    private Boolean defaultHidden;

    private CyclingWidget<String> widget;
    private Text tooltip;

    public StringSetOption(String name, String defaultValue, Set<String> values) {
        this(name, defaultValue, values, Text.empty());
    }

    public StringSetOption(String name, String defaultValue, Set<String> values, Text tooltip) {
        this.name = name;
        this.comment = "";
        this.value = defaultValue;
        this.bounds = values;
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
    public Set<String> getValues() {
        return bounds;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getWrittenValue(ConfigBase config) {
        return config.getWrittenValue(name);
    }

    @Override
    public String getDefaultValue() {
        return defaultValue;
    }

    @Override
    public Boolean defaultHidden() {
        return defaultHidden;
    }

    @Override
    public WidgetBase getConfigWidget(int width) {
        this.widget = new CyclingWidget<>(width, List.copyOf(this.bounds), new CyclingWidget.UpdateOptionValue<>(this)) {

            @Override
            public void renderBackground(MatrixStack matrices, int mouseX, int mouseY) {
                if (this.isMouseOver(mouseX, mouseY, this.x - 150, this.y - 2, 300, 24)) {
                    this.drawRectWithOutline(matrices, this.x - 150, this.y - 2, 300, 24, 0x2B_FFFFFF);
                }
            }
        };

        this.widget.initially(this.getValue()).setTooltip(this.tooltip);
        return this.widget;
    }

    @Override
    public void setValue(String newValue) {
        this.value = newValue;
    }

    @Override
    public StringSetOption setComment(String comment) {
        this.comment = comment;
        return this;
    }

    @Override
    public StringSetOption setTooltip(Text tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    @Override
    public void updateValueFromString(String newValue) {
        this.setValue(newValue);
    }

    @Override
    public StringSetOption hideDefault(Boolean bool) {
        this.defaultHidden = bool;
        return this;
    }
}
