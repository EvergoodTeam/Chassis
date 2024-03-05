package evergoodteam.chassis.config.option;

import evergoodteam.chassis.client.gui.text.GradientText;
import evergoodteam.chassis.client.gui.widget.TextWidget;
import evergoodteam.chassis.client.gui.widget.WidgetBase;
import evergoodteam.chassis.client.gui.widget.interfaces.ConfigWidgetEntry;
import evergoodteam.chassis.config.ConfigBase;
import evergoodteam.chassis.util.gui.ColorUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class CategoryOption extends AbstractOption<String> {

    private final ConfigBase config;
    private final List<AbstractOption<?>> options = new ArrayList<>();
    private boolean frame = false;
    private int backgroundColor = ColorUtils.TWHITE;
    private int outlineColor = ColorUtils.AWHITE;

    public CategoryOption(ConfigBase config, String name, String comment) {
        super(name, comment, Text.literal(name), Text.literal(comment));
        this.setComment(comment);
        this.config = config;
    }

    // TODO: better way to do this
    /**
     * Adds a boolean option to a {@link CategoryOption} <p>
     */
    public CategoryOption addBooleanOption(BooleanOption booleanOption) {
        config.getOptionStorage().storeBooleanOption(booleanOption);
        options.add(booleanOption);
        return this;
    }

    public CategoryOption addDoubleOption(DoubleSliderOption doubleOption) {
        config.getOptionStorage().storeDoubleOption(doubleOption);
        options.add(doubleOption);
        return this;
    }

    public CategoryOption addIntegerOption(IntegerSliderOption integerOption) {
        config.getOptionStorage().storeIntegerOption(integerOption);
        options.add(integerOption);
        return this;
    }

    public CategoryOption addStringSetOption(StringSetOption stringSetOption) {
        config.getOptionStorage().storeStringSetOption(stringSetOption);
        options.add(stringSetOption);
        return this;
    }

    public List<AbstractOption<?>> getOptions() {
        return options;
    }

    @Override
    public Builder getBuilder() {
        return new Builder(this);
    }

    @Override
    public boolean isDefaultCommentHidden() {
        return true;
    }

    @Override
    public String getWrittenValue(ConfigBase config) {
        return null;
    }

    public boolean isConfigLockCat() {
        return this.equals(config.getOptionStorage().getConfigLockCat());
    }

    public boolean isResourceLockCat() {
        return this.equals(config.getOptionStorage().getResourceLockCat());
    }

    public boolean isGenericCategory() {
        return this.equals(config.getOptionStorage().getGenericCategory());
    }

    @Environment(value = EnvType.CLIENT)
    @Override
    public WidgetBase getConfigWidget(int width) {
        return new CategoryWidget(width / 2 - 150, 0, 300, 52, this.getDisplayName(), backgroundColor, outlineColor)
                .useFrame(frame)
                .setDescription(Text.literal(this.getComment())); // TODO: check description handling in other places
    }

    @Override
    public void setValue(String newValue) {
    }

    @Override
    public void setValueFromString(String newValue) {
    }

    public void useFrame(boolean frame) {
        this.frame = frame;
    }

    public void setBackgroundColor(int background) {
        this.backgroundColor = background;
    }

    public void setOutlineColor(int outline) {
        this.outlineColor = outline;
    }

    @Environment(value = EnvType.CLIENT)
    public static class CategoryWidget extends TextWidget implements ConfigWidgetEntry {

        private int background;
        private int outline;
        public boolean frame = false;

        public CategoryWidget(int x, int y, int width, int height, Text message, int color, int outline) {
            this(x, y, width, height, message);
            this.background = color;
            this.outline = outline;
            this.truncateWidth = 24;
        }

        CategoryWidget(int x, int y, int width, int height, Text message) {
            super(x, y, width, height, message);
        }

        @Override
        public int getExtendedHeight() {
            return 0;
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            if (this.enabled) {
                if (this.frame) {
                    if (this.getMessage() instanceof GradientText gradientText && gradientText.getPoints().size() > 1) {
                        List<Integer> points = gradientText.getPoints();
                        int first = ColorUtils.ARGB.convertARGBWithAlpha(0x2b, points.get(0));
                        int last = ColorUtils.ARGB.convertARGBWithAlpha(0x2b, points.get(points.size() / 2 + 1));
                        //log.error(points.size());
                        this.drawGradientFrameWithBackground(context, x, y, width, height, first, last, outline);
                    } else this.drawFrameWithBackground(context, x, y, width, height, background, outline);
                } else
                    this.drawRectWithOutline(context, x, y, width, height, background, outline);
                this.renderText(context);
            }
        }

        public CategoryWidget useFrame(boolean frame) {
            this.frame = frame;
            return this;
        }


        @Override
        public void drawHoveredHighlight(WidgetBase widget, DrawContext context, int centerX, int y, double mouseX, double mouseY) {
            if (this.frame) widget.drawRectangle(context, this.x, this.y, this.width, this.height, this.background);
            else
                widget.drawRectWithOutline(context, this.x, this.y, this.width, this.height, this.background, this.outline);
        }

        @Override
        public void playDownSound(SoundManager soundManager) {
        }

        @Override
        public boolean isMouseOverTooltip(WidgetBase widget, double mouseX, double mouseY) {
            return this.isMouseOver(mouseX, mouseY);
        }
    }

    public static class Builder extends AbstractOption.Builder<String, CategoryOption> {

        Builder(CategoryOption option) {
            super(option);
        }

        public Builder useFrame(boolean frame) {
            option.useFrame(frame);
            return this;
        }

        public Builder setBackgroundColor(int background) {
            option.setBackgroundColor(background);
            return this;
        }

        public Builder setOutlineColor(int outline) {
            option.setOutlineColor(outline);
            return this;
        }
    }
}
