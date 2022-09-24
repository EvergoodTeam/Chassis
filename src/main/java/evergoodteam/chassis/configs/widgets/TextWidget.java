package evergoodteam.chassis.configs.widgets;

import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

/**
 * Displays centered text without rendering the widget texture
 */
public class TextWidget extends WidgetBase {

    public int color = 0x5E_FFFFFF;
    public int outline = 0xFF_FFFFFF;

    public TextWidget(int x, int y, int width, int height, Text message, int color, int outline) {
        this(x, y, width, height, message);
        this.color = color;
        this.outline = outline;
    }

    public TextWidget(int x, int y, int width, int height, Text message, int color) {
        this(x, y, width, height, message);
        this.color = color;
        this.outline = color;
    }

    public TextWidget(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.drawRectWithOutline(matrices, this.x, this.y, this.width, this.height, this.color, this.outline);
        this.renderCenteredText(matrices);
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
    }
}
