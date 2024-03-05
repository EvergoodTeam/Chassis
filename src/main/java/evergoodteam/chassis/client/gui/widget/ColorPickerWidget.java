package evergoodteam.chassis.client.gui.widget;

import evergoodteam.chassis.client.gui.text.ChassisScreenTexts;
import evergoodteam.chassis.util.gui.ColorUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.awt.*;

@Environment(value = EnvType.CLIENT)
public class ColorPickerWidget extends DropdownWidget {

    protected TextFieldWidget textField;
    protected AHSBWidget colorPicker;

    public ColorPickerWidget(int x, int y, int width, int height, int color) {
        this(x, y, width, height, color, Text.empty());
    }

    public ColorPickerWidget(int x, int y, int width, int height, int color, Text message) {
        super(x, y, width, height, 22, message);
        this.initChildren(color);
    }

    public void initChildren(int color) {
        this.textField = new TextFieldWidget(x + width / 2, 0, 100, 20, Text.empty());
        this.textField.setAddedY(2);
        this.textField.setOrderedTooltip(ChassisScreenTexts.ARGB);
        this.addToWhitelist(textField);

        this.colorPicker = new AHSBWidget(x + space, color);
        this.colorPicker.setAddedY(2 + 20 + space);
        this.colorPicker.setOrder(colorPicker.sbPicker, colorPicker.hueSlider, colorPicker.alphaSlider);

        this.children.add(textField);
        this.children.add(colorPicker);
        this.children.addAll(colorPicker.getChildren());
    }

    // TODO: [NU] add rgb TextFields
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderPreview(context, mouseX, mouseY, delta);
        // Tooltip color preview, visible only when dropdown is open
        //renderTextPreview(context, mouseX, mouseY, delta);
    }

    /**
     * Renders a preview of the chosen colour. If the dropdown is closed, the widget's message will be displayed.
     */
    public void renderPreview(DrawContext context, int mouseX, int mouseY, float delta) {
        drawRectWithOutline(context, x + space, y + 2, colorPicker.sbPicker.width, 20, colorPicker.ashb.getValue(), ColorUtils.BLACK);

        int reversed = Color.HSBtoRGB(1f, 0f, 1f - colorPicker.ashb.getBrightness());
        if (!this.colorPicker.isMouseOverComps(mouseX, mouseY))
            context.drawText(textRenderer, this.getMessage(), (x + space + colorPicker.sbPicker.width / 2) - textRenderer.getWidth(this.getMessage()) / 2, y + 8, reversed, false);
    }

    public void renderTextPreview(DrawContext context, int mouseX, int mouseY, float delta) {
        if (!isOpen()) return;
        Text message = this.getMessage().copyContentOnly().fillStyle(Style.EMPTY.withColor(colorPicker.ashb.getValue()));
        drawWrappedTooltip(textRenderer, context, message, colorPicker.x + colorPicker.getWidth(), colorPicker.y + 16);
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
    }
}