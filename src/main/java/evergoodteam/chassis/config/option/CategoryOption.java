package evergoodteam.chassis.config.option;

import evergoodteam.chassis.client.gui.widget.TextWidget;
import evergoodteam.chassis.client.gui.widget.WidgetBase;
import evergoodteam.chassis.config.ConfigBase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CategoryOption extends AbstractOption<String> {

    private final ConfigBase config;
    private final List<AbstractOption<?>> options = new ArrayList<>();

    public CategoryOption(ConfigBase config, String name, String comment) {
        super(name, comment, Text.literal(name), Text.literal(comment));
        this.config = config;
        this.setEnvType(EnvType.CLIENT);
    }

    /**
     * Adds a boolean property to a {@link CategoryOption} <p>
     */
    public CategoryOption addBooleanProperty(BooleanOption booleanOption) {
        config.getOptionStorage().storeBooleanOption(booleanOption);
        options.add(booleanOption);
        return this;
    }

    public CategoryOption addDoubleProperty(DoubleSliderOption doubleOption) {
        config.getOptionStorage().storeDoubleOption(doubleOption);
        options.add(doubleOption);
        return this;
    }

    public CategoryOption addIntegerSliderProperty(IntegerSliderOption option) {
        config.getOptionStorage().storeIntegerOption(option);
        options.add(option);
        return this;
    }

    public CategoryOption addStringProperty(StringSetOption option) {
        config.getOptionStorage().storeStringSetOption(option);
        options.add(option);
        return this;
    }

    public List<AbstractOption<?>> getOptions() {
        return options;
    }

    @Override
    public Collection<String> getValues() {
        return null;
    }

    @Override
    public String getWrittenValue(ConfigBase config) {
        return null;
    }

    @Environment(value = EnvType.CLIENT)
    @Override
    public WidgetBase getConfigWidget(int width) {
        return new CategoryWidget(width / 2 - 150, 0, 300, 44, this.getDisplayName(), 0x2B_FFFFFF, 0x5E_FFFFFF)
                .setDescription(this.getTooltip());
    }

    @Override
    public CategoryOption setEnvType(EnvType type) {
        super.setEnvType(type);
        return this;
    }

    @Override
    public void setValue(String newValue) {
    }

    @Override
    public CategoryOption setComment(String comment) {
        super.setComment(comment);
        return this;
    }

    @Override
    public CategoryOption setDisplayName(Text displayName) {
        super.setDisplayName(displayName);
        return this;
    }

    @Override
    public CategoryOption setTooltip(Text tooltip) {
        super.setTooltip(tooltip);
        return this;
    }

    @Override
    public void updateValueFromString(String newValue) {
    }

    /**
     * Does nothing since there isn't a value
     */
    @Override
    public CategoryOption hideDefault(Boolean bool) {
        return this;
    }

    @Environment(value = EnvType.CLIENT)
    public static class CategoryWidget extends TextWidget {

        public int color = 0x5E_FFFFFF;
        public int outline = 0xFF_FFFFFF;

        public CategoryWidget(int x, int y, int width, int height, Text message, int color, int outline) {
            this(x, y, width, height, message);
            this.color = color;
            this.outline = outline;
        }

        CategoryWidget(int x, int y, int width, int height, Text message) {
            super(x, y, width, height, message);
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            if (this.enabled) {
                this.drawRectWithOutline(context, this.x, this.y, this.width, this.height, this.color, this.outline);
                this.renderText(context);
            }
        }

        @Override
        public void playDownSound(SoundManager soundManager) {
        }
    }
}
