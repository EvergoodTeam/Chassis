package evergoodteam.chassis.config.option;

import evergoodteam.chassis.client.gui.widget.WidgetBase;
import evergoodteam.chassis.config.ConfigBase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;

import java.util.Collection;

public abstract class AbstractOption<T> {

    private EnvType envType = EnvType.SERVER;
    private final String name;
    private String comment;
    private Boolean defaultHidden;
    private Text display;
    private Text tooltip;
    private T value;
    private final T defaultValue;

    /**
     * EnvType.SERVER by default!
     */
    public AbstractOption(String name, T defaultValue, Text displayName, Text tooltip) {
        this.name = name;
        this.comment = "";
        this.value = defaultValue;
        this.defaultValue = defaultValue;
        this.defaultHidden = false;
        this.display = displayName;
        this.tooltip = tooltip;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public Text getDisplayName() {
        return display;
    }

    public Text getTooltip() {
        return tooltip;
    }

    public EnvType getType() {
        return this.envType;
    }

    public T getValue() {
        return value;
    }

    public abstract Collection<T> getValues();

    public abstract T getWrittenValue(ConfigBase config);

    public T getDefaultValue() {
        return defaultValue;
    }

    public Boolean isDefaultValue() {
        return getValue().equals(getDefaultValue());
    }

    public Boolean defaultHidden() {
        return defaultHidden;
    }

    @Environment(value = net.fabricmc.api.EnvType.CLIENT)
    public abstract WidgetBase getConfigWidget(int width);

    public AbstractOption<T> setEnvType(EnvType type) {
        this.envType = type;
        return this;
    }

    public void setValue(T newValue) {
        this.value = newValue;
    }

    public AbstractOption<T> setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public AbstractOption<T> setDisplayName(Text displayName) {
        this.display = displayName;
        return this;
    }

    public AbstractOption<T> setTooltip(Text tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    public void reset() {
        this.setValue(this.getDefaultValue());
    }

    /**
     * Updates the value from a .properties config file if the property is found; if not
     * found, sets it to the default value
     */
    public void updateValueFromWritten(ConfigBase config) {
        this.setValue(this.getWrittenValue(config));
    }

    public abstract void updateValueFromString(String newValue);

    public AbstractOption<T> hideDefault(Boolean bool) {
        this.defaultHidden = bool;
        return this;
    }

    public interface Interval<T> {

        T getMin();

        T getMax();

        void setMin(T min);

        void setMax(T max);
    }
}
