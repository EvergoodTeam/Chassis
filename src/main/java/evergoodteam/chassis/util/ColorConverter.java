package evergoodteam.chassis.util;

import evergoodteam.chassis.util.gui.ColorUtils;

/**
 * @deprecated as of release 1.2.3, moved to {@link ColorUtils}
 */
@Deprecated
public class ColorConverter {

    /**
     * Convert a hex color code to a decimal color code
     *
     * @param hex color code WITHOUT '#'
     */
    public static int getDecimalFromHex(String hex) {
        return ColorUtils.getDecimalFromHex(hex);
    }

    /**
     * Convert a RGB color code to a decimal color code
     */
    public static int getDecimalFromRGB(int r, int g, int b) {
        return ColorUtils.getDecimalFromRGB(r, g, b);
    }

    /**
     * Convert a RGB color code to a hex color code
     */
    public static String getHexFromRGB(int r, int g, int b) {
        return ColorUtils.getHexFromRGB(r, g, b);
    }
}
