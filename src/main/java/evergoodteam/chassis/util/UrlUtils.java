package evergoodteam.chassis.util;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class UrlUtils {

    /**
     * Checks if the provided URL is an image
     */
    public static boolean isImage(String url) {
        if (isValid(url)) {
            try {
                return ImageIO.read(new URL(url)) != null;
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
        try {
            new URL(url).toURI();
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }

        return true;
    }
}
