package evergoodteam.chassis.util;

import org.apache.commons.validator.routines.UrlValidator;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;

public class UrlUtils {

    private static String[] schemes = {"http", "https"};
    private static UrlValidator validator = new UrlValidator(schemes);

    /**
     * Checks if the provided URL is an image
     */
    public static boolean isImage(String url) {
        if (isValid(url)) {
            try {
                if (ImageIO.read(new URL(url)) != null) return true;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    /**
     * Checks if the provided URL is valid
     */
    public static boolean isValid(String url) {
        return validator.isValid(url);
    }
}
