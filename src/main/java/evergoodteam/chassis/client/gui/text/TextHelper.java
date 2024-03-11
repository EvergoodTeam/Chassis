package evergoodteam.chassis.client.gui.text;

import evergoodteam.chassis.util.gui.ColorUtils;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

public class TextHelper {

    public static Text colored(String text, String hex){
        return Text.literal(text).setStyle(getStyleColor(hex));
    }

    public static Text colored(String text, int color){
        return Text.literal(text).setStyle(getStyleColor(color));
    }

    public static TextColor getTextColor(Integer color){
        return getStyleColor(color).getColor();
    }

    public static Style getStyleColor(String hex){
        return Style.EMPTY.withColor(ColorUtils.RGB.getIntFromHexRGB(hex));
    }

    public static Style getStyleColor(int color){
        return Style.EMPTY.withColor(color);
    }
}
