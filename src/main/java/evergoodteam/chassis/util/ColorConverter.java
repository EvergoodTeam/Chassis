package evergoodteam.chassis.util;

public class ColorConverter {

    public static int getDecimalFromHex(String hex) {
        return Integer.parseInt(hex, 16);
    }

    public static int getDecimalFromRGB(int r, int g, int b) {
        return (r << 16) + (g << 8) + b;
    }

    public static String getHexFromRGB(int r, int g, int b) {
        String rs = Integer.toHexString(r * 256);
        String gs = Integer.toHexString(g * 256);
        String bs = Integer.toHexString(b * 256);
        return rs + gs + bs;
    }
}
