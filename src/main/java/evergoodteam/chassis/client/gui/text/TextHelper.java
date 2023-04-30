package evergoodteam.chassis.client.gui.text;

import evergoodteam.chassis.util.gui.ColorUtils;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

public class TextHelper {

    public static Text colored(String text, String hex){
        return Text.literal(text).setStyle(getColor(hex));
    }

    public static TextColor getColor(Integer color){
        return Style.EMPTY.withColor(color).getColor();
    }

    public static Style getColor(String hex){
        return Style.EMPTY.withColor(ColorUtils.getDecimalFromHex(hex));
    }
}
