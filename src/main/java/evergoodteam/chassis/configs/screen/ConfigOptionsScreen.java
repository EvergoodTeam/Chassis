package evergoodteam.chassis.configs.screen;

import com.google.common.collect.ImmutableList;
import evergoodteam.chassis.client.ChassisClient;
import evergoodteam.chassis.client.gui.widgets.WidgetBase;
import evergoodteam.chassis.client.gui.widgets.ResettableListWidget;
import evergoodteam.chassis.client.gui.text.GradientTextRenderer;
import evergoodteam.chassis.configs.ConfigBase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class ConfigOptionsScreen extends Screen {

    protected final Screen parent;
    protected final ConfigBase config;
    protected final GradientTextRenderer gradientTextRenderer = ChassisClient.gradientTextRenderer;

    public ConfigOptionsScreen(Screen parent, ConfigBase config) {
        super(config.getTitle());
        this.parent = parent;
        this.config = config;
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }

    public static List<OrderedText> getHoveredButtonTooltip(ResettableListWidget buttonList, int mouseX, int mouseY) {
        Optional<WidgetBase> optional = buttonList.getHoveredTooltip(mouseX, mouseY);
        if (optional.isPresent()) {
            return optional.get().getOrderedTooltip();
        }
        return ImmutableList.of();
    }

    public void drawCenteredGradientText(MatrixStack matrices, @Nullable List<Integer> points){
        gradientTextRenderer.drawWithShadow(matrices, this.title, points, (float) (this.width / 2 - gradientTextRenderer.getWidth(this.title.asOrderedText()) / 2), 15, 16777215);
    }
}
