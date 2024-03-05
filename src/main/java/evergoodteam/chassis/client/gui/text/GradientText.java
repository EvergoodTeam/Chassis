package evergoodteam.chassis.client.gui.text;

import evergoodteam.chassis.util.gui.ColorUtils;
import net.minecraft.text.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Modified version of {@link MutableText} to be used with {@link GradientTextRenderer}.
 */
public class GradientText extends MutableText {

    @Nullable
    private List<Integer> points;
    private int delay = -1;
    private int defaultDelay = -1;

    /**
     * Creates a {@link GradientText} copy of the provided {@link Text} and attempts to copy the color points if
     * it is an instance of GradientText.
     */
    public static GradientText copyOf(Text text) {
        GradientText copy = new GradientText(text.getContent(), new ArrayList<>(text.getSiblings()), text.getStyle());
        if (text instanceof GradientText) {
            if (((GradientText) text).getPoints() != null) copy.setColorPoints(((GradientText) text).getPoints());
        }
        return copy;
    }

    /**
     * Creates a {@link GradientText} copy of the provided {@link Text} and attempts to copy the color points if
     * it is an instance of GradientText.
     *
     * @param string string to be used by the copy, e.g. a truncated version of the text's string
     */
    public static GradientText copyOf(Text text, String string) {
        GradientText copy = new GradientText(new LiteralTextContent(string), new ArrayList<>(text.getSiblings()), text.getStyle());
        if (text instanceof GradientText) {
            if (((GradientText) text).getPoints() != null) copy.setColorPoints(((GradientText) text).getPoints());
        }
        return copy;
    }

    public GradientText(TextContent content, List<Text> siblings, Style style) {
        super(content, siblings, style);
    }

    /**
     * Sets the text's gradient points with the specified hex colors:
     * <ul>
     *      <li>multiple hex values represent multiple transition points, all evenly spaced out on the text's string length</li>
     *      <li>a single hex value will simply override the text's color</li>
     *      <li>if no hex values are provided, white will be used</li>
     * </ul>
     *
     * <p>The space between points is determined by the length of the text's string, see {@link #setColorPoints(int, String...)}
     * to have smoother and longer gradient transitions.</p>
     *
     * @param hexColors hex colors to be used as transition points
     * @see #setColorPoints(int, String...)
     */
    public GradientText setColorPoints(String... hexColors) {
        return setColorPoints(0, hexColors);
    }

    /**
     * Sets the text's gradient points with the specified hex colors:
     * <ul>
     *      <li>multiple hex values represent multiple transition points, all evenly spaced out on the text's string length</li>
     *      <li>a single hex value will simply override the text's color</li>
     *      <li>if no hex values are provided, white will be used</li>
     * </ul>
     *
     * @param extension length to extend the overall gradient by: a higher amount will result in smoother and longer gradient transitions
     * @param hexColors hex colors to be used as transition points
     */
    public GradientText setColorPoints(int extension, String... hexColors) {
        return setColorPoints(ColorUtils.getGradientPoints(this.getString() + "x".repeat(extension), hexColors));
    }

    private GradientText setColorPoints(@NotNull List<Integer> points) {
        this.points = points;
        return this;
    }

    /**
     * Updates the delay used to scroll forwards the gradient.
     *
     * @param delay < 0 will prevent the gradient from scrolling
     */
    public GradientText setScrollDelay(int delay) {
        this.delay = delay;
        this.defaultDelay = delay;
        return this;
    }

    public List<Integer> getPoints() {
        return this.points;
    }

    /**
     * Scrolls through the color points, to be called on every render call.
     */
    public void scroll() {
        if (points != null && delay >= 0) {
            if (delay == 0) {
                Collections.rotate(this.points, 1);
                delay = defaultDelay;
            } else delay--;
        }
    }
}
