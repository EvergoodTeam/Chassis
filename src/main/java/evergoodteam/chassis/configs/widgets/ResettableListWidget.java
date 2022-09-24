package evergoodteam.chassis.configs.widgets;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import evergoodteam.chassis.configs.options.OptionBase;
import evergoodteam.chassis.configs.screen.ChassisScreenTexts;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Environment(value = EnvType.CLIENT)
public class ResettableListWidget extends ElementListWidget<ResettableListWidget.ButtonEntry> {

    public ResettableListWidget(MinecraftClient client, int width, int height, int top, int bottom, int itemHeight) {
        super(client, width, height, top, bottom, itemHeight);
        this.centerListVertically = false;
    }

    public void addAll(OptionBase<?>[] options) {
        addCategoryText("General Options");
        for (OptionBase<?> option : options) addResettableSingleOptionEntry(option);
    }

    public int addResettableSingleOptionEntry(OptionBase<?> option) {
        return this.addEntry(ResettableListWidget.ButtonEntry.create(this.width, option));
    }

    public int addCategoryText(String title) {
        return this.addEntry(ResettableListWidget.ButtonEntry.create(this.width, title));
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
    public WidgetBase getButtonFor(OptionBase<?> option) {
        for (ResettableListWidget.ButtonEntry buttonEntry : this.children()) {
            WidgetBase clickableWidget = buttonEntry.optionToButton.get(option);
            if (clickableWidget == null) continue;
            return clickableWidget;
        }
        return null;
    }

    public Optional<WidgetBase> getHoveredTooltip(double mouseX, double mouseY) {
        for (ResettableListWidget.ButtonEntry buttonEntry : this.children()) {
            for (WidgetBase clickableWidget : buttonEntry.buttons) {
                if (!clickableWidget.isMouseOver(mouseX, mouseY,
                        clickableWidget.x - 150, clickableWidget.y - 2, 150, 24)) continue;
                return Optional.of(clickableWidget);
            }
        }
        return Optional.empty();
    }

    @Environment(value = EnvType.CLIENT)
    public static class ButtonEntry extends ElementListWidget.Entry<ResettableListWidget.ButtonEntry> {

        public int height;
        public OptionBase<?> option;
        public WidgetBase button;
        public WidgetBase reset;
        public Map<OptionBase<?>, WidgetBase> optionToButton;
        public List<WidgetBase> buttons;

        private ButtonEntry(OptionBase<?> option, int width) {
            this.option = option;
            this.height = 20;
            this.button = valueButton(option, width);
            this.reset = resetButton(option, width);
            this.optionToButton = ImmutableMap.of(option, this.button);
            this.buttons = ImmutableList.of(this.button, this.reset);
        }

        /**
         * Category divider
         */
        private ButtonEntry(String text, int width) {
            this.height = 32;
            this.button = new TextWidget(width / 2 - 150, 0, 300, this.height, Text.literal(text), 0x2B_FFFFFF, 0x5E_FFFFFF);
            this.buttons = ImmutableList.of(this.button);
        }

        public WidgetBase valueButton(OptionBase<?> option, int width) {
            return option.getConfigWidget(width);
        }

        private WidgetBase resetButton(OptionBase<?> option, int width) {
            return new WidgetBase(width / 2 + 102, 0, 40, 20, ChassisScreenTexts.RESET,
                    buttonWidget -> {
                        option.reset();
                        this.button = valueButton(option, width);
                        this.reset = resetButton(option, width);
                        this.buttons = ImmutableList.of(this.button, this.reset);
                    });
        }

        public static ResettableListWidget.ButtonEntry create(int width, OptionBase<?> option) {
            return new ResettableListWidget.ButtonEntry(option, width);
        }

        public static ResettableListWidget.ButtonEntry create(int width, String text) {
            return new ResettableListWidget.ButtonEntry(text, width);
        }

        @Override
        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {

            this.buttons.forEach((button) -> {
                button.y = y;
                button.render(matrices, mouseX, mouseY, tickDelta);
            });

            if (this.optionToButton != null) this.optionToButton.forEach((option, button) -> {
                MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices,
                        Text.translatable(option.getName()).getString(),
                        button.x - 142, y + (entryHeight - 8) / 2, 16777215);
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
}