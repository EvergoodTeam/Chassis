package evergoodteam.chassis.configs.screen;

import com.google.common.collect.ImmutableList;
import evergoodteam.chassis.configs.widgets.WidgetBase;
import evergoodteam.chassis.configs.widgets.ResettableListWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Optional;

public class ConfigOptionsScreen extends Screen {

    protected final Screen parent;
    protected final GameOptions gameOptions;

    public ConfigOptionsScreen(Screen parent, GameOptions gameOptions, Text title) {
        super(title);
        this.parent = parent;
        this.gameOptions = gameOptions;
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
}
