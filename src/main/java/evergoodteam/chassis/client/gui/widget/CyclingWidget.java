package evergoodteam.chassis.client.gui.widget;

import evergoodteam.chassis.config.option.AbstractOption;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.List;
import java.util.function.Function;

@Environment(value = EnvType.CLIENT)
public class CyclingWidget<T> extends WidgetBase {

    public int index;
    public List<T> values;
    public Function<T, Text> valueToText;

    public CyclingWidget(int width, List<T> values, Function<T, Text> valueToText) {
        this(width, values);
        this.valueToText = valueToText;
    }

    public CyclingWidget(int width, List<T> values) {
        this(width / 2, 0, 100, 20, values);
    }

    public CyclingWidget(int x, int y, int width, int height, List<T> values) {
        super(x, y, width, height, Text.empty());
        this.index = 0;
        this.values = values;
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
        this.onValueUpdate(nextValue);
    }

    public CyclingWidget<T> initially(T value) { // Can be considered an equivalent of setValueSilently
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

    public void onValueUpdate(T value) {
    }

    public T getValue(int index) {
        return this.values.get(index);
    }

    public CyclingWidget<T> setValueToText(Function<T, Text> valueToText) {
        this.valueToText = valueToText;
        return this;
    }
}
