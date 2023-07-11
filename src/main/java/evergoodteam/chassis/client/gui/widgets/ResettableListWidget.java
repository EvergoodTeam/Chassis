package evergoodteam.chassis.client.gui.widgets;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import evergoodteam.chassis.client.gui.text.ChassisScreenTexts;
import evergoodteam.chassis.configs.options.AbstractOption;
import evergoodteam.chassis.configs.options.CategoryOption;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ElementListWidget;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Environment(value = EnvType.CLIENT)
public class ResettableListWidget extends ElementListWidget<ResettableListWidget.ButtonEntry> {

    /**
     * Creates a list of options, each with their dedicated reset button, similarly to the list found in the
     * keybindings screen.
     *
     * @param tallestItem the height of the tallest item in the list
     */
    public ResettableListWidget(MinecraftClient client, int width, int height, int top, int bottom, int tallestItem) {
        super(client, width, height, top, bottom, tallestItem);
        this.centerListVertically = false;
    }

    public void addAll(AbstractOption<?>[] options) {
        for (AbstractOption<?> option : options) addSingleOptionEntry(option);
    }

    public int addSingleOptionEntry(AbstractOption<?> option) {
        return this.addEntry(ResettableListWidget.ButtonEntry.create(option, this.width));
    }

    @Override
    @Nullable
    public ResettableListWidget.ButtonEntry getEntryAtPosition(double mouseX, double mouseY) {
        for (ButtonEntry buttonEntry : this.children()) {
            if (buttonEntry.button.isMouseOver(mouseX, mouseY)) return buttonEntry;
            else if (buttonEntry.reset != null) {
                if (buttonEntry.reset.isMouseOver(mouseX, mouseY)) return buttonEntry;
            }
        }
        return null;
    }

    @Override
    public int getMaxPosition() {
        int integer = this.headerHeight;
        for (int i = 0; i < children().size(); i++)
            integer += (children().get(i).height + 4);
        return integer;
    }

    @Override
    public int getRowTop(int index) {
        int integer = top + 4 - (int) this.getScrollAmount() + headerHeight;
        for (int i = 0; i < children().size() && i < index; i++)
            integer += (children().get(i).height + 4);
        return integer;
    }

    @Override
    public int getRowWidth() {
        return 400;
    }

    @Override
    protected int getScrollbarPositionX() {
        return super.getScrollbarPositionX() + 32;
    }

    @Nullable
    public WidgetBase getButtonFor(AbstractOption<?> option) {
        for (ResettableListWidget.ButtonEntry buttonEntry : this.children()) {
            WidgetBase widget = buttonEntry.optionToButton.get(option);
            if (widget == null) continue;
            return widget;
        }
        return null;
    }

    public Optional<WidgetBase> getHoveredTooltip(double mouseX, double mouseY) {
        if (mouseY > top && mouseY < bottom) {
            for (ResettableListWidget.ButtonEntry buttonEntry : this.children()) {
                for (WidgetBase clickableWidget : buttonEntry.buttons) {
                    if (!clickableWidget.isMouseOver(mouseX, mouseY, clickableWidget.x - 150, clickableWidget.y - 2, 150, 24))
                        continue;
                    return Optional.of(clickableWidget);
                }
            }
        }
        return Optional.empty();
    }

    @Environment(value = EnvType.CLIENT)
    public static class ButtonEntry extends ElementListWidget.Entry<ResettableListWidget.ButtonEntry> {

        public int height;
        public AbstractOption<?> option;
        public WidgetBase button;
        public WidgetBase reset;
        public Map<AbstractOption<?>, WidgetBase> optionToButton;
        public List<WidgetBase> buttons;

        private ButtonEntry(AbstractOption<?> option, int width) {
            this.option = option;
            this.height = option.getConfigWidget(width).height;
            this.button = valueButton(option, width);
            this.reset = resetButton(option, width);
            this.optionToButton = ImmutableMap.of(option, this.button);
            this.buttons = option instanceof CategoryOption ? ImmutableList.of(this.button) : ImmutableList.of(this.button, this.reset);
        }

        public WidgetBase valueButton(AbstractOption<?> option, int width) {
            return option.getConfigWidget(width);
        }

        private WidgetBase resetButton(AbstractOption<?> option, int width) {
            return new ResetWidget(option,
                    buttonWidget -> {
                        option.reset();
                        this.button = valueButton(option, width);
                        this.reset = resetButton(option, width);
                        this.buttons = ImmutableList.of(this.button, this.reset);
                    },
                    width);
        }

        public static ResettableListWidget.ButtonEntry create(AbstractOption<?> option, int width) {
            return new ResettableListWidget.ButtonEntry(option, width);
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.buttons.forEach((button) -> {
                button.y = y;
                button.render(context, mouseX, mouseY, tickDelta);
            });
        }

        @Override
        public List<? extends Element> children() {
            return this.buttons;
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return this.buttons;
        }
    }

    public static class ResetWidget extends WidgetBase {

        private AbstractOption<?> option;

        public ResetWidget(AbstractOption<?> option, PressAction onPress, int width) {
            super(width / 2 + 102, 0, 40, 20, ChassisScreenTexts.RESET, onPress);
            this.option = option;
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            this.active = !this.option.getValue().equals(this.option.getDefaultValue());
            super.render(context, mouseX, mouseY, delta);
        }
    }
}