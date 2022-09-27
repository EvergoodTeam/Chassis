package evergoodteam.chassis.configs.screen;

import evergoodteam.chassis.configs.ConfigBase;
import evergoodteam.chassis.configs.options.OptionBase;
import evergoodteam.chassis.configs.widgets.ResettableListWidget;
import evergoodteam.chassis.util.ColorConverter;
import evergoodteam.chassis.util.StringUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static evergoodteam.chassis.util.Reference.CMI;
import static org.slf4j.LoggerFactory.getLogger;

@Environment(EnvType.CLIENT)
public class ConfigScreen extends ConfigOptionsScreen {

    private static final Logger LOGGER = getLogger(CMI + "/C/Screen");

    public Screen parent;
    public ResettableListWidget list;
    public ConfigBase config;
    public Boolean retainValues;

    public ConfigScreen(Screen parent, ConfigBase config) {
        super(parent, MinecraftClient.getInstance().options,
                Text.literal(StringUtils.capitalize(config.namespace) + " Configs").setStyle(ColorConverter.coloredStyle("65c18c")));
        this.parent = parent;
        this.config = config;
        this.retainValues = false;
    }

    static List<OptionBase<?>> optionList;

    @Override
    protected void init() {
        if (!retainValues) { // Prevent values from being reset
            optionList = new ArrayList<>();
            config.readProperties();
            optionList.addAll(config.getOptionStorage().getOptions());
        } else retainValues = false;

        this.list = new ResettableListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        this.list.addCategoryText("Options");
        this.list.addAll(optionList.toArray(new OptionBase[0]));
        this.addSelectableChild(this.list);

        this.initFooter();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        this.list.render(matrices, mouseX, mouseY, delta);

        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 15, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
        List<OrderedText> list = getHoveredButtonTooltip(this.list, mouseX, mouseY);
        this.renderOrderedTooltip(matrices, list, mouseX, mouseY);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.client.setScreen(new ConfirmScreen(this::discardCallback, ChassisScreenTexts.DISCARD, ChassisScreenTexts.DISCARD_D));
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void discardCallback(boolean discard) {
        if (discard) {
            this.close();
        } else {
            this.retainValues = true;
            this.client.setScreen(this);
        }
    }

    public void initFooter() {
        this.addDrawableChild(new ButtonWidget(this.width / 2 + 60, this.height - 27, 100, 20,
                ChassisScreenTexts.RESET_A,
                buttonWidget -> {
                    config.getOptionStorage().getOptions().forEach(OptionBase::reset);

                    this.remove(this.list);
                    this.list = new ResettableListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
                    this.list.addCategoryText("Options");
                    this.list.addAll(optionList.toArray(new OptionBase[0]));
                    this.addSelectableChild(this.list);

                    LOGGER.debug("Reset config options for \"{}\"", config.namespace);
                }));

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 160, this.height - 27, 100, 20,
                ChassisScreenTexts.OPEN,
                buttonWidget -> {
                    config.openConfigFile();
                }));

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 50, this.height - 27, 100, 20,
                ChassisScreenTexts.SAVE,
                buttonWidget -> {

                    config.getOptionStorage().getOptions().forEach(option -> config.setWrittenValue(option.getName(), option.getValue()));
                    config.readProperties();

                    LOGGER.debug("Saved config options for \"{}\"", config.namespace);

                    client.setScreen(this.parent);
                }));
    }
}
