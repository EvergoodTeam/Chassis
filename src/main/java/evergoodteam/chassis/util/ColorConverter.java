package evergoodteam.chassis.util;

public class ColorConverter {

    /**
     * Convert a hex color code to a decimal color code
     *
     * @param hex color code WITHOUT '#'
     */
    public static int getDecimalFromHex(String hex) {
        return Integer.parseInt(hex, 16);
    }

    /**
     * Convert a RGB color code to a decimal color code
     */
    public static int getDecimalFromRGB(int r, int g, int b) {
        return (r << 16) + (g << 8) + b;
    }

    /**
     * Convert a RGB color code to a hex color code
     */
    public static String getHexFromRGB(int r, int g, int b) {
        String rs = Integer.toHexString(r * 256);
        String gs = Integer.toHexString(g * 256);
        String bs = Integer.toHexString(b * 256);
        return rs + gs + bs;
    }
}
