package evergoodteam.chassis.configs.options;

import evergoodteam.chassis.configs.ConfigBase;
import evergoodteam.chassis.configs.widgets.WidgetBase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.List;

public interface OptionBase<T> {

    String getName();

    String getComment();

    Text getDisplayName();

    Text getTooltip();

    T getValue();

    Collection<T> getValues();

    T getWrittenValue(ConfigBase config);

    T getDefaultValue();

    Boolean defaultHidden();

    @Environment(value = EnvType.CLIENT)
    WidgetBase getConfigWidget(int width);

    void setValue(T newValue);

    <E extends OptionBase<T>> E setComment(String comment);

    <E extends OptionBase<T>> E setTooltip(Text tooltip);

    default void reset() {
        this.setValue(this.getDefaultValue());
    }

    void updateValueFromString(String newValue);

    <E extends OptionBase<T>> E hideDefault(Boolean bool);

    default void addTo(List<? extends OptionBase<T>> list) {
        list.add(this.asOption());
    }

    private <E extends OptionBase<T>> E asOption() {
        return (E) this;
    }

    interface Interval<T> {

        T getMin();

        T getMax();
    }
}
