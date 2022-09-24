package evergoodteam.chassis.util;

import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.awt.*;

public class ColorConverter {

    public static Text coloredText(String text, String hexColor){
        return Text.literal(text).setStyle(coloredStyle(hexColor));
    }

    public static Style coloredStyle(String hex){
        return Style.EMPTY.withColor(getDecimalFromHex(hex));
    }

    //region Decimal

    /**
     * Converts a Color object to a decimal color code
     */
    public static int getDecimalFromColor(Color color) {
        return getDecimalFromRGB(getRGBFromColor(color));
    }

    /**
     * Converts a hex color code to a decimal color code
     *
     * @param hex color code WITHOUT '#'
     */
    public static int getDecimalFromHex(String hex) {
        return Integer.parseInt(hex, 16);
    }

    public static int getDecimalFromRGB(Integer[] rgb) {
        return (rgb[0] * 65536) + (rgb[1] * 256) + rgb[2];
    }

    /**
     * Converts a RGB color code to a decimal color code
     */
    public static int getDecimalFromRGB(int r, int g, int b) {
        return (r * 65536) + (g * 256) + b;
    }
    //endregion

    //region RGB

    /**
     * Converts a decimal color code to a RGB color code
     */
    public static Integer[] getRGBFromDecimal(int decimal) {
        return getRGBFromColor(new Color(decimal));
    }

    /**
     * Converts a Color object to a RGB color code
     */
    public static Integer[] getRGBFromColor(Color color) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();

        return new Integer[]{red, green, blue};
    }
    //endregion

    //region Hex

    /**
     * Converts a Color object to a hex color code
     */
    public static String getHexFromColor(Color color) {
        return getHexFromRGB(getRGBFromColor(color));
    }

    /**
     * Converts a decimal color code to a hex color code
     */
    public static String getHexFromDecimal(int decimal) {
        return getHexFromRGB(getRGBFromDecimal(decimal));
    }

    /**
     * Converts a RGB color code to a hex color code
     */
    public static String getHexFromRGB(Integer[] rgb) {
        return getHexFromRGB(rgb[0], rgb[1], rgb[2]);
    }

    /**
     * Converts a RGB color code to a hex color code
     */
    public static String getHexFromRGB(int r, int g, int b) {
        String rs = Integer.toHexString(r * 256);
        String gs = Integer.toHexString(g * 256);
        String bs = Integer.toHexString(b * 256);
        return rs + gs + bs;
    }
    //endregion
}
