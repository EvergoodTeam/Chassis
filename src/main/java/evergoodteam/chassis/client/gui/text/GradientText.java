package evergoodteam.chassis.client.gui.text;

import com.google.common.collect.Lists;
import evergoodteam.chassis.util.gui.ColorUtils;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * Modified version of {@link MutableText} to be used with {@link GradientTextRenderer}
 */
public class GradientText implements Text {

    private final TextContent content;
    private final List<Text> siblings;
    private Style style;
    private OrderedText ordered = OrderedText.EMPTY;
    @Nullable
    private Language language;
    private List<Integer> points;
    private int delay = -1;
    private int defaultDelay = -1;

    public GradientText(TextContent content, List<Text> siblings, Style style) {
        this.content = content;
        this.siblings = siblings;
        this.style = style;
    }

    public static GradientText translatable(String key) {
        return GradientText.of(new TranslatableTextContent(key, null, TranslatableTextContent.EMPTY_ARGUMENTS));
    }

    public static GradientText translatable(String key, Object... args) {
        return GradientText.of(new TranslatableTextContent(key, null, args));
    }

    public static GradientText literal(String string) {
        return GradientText.of(new LiteralTextContent(string));
    }

    /**
     * Creates a {@link GradientText} copy of the provided {@link Text} and attempts to copy the color points if
     * it is an instance of GradientText
     */
    public static GradientText copyOf(Text text) {
        return copyOf(text, text.getString());
    }

    /**
     * Creates a {@link GradientText} copy of the provided {@link Text} and attempts to copy the color points if
     * it is an instance of GradientText
     *
     * @param string string to be used by the copy, e.g. a truncated version of the text's string
     */
    public static GradientText copyOf(Text text, String string) {
        GradientText copy = GradientText.literal(string).fillStyle(text.getStyle());
        if (text instanceof GradientText) {
            if(((GradientText) text).getPoints() != null ) copy.setColorPoints(((GradientText) text).getPoints());
        }
        return copy;
    }

    public static GradientText of(TextContent content) {
        return new GradientText(content, Lists.newArrayList(), Style.EMPTY);
    }

    public GradientText setColorPoints(@NotNull List<Integer> points) {
        this.points = points;
        return this;
    }

    /**
     * Gives the category's title a gradient, or just a color if only one gradient point is specified
     *
     * @param hexColors hex colors to be used as transition points
     */
    public GradientText setColorPoints(String... hexColors) {
        return setColorPoints(0, hexColors);
    }

    /**
     * Gives the category's title a gradient, or just a color if only one gradient point is specified
     *
     * @param extension length to add to the text's string when generating the gradients
     * @param hexColors hex colors to be used as transition points
     */
    public GradientText setColorPoints(int extension, String... hexColors) {
        this.points = ColorUtils.getGradientPoints(this.getString() + "x".repeat(extension), hexColors);
        return this;
    }

    /**
     * Scrolls through the color points
     */
    public void scroll() {
        if (points != null && delay >= 0) {
            if (delay == 0) {
                Collections.rotate(this.points, 1);
                delay = defaultDelay;
            } else delay--;
        }
    }

    /**
     * Updates the delay to scroll the colors of the category's title
     *
     * @param delay anything below 0 will prevent the color scrolling
     */
    public GradientText setScrollDelay(int delay) {
        this.delay = delay;
        this.defaultDelay = delay;
        return this;
    }

    public GradientText setFormatting(Formatting... formattings) {
        return this.setStyle(this.getStyle().withFormatting(formattings));
    }

    public GradientText fillStyle(Style styleOverride) {
        return this.setStyle(styleOverride.withParent(this.getStyle()));
    }

    public GradientText setStyle(Style style) {
        this.style = style;
        return this;
    }

    @Override
    public Style getStyle() {
        return this.style;
    }

    public List<Integer> getPoints() {
        return this.points;
    }

    @Override
    public TextContent getContent() {
        return this.content;
    }

    @Override
    public List<Text> getSiblings() {
        return this.siblings;
    }

    @Override
    public OrderedText asOrderedText() {
        Language language = Language.getInstance();
        if (this.language != language) {
            this.ordered = language.reorder(this);
            this.language = language;
        }
        return this.ordered;
    }
}
