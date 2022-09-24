package evergoodteam.chassis.configs.widgets;

import evergoodteam.chassis.configs.options.OptionBase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.List;
import java.util.function.Function;

public class CyclingWidget<T> extends WidgetBase {

    public int index;
    public List<T> values;
    public final UpdateCallback<T> updateCallback;
    public Function<T, Text> valueToText;

    public CyclingWidget(int width, List<T> values, UpdateCallback<T> updateCallback, Function<T, Text> valueToText, Text tooltip) {
        this(width, values, updateCallback);
        this.valueToText = valueToText;
    }

    public CyclingWidget(int width, List<T> values, UpdateCallback<T> updateCallback, Function<T, Text> valueToText) {
        this(width, values, updateCallback);
        this.valueToText = valueToText;
    }

    public CyclingWidget(int width, List<T> values, UpdateCallback<T> updateCallback) {
        this(width / 2, 0, 100, 20, Text.empty(), values, updateCallback);
    }

    public CyclingWidget(int x, int y, int width, int height, Text message, List<T> values, UpdateCallback<T> updateCallback) {
        super(x, y, width, height, message);
        this.index = 0;
        this.values = values;
        this.updateCallback = updateCallback;
    }

    @Override
    public void onPress() {
        if (Screen.hasShiftDown()) {
            this.cycle(-1);
        } else {
            this.cycle(1);
        }
    }

    public void cycle(int amount) {
        this.index = MathHelper.floorMod(this.index + amount, this.values.size());
        T nextValue = getValue(this.index);

        this.updateMessage(nextValue);
        this.updateCallback.onValueChange(this, nextValue);
    }

    public CyclingWidget<T> initially(T value) {
        int i = this.values.indexOf(value);
        if (i != -1) {
            this.index = i;
            this.updateMessage(getValue(i));
        }
        return this;
    }

    public void updateMessage(T value) {
        if (this.valueToText != null) this.setMessage(this.valueToText.apply(value));
        else this.setMessage(Text.literal(String.valueOf(value)));
    }

    public T getValue(int index) {
        return this.values.get(index);
    }

    public CyclingWidget<T> setValueToText(Function<T, Text> valueToText) {
        this.valueToText = valueToText;
        return this;
    }

    public record UpdateOptionValue<T>(OptionBase<T> option) implements CyclingWidget.UpdateCallback<T> {
        @Override
        public void onValueChange(CyclingWidget<T> button, T value) {
            option.setValue(value);
        }
    }

    @Environment(value = EnvType.CLIENT)
    public static interface UpdateCallback<T> {
        void onValueChange(CyclingWidget<T> var1, T var2);
    }
}
