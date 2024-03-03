package evergoodteam.chassis.util.gui;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ColorUtils {

    public static final int BLACK = 0xFF_000000; // -16777216
    public static final int WHITE = -1;
    public static final int TWHITE = 0x2B_FFFFFF;
    public static final int AWHITE = 0xB6_FFFFFF;
    public static final Pattern argbHex = Pattern.compile("^([a-fA-F0-9]{8})$");
    public static final Pattern rgbHex = Pattern.compile("^([a-fA-F0-9]{6})$");

    /**
     * Returns a list of decimal ARGB color codes: each represents a color obtained from calculating the various intermediates
     * between the specified hex color points. The result is a full gradient that is supposed to cover the provided
     * string's length.
     *
     * @param length length for the overall gradient: a higher amount will result in smoother and longer gradient transitions
     * @param points hex colors to be used as transition points
     */
    public static List<Integer> getGradientPoints(int length, String... points) {
        return getGradientPoints("x".repeat(length), points);
    }

    /**
     * Returns a list of decimal ARGB color codes (all opaque): each represents a color obtained from calculating the various intermediates
     * between the specified hex color points. The result is a full gradient that is supposed to cover the provided
     * string's length.
     *
     * @param string string whose length is the base for the amount of points generated
     * @param points hex colors to be used as transition points
     */
    // TODO: [NU] if last isnt same as first, automatically add a transition
    public static List<Integer> getGradientPoints(@NotNull String string, String... points) {
        if (points == null || points.length == 0) return List.of(16777215);
        else if (points.length == 1) return List.of(RGB.getIntFromHexRGB(points[0]));

        List<Integer> result = new ArrayList<>();
        int lengthNoSpaces;
        boolean singleChar = false;

        if (string.length() != 1) lengthNoSpaces = string.replaceAll(" ", "").length();
        else {
            singleChar = true;
            lengthNoSpaces = points.length * 5;
        }

        int interval = lengthNoSpaces / (points.length - 1);
        int remainder = lengthNoSpaces % (points.length - 1);
        int letterCounter = 0;

        for (int i = 0; i < points.length - 1; i++) {
            // TODO: [NU] improve remainder distribution
            int updatedInterval = interval;
            if (remainder > 0) {
                updatedInterval++;
                remainder--;
            }

            // Generate transition between each point
            for (int j = 0; j < updatedInterval; j++) {
                // Move back to the previous percentage to skip over spaces and have the next letter be neighbour to last letter in color
                if (!singleChar && ' ' == string.charAt(letterCounter)) j--;

                // Assume all text is 100% opaque, also avoids having to convert from rgb to argb when using the same values for GUI
                result.add(ARGB.getARGBFromRGB(255, getIntermediate(points[i], points[i + 1], (double) ((j * 100) / updatedInterval) / 100)));
                letterCounter++;
            }
        }

        return result;
    }

    /**
     * Returns an array with the RGB integer values of the calculated intermediate color between
     * the two provided, taking into account a percentage.
     *
     * @param firstHex  first color, in the hex color coding, point A
     * @param secondHex second color, in the hex color coding, point B
     * @param percent   how far from the first color the intermediate should be <p>
     *                  e.g. 50% will be halfway between A and B
     */
    // TODO: hex validation?
    public static int[] getIntermediate(String firstHex, String secondHex, double percent) {
        return getIntermediate(Color.decode("#" + firstHex), Color.decode("#" + secondHex), percent);
    }

    /**
     * Returns an array with the RGB integer values of the calculated intermediate color between
     * the two provided, taking into account a percentage.
     *
     * @param first   first color, point A
     * @param second  second color, point B
     * @param percent how far from the first color the intermediate should be <p>
     *                e.g. 50% will be halfway between A and B
     */
    public static int[] getIntermediate(Color first, Color second, double percent) {
        double inverse_percent = 1.0 - percent;
        int redPart = (int) (second.getRed() * percent + first.getRed() * inverse_percent);
        int greenPart = (int) (second.getGreen() * percent + first.getGreen() * inverse_percent);
        int bluePart = (int) (second.getBlue() * percent + first.getBlue() * inverse_percent);

        return new int[]{redPart, greenPart, bluePart};
    }

    public static class ARGB {

        public static float[] getFloatARGBComponents(int argb) {
            float a = getAlphaFloatFromARGB(argb);
            float r = getRedFloatFromARGB(argb);
            float g = getGreenFloatFromARGB(argb);
            float b = getBlueFloatFromARGB(argb);
            return new float[]{a, r, g, b};
        }

        public static float getAlphaFloatFromARGB(int argb) {
            return getAlphaFromARGB(argb) / 255.0f;
        }

        public static float getRedFloatFromARGB(int argb) {
            return getRedFromARGB(argb) / 255.0f;
        }

        public static float getGreenFloatFromARGB(int argb) {
            return getGreenFromARGB(argb) / 255.0f;
        }

        public static float getBlueFloatFromARGB(int argb) {
            return getBlueFromARGB(argb) / 255.0f;
        }

        public static int getAlphaFromARGB(int argb) {
            return argb >>> 24;
        }

        public static int getRedFromARGB(int argb) {
            return argb >> 16 & 255;
        }

        public static int getGreenFromARGB(int argb) {
            return argb >> 8 & 255;
        }

        public static int getBlueFromARGB(int argb) {
            return argb & 255;
        }

        /**
         * Converts a hex color value with an alpha value to an integer. The string can start with a '#', but it will be removed
         * to check the validity of the provided hex string.
         *
         * @param argb #AARRGGBB
         * @return -1 (white) if the provided string isn't a valid hex color value
         */
        public static int getIntFromHexARGB(String argb) {
            return new BigInteger(Hex.checkHexARGB(argb), 16).intValue();
        }

        public static int getARGBFromRGB(int alpha, int rgb) {
            return getARGBFromRGB(alpha, RGB.getComponentsFromRGB(rgb));
        }

        /**
         * Converts an RGB array of values to ARGB.
         */
        public static int getARGBFromRGB(int alpha, int[] rgb) {
            return getARGBFromRGB(alpha, rgb[0], rgb[1], rgb[2]);
        }

        public static int getARGBFromRGB(int alpha, int red, int green, int blue) {
            return (alpha << 24) + RGB.getIntFromRGB(red, green, blue);
        }

        public static float getHueFromARGB(int argb) {
            Color c = new Color(argb, true);
            return Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null)[0];
        }

        public static float getSaturationFromARGB(int argb) {
            Color c = new Color(argb, true);
            return Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null)[1];
        }

        public static float getBrightnessFromARGB(int argb) {
            Color c = new Color(argb, true);
            return Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null)[2];
        }

        public static int convertARGBToTransparent(int argb) {
            return convertARGBWithAlpha(0, argb);
        }

        public static int convertARGBToOpaque(int argb) {
            return convertARGBWithAlpha(255, argb);
        }

        public static int convertARGBWithAlpha(int newAlpha, int argb) {
            Color c = new Color(argb, true);
            Color d = new Color(c.getRed(), c.getGreen(), c.getBlue(), newAlpha);
            return d.getRGB();
        }
    }

    public static class RGB {

        public static int[] getComponentsFromRGB(int rgb) {
            int red = 0xFF & (rgb >> 16);
            int green = 0xFF & (rgb >> 8);
            int blue = 0xFF & (rgb >> 0);

            return new int[]{red, green, blue};
        }

        /**
         * Converts a hex color value to an integer.
         *
         * @param rgb #RRGGBB
         */
        public static int getIntFromHexRGB(String rgb) {
            return Integer.parseInt(Hex.checkHexRGB(rgb), 16);
        }

        public static int getIntFromRGB(int[] rgb) {
            return getIntFromRGB(rgb[0], rgb[1], rgb[2]);
        }

        public static int getIntFromRGB(int r, int g, int b) {
            return (r << 16) + (g << 8) + b;
        }

        public static int getRGBFromARGB(int argb) {
            int[] comps = getComponentsFromRGB(argb);
            return getIntFromRGB(comps);
        }
    }

    public static class Hex {

        public static String checkHexARGB(String hex) {
            if (isARGBHex(hex)) return hex;
            if (isRGBHex(hex)) return "FF" + hex;
            return "FFFFFFFF";
        }

        public static String checkHexRGB(String hex) {
            if (isRGBHex(hex)) return hex;
            return "FFFFFFFF";
        }

        /**
         * The string can start with a '#', but it will be removed to check the validity of the provided hex string.
         *
         * @param argb #AARRGGBB
         */
        public static boolean isARGBHex(String argb) {
            String noHash = argb.replaceAll("#", "");
            return argbHex.matcher(noHash).matches();
        }

        public static boolean isRGBHex(String rgb) {
            String noHash = rgb.replaceAll("#", "");
            return rgbHex.matcher(noHash).matches();
        }

        public static String getHexFromARGB(int argb) {
            String rgb = getHexFromRGB(RGB.getRGBFromARGB(argb));
            return getAlphaFromARGB(argb) + rgb;
        }

        public static String getAlphaFromARGB(int argb) {
            return getAlphaFromFloat(ARGB.getAlphaFloatFromARGB(argb));
        }

        /**
         * @param alpha (0.0 - 1.0)
         */
        public static String getAlphaFromFloat(float alpha) {
            String a = Integer.toHexString((int) (alpha * 255));
            if (a.length() == 1) a += "0";
            return a;
        }

        public static String getHexFromRGB(Integer[] rgb) {
            return getHexFromRGB(rgb[0], rgb[1], rgb[2]);
        }

        public static String getHexFromRGB(int rgb) {
            int r = (rgb >> 16) & 0xFF;
            int g = (rgb >> 8) & 0xFF;
            int b = (rgb >> 0) & 0xFF;

            return getHexFromRGB(r, g, b);
        }

        public static String getHexFromRGB(int r, int g, int b) {
            return String.format("%02x%02x%02x", r, g, b);
        }
    }
}
