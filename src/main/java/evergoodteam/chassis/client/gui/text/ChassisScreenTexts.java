package evergoodteam.chassis.client.gui.text;

import net.minecraft.text.Text;

public class ChassisScreenTexts {
    public static final String GREEN = "44ff6b";
    public static final String RED = "ff0046";

    public static final Text SAVE = Text.translatable("config.chassis.save");
    public static final Text OPEN = Text.translatable("config.chassis.open");
    public static final Text RESET = Text.translatable("config.chassis.reset");
    public static final Text RESET_A = Text.translatable("config.chassis.resetall");
    public static final Text DISCARD = Text.translatable("config.chassis.discard");
    public static final Text DISCARD_D = Text.translatable("config.chassis.discard.desc");
    public static final Text ON = TextHelper.colored("ON", GREEN);
    public static final Text OFF = TextHelper.colored("OFF", RED);
}
