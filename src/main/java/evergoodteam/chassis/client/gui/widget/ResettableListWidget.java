package evergoodteam.chassis.client.gui.widget;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import evergoodteam.chassis.client.gui.text.ChassisScreenTexts;
import evergoodteam.chassis.client.gui.widget.interfaces.ConfigWidgetEntry;
import evergoodteam.chassis.config.option.AbstractOption;
import evergoodteam.chassis.config.option.CategoryOption;
import evergoodteam.chassis.util.gui.ColorUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ParentElement;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ElementListWidget;
import org.jetbrains.annotations.Nullable;

import java.util.*;

// TODO: dropdowns at the bottom of the page wont move back up the page when closed
@Environment(value = EnvType.CLIENT)
public class ResettableListWidget extends ElementListWidget<ResettableListWidget.ButtonEntry> {

    public boolean renderSelection = false;

    /**
     * Creates a list of options, each with their dedicated reset button, similarly to the list found in the
     * keybindings screen.
     */
    public ResettableListWidget(MinecraftClient client, int width, int height, int top, int bottom, int scrollAmount) {
        // Using itemHeight as scroll velocity
        super(client, width, height, top, bottom, scrollAmount);
        this.centerListVertically = false;
    }

    public void addAll(AbstractOption<?>[] options) {
        for (AbstractOption<?> option : options) addSingleOption(option);
    }

    public int addSingleOption(AbstractOption<?> option) {
        return this.addEntry(ResettableListWidget.ButtonEntry.create(option, this.width));
    }

    public Optional<WidgetBase> getButtonFor(AbstractOption<?> option) {
        for (ResettableListWidget.ButtonEntry buttonEntry : this.children()) {
            WidgetBase widget = buttonEntry.optionToButton.get(option);
            if (widget != null) return Optional.of(widget);
        }
        return Optional.empty();
    }


    @Override
    public void ensureVisible(ResettableListWidget.ButtonEntry entry) {
        int rowTop = this.getRowTop(this.children().indexOf(entry));
        int j = rowTop - this.top - 4 - entry.height;
        if (j < 0) {
            this.scroll(j);
        }

        int k = this.bottom - rowTop - entry.height - entry.height;
        if (k < 0) {
            this.scroll(-k);
        }
    }

    // Modified version to account for different entry heights and highlights on hover
    @Override
    public void renderEntry(DrawContext context, int mouseX, int mouseY, float delta, int index, int x, int y, int entryWidth, int entryHeight) {
        ResettableListWidget.ButtonEntry entry = this.getEntry(index);

        // Debug
        //entry.drawBorder(context, index, y, x, entryWidth, entry.height, mouseX, mouseY, Objects.equals(this.getHoveredEntry(), entry), delta);
        //entry.drawHoveredBorder(context, index, y, x, entryWidth, entry.height, mouseX, mouseY, Objects.equals(this.getHoveredEntry(), entry), delta);

        if (this.renderSelection && this.isSelectedEntry(index)) {
            int i = this.isFocused() ? -1 : -8355712;
            this.drawSelectionHighlight(context, y, entryWidth, entry.height, i, -16777216);
        }

        entry.render(context, index, y, x, entryWidth, entryHeight, mouseX, mouseY, Objects.equals(this.getHoveredEntry(), entry), delta);
    }

    @Override
    public boolean isSelectedEntry(int index) {
        return Objects.equals(this.getSelectedOrNull(), this.children().get(index));
    }


    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double verticalAmount) {
        // Using itemHeight as scroll velocity
        this.setScrollAmount(this.getScrollAmount() - verticalAmount * this.itemHeight / 2.0);
        return true;
    }


    @Override
    public void centerScrollOn(ResettableListWidget.ButtonEntry entry) {
        super.centerScrollOn(entry);
        // Untested
        /*
        int index = this.children().indexOf(entry);
        int a = 0;
        for(int i = 0; i <= index; i++){
            a += (this.children().get(i).height + 4);
        }

        this.setScrollAmount(a + (double) entry.height / 2 - (double) (this.bottom - this.top) / 2);
        */
    }

    public Optional<WidgetBase> getHoveredWidget(double mouseX, double mouseY) {
        Optional<ButtonEntry> entry = this.getHoveredEntry(mouseX, mouseY);
        if (entry.isPresent()) return entry.get().getHoveredTooltip(mouseX, mouseY);
        return Optional.empty();
    }

    public Optional<ButtonEntry> getHoveredEntry(double mouseX, double mouseY) {
        ButtonEntry entry = getEntryAtPosition(mouseX, mouseY);
        return entry != null ? Optional.of(entry) : Optional.empty();
    }

    @Override
    @Nullable
    public ResettableListWidget.ButtonEntry getEntryAtPosition(double mouseX, double mouseY) {
        int currentPosition = (int) (mouseY - this.top - this.headerHeight + this.getScrollAmount());

        int halfRowWidth = this.getRowWidth() / 2;
        int halfway = this.left + this.width / 2;
        int rowX = halfway - halfRowWidth;
        int rowX2 = halfway + halfRowWidth;

        int indexedHeight = 4; // Account for initial gap

        for (ButtonEntry buttonEntry : this.children()) {
            if (mouseY <= this.top + this.headerHeight || mouseY >= this.bottom)
                continue; // fixes tooltip showing outside actual scrollable list

            if (currentPosition - indexedHeight >= 0 && currentPosition - indexedHeight <= buttonEntry.height) {
                if (mouseX < (double) this.getScrollbarPositionX() && mouseX >= (double) rowX && mouseX <= (double) rowX2) {
                    return buttonEntry;
                }
            }

            int gap = 4;
            indexedHeight += (buttonEntry.height + gap);
        }

        return null;
    }

    @Override
    public int getMaxPosition() {
        int result = this.headerHeight;
        for (ResettableListWidget.ButtonEntry child : this.children()) {
            result += child.height + 4;
        }
        return result;
    }

    @Override
    public int getRowTop(int index) {
        int integer = top + 4 - (int) this.getScrollAmount() + headerHeight;
        for (int i = 0; i < this.children().size() && i < index; i++)
            integer += (this.children().get(i).height + 4);
        return integer;
    }

    @Override
    public int getRowBottom(int index) {
        return this.getRowTop(index) + this.children().get(index).height + 4;
    }

    @Override
    public int getRowWidth() {
        return 400;
    }

    @Override
    protected int getScrollbarPositionX() {
        return super.getScrollbarPositionX() + 32;
    }

    @Environment(value = EnvType.CLIENT)
    public static class ButtonEntry extends ElementListWidget.Entry<ResettableListWidget.ButtonEntry> {

        public int height;
        public AbstractOption<?> option;
        public WidgetBase mainButton;
        public WidgetBase reset;
        public Map<AbstractOption<?>, WidgetBase> optionToButton;
        public List<WidgetBase> children;

        public static ResettableListWidget.ButtonEntry create(AbstractOption<?> option, int width) {
            return new ResettableListWidget.ButtonEntry(option, width);
        }

        private ButtonEntry(AbstractOption<?> option, int width) {
            this.option = option;
            this.mainButton = option.getConfigWidget(width);
            this.setHeight();
            this.reset = resetButton(option, width);
            this.optionToButton = ImmutableMap.of(option, this.mainButton);
            this.setButtonChildren(option);
        }

        /**
         * <p>Adds extra height to the entry to accommodate for things like hover highlighting, specified with {@link ConfigWidgetEntry#hasExtendedHeight()} and {@link ConfigWidgetEntry#getEntryExtendedHeight()}. </p>
         * <p>NOTE: this extra vertical space can to be accounted for in the main widget with {@link WidgetBase#setAddedHeight(int)}</p>
         */
        public void setHeight() {
            this.height = this.mainButton.getHeight();
            if (this.mainButton instanceof ConfigWidgetEntry configWidget && configWidget.hasExtendedHeight())
                this.height += configWidget.getEntryExtendedHeight();
        }

        public void setButtonChildren(AbstractOption<?> option) {
            if (option instanceof CategoryOption) {
                this.children = ImmutableList.of(this.mainButton);
                return;
            }
            List<WidgetBase> result = new ArrayList<>();
            result.add(this.reset); // Has to be on top layer
            result.add(this.mainButton);
            result.addAll(this.mainButton.getChildren());

            this.children = ImmutableList.copyOf(result);
        }

        // TODO: could just update the button instead of replacing it
        private WidgetBase resetButton(AbstractOption<?> option, int width) {
            return new ResetWidget(option,
                    buttonWidget -> {
                        option.reset();
                        if (this.mainButton instanceof ConfigWidgetEntry configWidget) configWidget.onReset();
                    },
                    width, getResetWidgetAddY());
        }

        public int getResetWidgetAddY() {
            if (this.mainButton instanceof ConfigWidgetEntry configWidget) return configWidget.getResetWidgetAddedHeight();
            return this.mainButton.getAddedHeight();
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.setHeight();

            drawHoveredBorder(context, index, y, x, entryWidth, this.height, mouseX, mouseY, hovered, tickDelta);

            this.children.forEach((button) -> {
                if (button == null) return;
                button.setY(y + button.getAddedHeight());
                button.render(context, mouseX, mouseY, tickDelta);
            });
        }

        @Override
        public void drawBorder(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            // Debug
            this.mainButton.drawRectWithOutline(context, x, y, entryWidth, entryHeight, ColorUtils.TWHITE, 0xFF_FF0000);
            context.drawTextWithShadow(this.mainButton.textRenderer, String.valueOf(index), x, y, -1);
        }

        public void drawHoveredBorder(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            int centerX = context.getScaledWindowWidth() / 2;
            if (hovered && (mouseX >= centerX - ConfigWidgetEntry.halfWidth && mouseX < centerX + ConfigWidgetEntry.halfWidth)) {
                // Debug
                //this.mainButton.drawRectWithOutline(context, x, y, entryWidth, entryHeight, ColorUtils.TWHITE, 0xFF_00FF00);
                if (this.mainButton instanceof ConfigWidgetEntry)
                    drawHoveredHighlight(context, centerX, y, mouseX, mouseY);
            }
        }

        // Checks for mouse position are done before
        public void drawHoveredHighlight(DrawContext context, int centerX, int y, int mouseX, int mouseY) {
            ((ConfigWidgetEntry) this.mainButton).drawHoveredHighlight(this.mainButton, context, centerX, y, mouseX, mouseY);
        }

        @Override
        public List<? extends Element> children() {
            return this.children;
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return this.children;
        }

        // TODO: could cover a way wider range of possibilities, but this works for our case
        public Optional<WidgetBase> getHoveredTooltip(double mouseX, double mouseY) {
            WidgetBase hovered = null;
            if (this.mainButton instanceof ParentElement parent) {
                Optional<Element> element = parent.hoveredElement(mouseX, mouseY);
                if (element.isPresent()) hovered = (WidgetBase) element.get();
            } else if (this.mainButton instanceof ConfigWidgetEntry configWidget) {
                if (configWidget.isMouseOverTooltip(this.mainButton, mouseX, mouseY)) hovered = this.mainButton;
            } else {
                if (this.mainButton.isMouseOver(mouseX, mouseY)) hovered = this.mainButton;
            }

            return hovered != null ? Optional.of(hovered) : Optional.empty();
        }
    }

    public static class ResetWidget extends WidgetBase {

        private AbstractOption<?> option;

        public ResetWidget(AbstractOption<?> option, PressAction onPress, int width, int addY) {
            super(onPress, width / 2 + 102, 0, 40, 20, ChassisScreenTexts.RESET);
            this.setAddedHeight(addY);
            this.option = option;
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            this.active = !this.option.getValue().equals(this.option.getDefaultValue());
            super.render(context, mouseX, mouseY, delta);
        }
    }
}