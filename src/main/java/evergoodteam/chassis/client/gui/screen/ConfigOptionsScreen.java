package evergoodteam.chassis.client.gui.screen;

import com.google.common.collect.ImmutableList;
import evergoodteam.chassis.client.ChassisClient;
import evergoodteam.chassis.client.gui.DrawingUtils;
import evergoodteam.chassis.client.gui.text.GradientTextRenderer;
import evergoodteam.chassis.client.gui.widget.ResettableListWidget;
import evergoodteam.chassis.client.gui.widget.TextWidget;
import evergoodteam.chassis.client.gui.widget.WidgetBase;
import evergoodteam.chassis.config.ConfigBase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.OrderedText;

import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class ConfigOptionsScreen extends Screen {

    protected final Screen parent;
    protected final ConfigBase config;
    protected final GradientTextRenderer gradientTextRenderer = ChassisClient.gradientTextRenderer;

    public ConfigOptionsScreen(Screen parent, ConfigBase config) {
        super(config.getDisplayTitle());
        this.parent = parent;
        this.config = config;
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }

    public void drawCenteredGradientTitle(DrawContext context) {
        DrawingUtils.drawCenteredGradientText(context, gradientTextRenderer, this.title, this.width / 2, 15, 16777215);
    }

    // TODO: [NU] have tooltips also use gradients when present in the text
    public void renderTooltip(DrawContext context, ResettableListWidget buttonList, int mouseX, int mouseY) {

        Optional<WidgetBase> hoveredWidget = buttonList.getHoveredWidget(mouseX, mouseY);
        if (hoveredWidget.isPresent()) {
            WidgetBase current = hoveredWidget.get();
            List<OrderedText> tooltipList = current.getOrderedTooltip();
            if (current instanceof TextWidget)
                context.chassisDrawOrderedTooltipWithoutJump(textRenderer, tooltipList, mouseX, mouseY);
            else context.drawOrderedTooltip(textRenderer, tooltipList, mouseX, mouseY);
        }


    }

    public static List<OrderedText> getHoveredButtonTooltip(ResettableListWidget buttonList, int mouseX, int mouseY) {
        Optional<WidgetBase> optional = buttonList.getHoveredWidget(mouseX, mouseY);
        if (optional.isPresent()) return optional.get().getOrderedTooltip();
        return ImmutableList.of();
    }
}
