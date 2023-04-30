package evergoodteam.chassis.client.gui.widgets;

import evergoodteam.chassis.client.gui.text.GradientText;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

/**
 * Displays centered text without rendering the button texture
 */
public class TextWidget extends WidgetBase {

    public Text description = Text.empty();
    public int titleTransparency = 0xFF_FFFFFF;

    public TextWidget(int x, int y, int width, int height, Text message, PressAction onPress) {
        super(x, y, width, height, message, onPress);
    }

    public TextWidget(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

    public TextWidget setDescription(Text description) {
        this.description = description;
        return this;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.hovered = this.isMouseOver(mouseX, mouseY);
        if (this.hovered) onHover();
        this.renderBackground(matrices, mouseX, mouseY);
        this.renderButton(matrices, this.x, this.y, this.width, this.height);
        this.renderText(matrices);
    }

    public void renderText(MatrixStack matrices) {
        boolean desc = description.getString().equals("");
        this.renderTitle(matrices, !desc);
        if (!desc) this.renderDescription(matrices);
    }

    public void renderDescription(MatrixStack matrices) {
        this.renderCenteredText(matrices, description, this.y + ((this.height - 8) / 4) * 3);
    }

    public void renderTitle(MatrixStack matrices, boolean description) {
        int y = description ? this.y + ((this.height - 8) / 4) : this.y + (this.height - 8) / 2;
        if (this.getMessage() instanceof GradientText) ((GradientText) this.getMessage()).scroll();
        TextWidget.gradientText(matrices, gradientTextRenderer, GradientText.copyOf(this.getMessage(), this.truncateString(this.getMessage().getString())), null, this.x + (this.width / 2), y, this.titleTransparency);
    }
}
