package evergoodteam.chassis.client.gui.widget;

import evergoodteam.chassis.client.gui.text.GradientText;
import lombok.extern.log4j.Log4j2;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

/**
 * Displays centered text without rendering the button texture
 */
@Log4j2
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
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.hovered = this.isMouseOver(mouseX, mouseY);
        if (this.hovered) onHover();
        this.renderBackground(context, mouseX, mouseY);
        this.renderButton(context, this.x, this.y, this.width, this.height);
        this.renderText(context);
    }

    public void renderText(DrawContext context) {
        boolean desc = description.getString().equals("");
        this.renderTitle(context, !desc);
        if (!desc) this.renderDescription(context);
    }

    public void renderDescription(DrawContext context) {
        this.renderCenteredText(context, description, this.y + ((this.height - 8) / 4) * 3);
    }

    public void renderTitle(DrawContext context, boolean description) {
        int y = description ? this.y + ((this.height - 8) / 4) : this.y + (this.height - 8) / 2;
        if (this.getMessage() instanceof GradientText) ((GradientText) this.getMessage()).scroll();
        TextWidget.drawCenteredGradientText(context, gradientTextRenderer, GradientText.copyOf(this.getMessage(), this.truncateString(this.getMessage().getString())), this.x + (this.width / 2), y, this.titleTransparency);
    }
}
