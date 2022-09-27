package evergoodteam.chassis.configs.options;

import evergoodteam.chassis.configs.ConfigBase;
import evergoodteam.chassis.configs.widgets.CyclingWidget;
import evergoodteam.chassis.configs.widgets.WidgetBase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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

    private Text display;
    private Text tooltip;
    private CyclingWidget<String> widget;

    public StringSetOption(String name, String defaultValue, Set<String> values) {
        this(name, defaultValue, values, Text.literal(name), Text.empty());
    }

    public StringSetOption(String name, String defaultValue, Set<String> values, Text displayName) {
        this(name, defaultValue, values, displayName, Text.empty());
    }


    public StringSetOption(String name, String defaultValue, Set<String> values, Text displayName, Text tooltip) {
        this.name = name;
        this.comment = "";
        this.value = defaultValue;
        this.bounds = values;
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

    @Environment(value = EnvType.CLIENT)
    @Override
    public WidgetBase getConfigWidget(int width) {
        return new StringConfigWidget(this, width);
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

    @Environment(value = EnvType.CLIENT)
    public static class StringConfigWidget extends CyclingWidget<String>{

        public StringConfigWidget(StringSetOption option, int width){
            super(width, List.copyOf(option.bounds), new CyclingWidget.UpdateOptionValue<>(option));
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
