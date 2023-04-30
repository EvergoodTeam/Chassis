package evergoodteam.chassis.util.gui;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ColorUtils {

    public static List<Integer> getGradientPoints(int length, @NotNull String... points){
        return getGradientPoints("x".repeat(length), points);
    }

    /**
     * Returns a list of decimal color codes: each represents a color obtained from calculating the various intermediates
     * between the specified gradient points. The result is a full gradient that is supposed to cover the provided
     * string's length
     *
     * @param string string to which the gradient can be applied
     * @param points hex colors to be used as transition points <p>
     *               e.g. from red to yellow
     */
    public static List<Integer> getGradientPoints(@NotNull String string, @NotNull String... points) {

        if (points.length == 0) return List.of(16777215);
        else if (points.length == 1) return List.of(getDecimalFromHex(points[0]));

        List<Integer> colorPoints = new ArrayList<>();
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
            // TODO: Improve remainder distribution
            int updatedInterval = interval;
            if (remainder > 0) {
                updatedInterval++;
                remainder--;
            }

            for (int j = 0; j < updatedInterval; j++) {
                if (!singleChar && ' ' == string.charAt(letterCounter)) j--;
                colorPoints.add(getDecimalFromRGB(getIntermediate(points[i], points[i + 1], (double) ((j * 100) / updatedInterval) / 100)));
                letterCounter++;
            }
        }

        return colorPoints;
    }

    /**
     * Returns an array with the RGB integer values of the calculated intermediate color between
     * the two provided, taking into account a percentage
     *
     * @param firstHex  first color in the hex color coding, point A
     * @param secondHex second color in the hex color coding, point B
     * @param percent   how far from the first color the intermediate should be <p>
     *                  e.g. 50% will be halfway between A and B
     */
    public static Integer[] getIntermediate(String firstHex, String secondHex, Double percent) {
        return getIntermediate(Color.decode("#" + firstHex), Color.decode("#" + secondHex), percent);
    }

    /**
     * Returns an array with the RGB integer values of the calculated intermediate color between
     * the two provided, taking into account a percentage
     *
     * @param first   first color, point A
     * @param second  second color, point B
     * @param percent how far from the first color the intermediate should be <p>
     *                e.g. 50% will be halfway between A and B
     */
    public static Integer[] getIntermediate(Color first, Color second, Double percent) {
        double inverse_percent = 1.0 - percent;
        int redPart = (int) (second.getRed() * percent + first.getRed() * inverse_percent);
        int greenPart = (int) (second.getGreen() * percent + first.getGreen() * inverse_percent);
        int bluePart = (int) (second.getBlue() * percent + first.getBlue() * inverse_percent);

        return new Integer[]{redPart, greenPart, bluePart};
    }

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
}
