package evergoodteam.chassis.client.gui.widget;

import evergoodteam.chassis.client.gui.text.GradientText;
import evergoodteam.chassis.util.gui.ColorUtils;
import lombok.extern.log4j.Log4j2;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * Displays centered text without rendering the button texture
 */
@Log4j2
public class TextWidget extends WidgetBase {

    public Text description = Text.empty();
    public int titleTransparency = ColorUtils.WHITE;

    public TextWidget(int x, int y, int width, int height, Text message) {
        this(null, x, y, width, height, message);
    }

    public TextWidget(WidgetUpdateCallback updateCallback, int x, int y, int width, int height, Text message) {
        super(updateCallback, x, y, width, height, message);
        updateTooltip();
    }

    public void updateTooltip() {
        if (isTitleTruncatable() && isDescriptionTruncatable()) {
            Text tt = Text.literal(getMessage().getString() + "\n\n")
                    .append(Text.literal(description.getString())
                            .formatted(Formatting.GRAY));
            this.setOrderedTooltip(tt);
        } else if (isTitleTruncatable()) this.setOrderedTooltip(GradientText.copyOf(this.getMessage()));
        else if (isDescriptionTruncatable()) this.setOrderedTooltip(GradientText.copyOf(this.description));
    }

    /**
     * Returns true if the message has to be truncated because of excessive length
     */
    public boolean isTitleTruncatable() {
        return this.isTruncatable(this.getMessage());
    }

    public boolean isDescriptionTruncatable() {
        if (this.description == null) return false;
        return this.isTruncatable(this.description);
    }

    public TextWidget setDescription(Text description) {
        this.description = description;
        updateTooltip();
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
        boolean hasDesc = description.getString().isEmpty();
        this.renderTitle(context, !hasDesc);
        if (!hasDesc) this.renderDescription(context);
    }

    public void renderDescription(DrawContext context) {
        this.renderCenteredText(context, description, (int) (this.y + ((this.height - 8) / 6) * 4.5));
    }

    public void renderTitle(DrawContext context, boolean description) {
        int y = description ? this.y + ((this.height - 8) / 6) * 2 : this.y + (this.height - 8) / 2;
        if (this.getMessage() instanceof GradientText) ((GradientText) this.getMessage()).scroll();
        TextWidget.drawCenteredGradientText(context, gradientTextRenderer, getMessageCopy(), this.x + (this.width / 2), y, this.titleTransparency);
    }

    public GradientText getMessageCopy() {
        return GradientText.copyOf(this.getMessage(), this.truncateString(this.getMessage().getString(), this.truncateWidth + 8));
    }
}
