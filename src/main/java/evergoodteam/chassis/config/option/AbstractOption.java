package evergoodteam.chassis.config.option;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import evergoodteam.chassis.client.gui.widget.WidgetBase;
import evergoodteam.chassis.config.ConfigBase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractOption<T> {

    private EnvType envType = EnvType.CLIENT;
    private final String name;
    private final T defaultValue;
    private T value;
    private List<OptionUpdateCallback<T>> callbacks = new ArrayList<>();
    private String comment;
    private boolean commentHidden = false;
    private Boolean defaultCommentHidden = false;
    private Text displayName;
    private Text tooltip;

    /**
     * Returns a new instance of this option's builder. This builder can either be the default {@link Builder} or one specified by the
     * option itself, where custom methods can be featured.
     */
    public abstract Builder<T, ? extends AbstractOption<T>> getBuilder();

    /**
     * Creates a new {@link AbstractOption} object with the default value as its initial value.
     */
    // TODO: missing constructor for comment, check categoryOption
    public AbstractOption(String name, T defaultValue, Text displayName, Text tooltip) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.comment = "";
        this.displayName = displayName;
        this.tooltip = tooltip;
    }

    public EnvType getEnvType() {
        return this.envType;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

    public ImmutableCollection<T> getValues() {
        return ImmutableList.of(value);
    }

    /**
     * Uses the provided {@link ConfigBase} to retrieve the written value from a config file:
     * <li>if the option is found in the config file, the written value is converted to the option's type and returned.
     * <li>if the option is not found in the config file, the option's default value is returned.
     */
    public abstract T getWrittenValue(ConfigBase config);

    public T getDefaultValue() {
        return defaultValue;
    }

    public boolean isDefaultValue() {
        return getValue().equals(getDefaultValue());
    }

    public String getComment() {
        return comment;
    }

    public Text getDisplayName() {
        return displayName;
    }

    public Text getTooltip() {
        return tooltip;
    }

    public boolean isCommentHidden() {
        return commentHidden;
    }

    public boolean isDefaultCommentHidden() {
        return defaultCommentHidden;
    }

    @Environment(value = EnvType.CLIENT)
    public abstract WidgetBase getConfigWidget(int width);

    /**
     * Sets this option's environment type. {@link EnvType#CLIENT} is used as the default type for every new option instance.
     */
    public void setEnvType(EnvType type) {
        this.envType = type;
    }

    /**
     * Sets this option's value and runs through the stored {@link AbstractOption#callbacks}, calling both normal ({@link OptionUpdateCallback#onUpdate})
     * and generic ({@link OptionUpdateCallback#onAnyUpdate}) callback methods if the callback itself isn't null.
     */
    public void setValue(T newValue) {
        this.value = newValue;

        callbacks.stream().filter(Objects::nonNull).forEach(callback -> {
            callback.onUpdate(newValue);
            callback.onAnyUpdate(newValue);
        });
    }

    /**
     * Sets this option's value and runs through the stored {@link AbstractOption#callbacks}, calling only the generic
     * ({@link OptionUpdateCallback#onAnyUpdate}) callback method if the callback itself isn't null.
     */
    public void setValueSilently(T newValue) {
        this.value = newValue;
        callbacks.stream().filter(Objects::nonNull).forEach(callback -> callback.onAnyUpdate(newValue));
    }

    /**
     * Sets this option's comment, which is used as documentation inside the config file.
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Sets this option's display name, used for rendering inside the config screen.
     */
    public void setDisplayName(Text displayName) {
        this.displayName = displayName;
    }

    /**
     * Sets this option's tooltip, used for rendering inside the config screen.
     */
    public void setTooltip(Text tooltip) {
        this.tooltip = tooltip;
    }

    /**
     * Adds the specified updateCallback to the {@link AbstractOption#callbacks stored list}.
     */
    public void addUpdateCallback(OptionUpdateCallback<T> updateCallback) {
        callbacks.add(updateCallback);
    }

    /**
     * Sets this option's value to the stored default value.
     */
    public void reset() {
        this.setValue(this.getDefaultValue());
    }

    /**
     * Sets this option's value to the written one inside the config file if such option is found, otherwise the default value is used.
     */
    /*
    public void updateValueFromWritten(ConfigBase config) {
        this.setValue(this.getWrittenValue(config));
    }*/

    /**
     * Sets this option's value by using a string.
     */
    public abstract void setValueFromString(String newValue);

    public void hideComment(boolean bool) {
        this.commentHidden = bool;
    }

    /**
     * Sets {@link AbstractOption#defaultCommentHidden}, a boolean that specifies if the config file comments should have the default value (or bounds) written at their end: e.g. "(0 ~ 100)".
     * Useful for cases where default values don't need to be shared with the user or are absent.
     */
    public void hideDefaultComment(Boolean bool) {
        this.defaultCommentHidden = bool;
    }

    /**
     * Interface used to categorize options that rely on strict numerical bounds.
     */
    public interface Interval<T> {

        T getMin();

        T getMax();

        void setMin(T min);

        void setMax(T max);
    }

    public interface OptionUpdateCallback<T> {

        /**
         * Normal callback method that is called everytime the option is updated through {@link AbstractOption#setValue(Object)}.
         */
        default void onUpdate(T newValue) {
        }

        /**
         * Generic callback method that is called when the option is updated through either {@link AbstractOption#setValue(Object)} or {@link AbstractOption#setValueSilently(Object)}.
         */
        // TODO: fix need for silent calls
        default void onAnyUpdate(T newValue) {
        }
    }

    public static class Builder<T, E extends AbstractOption<T>> {

        E option;

        Builder(E option) {
            this.option = option;
        }

        public Builder<T, E> setEnvType(EnvType envType) {
            option.setEnvType(envType);
            return this;
        }

        public Builder<T, E> setComment(String comment) {
            option.setComment(comment);
            return this;
        }

        public Builder<T, E> setDisplayName(Text displayName) {
            option.setDisplayName(displayName);
            return this;
        }

        public Builder<T, E> setTooltip(Text tooltip) {
            option.setTooltip(tooltip);
            return this;
        }

        public Builder<T, E> addUpdateCallback(OptionUpdateCallback<T> updateCallback) {
            option.addUpdateCallback(updateCallback);
            return this;
        }

        public Builder<T, E> hideComment(Boolean bool) {
            option.hideComment(bool);
            return this;
        }

        public Builder<T, E> hideDefaultComment(Boolean bool) {
            option.hideDefaultComment(bool);
            return this;
        }

        public E build() {
            return option;
        }
    }
}