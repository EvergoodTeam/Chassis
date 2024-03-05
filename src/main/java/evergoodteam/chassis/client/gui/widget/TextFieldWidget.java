package evergoodteam.chassis.client.gui.widget;

import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class TextFieldWidget extends WidgetBase {

    private String text = "";
    private int maxLength = 32;
    private int focusedTicks;
    private boolean drawsBackground = true;
    private boolean focusUnlocked = true;
    private boolean editable = true;
    private boolean selecting;
    private int firstCharacterIndex;
    private int selectionStart;
    private int selectionEnd;
    private int editableColor = 0xE0E0E0;
    private int uneditableColor = 0x707070;
    @Nullable
    private String suggestion;
    @Nullable
    private Consumer<String> changedListener;
    private Predicate<String> textPredicate = Objects::nonNull;
    private BiFunction<String, Integer, OrderedText> renderTextProvider = (string, firstCharacterIndex) -> OrderedText.styledForwardsVisitedString(string, Style.EMPTY);
    @Nullable
    private Text placeholder;

    public TextFieldWidget(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

    public void setChangedListener(Consumer<String> changedListener) {
        this.changedListener = changedListener;
    }

    public void setRenderTextProvider(BiFunction<String, Integer, OrderedText> renderTextProvider) {
        this.renderTextProvider = renderTextProvider;
    }

    public void tick() {
        ++this.focusedTicks;
    }

    public void setText(String text) {
        if (!this.textPredicate.test(text)) {
            return;
        }
        this.text = text.length() > this.maxLength ? text.substring(0, this.maxLength) : text;
        this.setCursorToEnd();
        this.setSelectionEnd(this.selectionStart);
        this.onChanged(text);
    }

    public void setTextSilently(String text){
        if (!this.textPredicate.test(text)) {
            return;
        }
        this.text = text.length() > this.maxLength ? text.substring(0, this.maxLength) : text;
    }

    public String getText() {
        return this.text;
    }

    public String getSelectedText() {
        int i = Math.min(this.selectionStart, this.selectionEnd);
        int j = Math.max(this.selectionStart, this.selectionEnd);
        return this.text.substring(i, j);
    }

    public void setTextPredicate(Predicate<String> textPredicate) {
        this.textPredicate = textPredicate;
    }

    public void write(String text) {
        String string2;
        String string;
        int l;
        int i = Math.min(this.selectionStart, this.selectionEnd);
        int j = Math.max(this.selectionStart, this.selectionEnd);
        int k = this.maxLength - this.text.length() - (i - j);
        if (k < (l = (string = SharedConstants.stripInvalidChars(text)).length())) {
            string = string.substring(0, k);
            l = k;
        }
        if (!this.textPredicate.test(string2 = new StringBuilder(this.text).replace(i, j, string).toString())) {
            return;
        }
        this.text = string2;
        this.setSelectionStart(i + l);
        this.setSelectionEnd(this.selectionStart);
        this.onChanged(this.text);
    }

    private void onChanged(String newText) {
        if (this.changedListener != null) {
            this.changedListener.accept(newText);
        }
    }

    private void erase(int offset) {
        if (Screen.hasControlDown()) {
            this.eraseWords(offset);
        } else {
            this.eraseCharacters(offset);
        }
    }

    public void eraseWords(int wordOffset) {
        if (this.text.isEmpty()) {
            return;
        }
        if (this.selectionEnd != this.selectionStart) {
            this.write("");
            return;
        }
        this.eraseCharacters(this.getWordSkipPosition(wordOffset) - this.selectionStart);
    }

    public void eraseCharacters(int characterOffset) {
        int k;
        if (this.text.isEmpty()) {
            return;
        }
        if (this.selectionEnd != this.selectionStart) {
            this.write("");
            return;
        }
        int i = this.getCursorPosWithOffset(characterOffset);
        int j = Math.min(i, this.selectionStart);
        if (j == (k = Math.max(i, this.selectionStart))) {
            return;
        }
        String string = new StringBuilder(this.text).delete(j, k).toString();
        if (!this.textPredicate.test(string)) {
            return;
        }
        this.text = string;
        this.setCursor(j);
    }

    public int getWordSkipPosition(int wordOffset) {
        return this.getWordSkipPosition(wordOffset, this.getCursor());
    }

    private int getWordSkipPosition(int wordOffset, int cursorPosition) {
        return this.getWordSkipPosition(wordOffset, cursorPosition, true);
    }

    private int getWordSkipPosition(int wordOffset, int cursorPosition, boolean skipOverSpaces) {
        int i = cursorPosition;
        boolean bl = wordOffset < 0;
        int j = Math.abs(wordOffset);
        for (int k = 0; k < j; ++k) {
            if (bl) {
                while (skipOverSpaces && i > 0 && this.text.charAt(i - 1) == ' ') {
                    --i;
                }
                while (i > 0 && this.text.charAt(i - 1) != ' ') {
                    --i;
                }
                continue;
            }
            int l = this.text.length();
            if ((i = this.text.indexOf(32, i)) == -1) {
                i = l;
                continue;
            }
            while (skipOverSpaces && i < l && this.text.charAt(i) == ' ') {
                ++i;
            }
        }
        return i;
    }

    public void moveCursor(int offset) {
        this.setCursor(this.getCursorPosWithOffset(offset));
    }

    private int getCursorPosWithOffset(int offset) {
        return Util.moveCursor(this.text, this.selectionStart, offset);
    }

    public void setCursor(int cursor) {
        this.setSelectionStart(cursor);
        if (!this.selecting) {
            this.setSelectionEnd(this.selectionStart);
        }
        this.onChanged(this.text);
    }

    public void setSelectionStart(int cursor) {
        this.selectionStart = MathHelper.clamp(cursor, 0, this.text.length());
    }

    public void setCursorToStart() {
        this.setCursor(0);
    }

    public void setCursorToEnd() {
        this.setCursor(this.text.length());
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.isActive()) {
            return false;
        }
        this.selecting = Screen.hasShiftDown();
        if (Screen.isSelectAll(keyCode)) {
            this.setCursorToEnd();
            this.setSelectionEnd(0);
            return true;
        }
        if (Screen.isCopy(keyCode)) {
            MinecraftClient.getInstance().keyboard.setClipboard(this.getSelectedText());
            return true;
        }
        if (Screen.isPaste(keyCode)) {
            if (this.editable) {
                this.write(MinecraftClient.getInstance().keyboard.getClipboard());
            }
            return true;
        }
        if (Screen.isCut(keyCode)) {
            MinecraftClient.getInstance().keyboard.setClipboard(this.getSelectedText());
            if (this.editable) {
                this.write("");
            }
            return true;
        }
        switch (keyCode) {
            case 263: {
                if (Screen.hasControlDown()) {
                    this.setCursor(this.getWordSkipPosition(-1));
                } else {
                    this.moveCursor(-1);
                }
                return true;
            }
            case 262: {
                if (Screen.hasControlDown()) {
                    this.setCursor(this.getWordSkipPosition(1));
                } else {
                    this.moveCursor(1);
                }
                return true;
            }
            case 259: {
                if (this.editable) {
                    this.selecting = false;
                    this.erase(-1);
                    this.selecting = Screen.hasShiftDown();
                }
                return true;
            }
            case 261: {
                if (this.editable) {
                    this.selecting = false;
                    this.erase(1);
                    this.selecting = Screen.hasShiftDown();
                }
                return true;
            }
            case 268: {
                this.setCursorToStart();
                return true;
            }
            case 269: {
                this.setCursorToEnd();
                return true;
            }
        }
        return false;
    }

    public boolean isActive() {
        return this.enabled && this.isFocused() && this.isEditable();
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (!this.isActive()) {
            return false;
        }
        if (SharedConstants.isValidChar(chr)) {
            if (this.editable) {
                this.write(Character.toString(chr));
            }
            return true;
        }
        return false;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);

        int i = MathHelper.floor(mouseX) - this.getX();
        if (this.drawsBackground) {
            i -= 4;
        }
        String string = this.textRenderer.trimToWidth(this.text.substring(this.firstCharacterIndex), this.getInnerWidth());
        this.setCursor(this.textRenderer.trimToWidth(string, i).length() + this.firstCharacterIndex);
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
    }

    private static final String HORIZONTAL_CURSOR = "_";

    @Override
    public void renderButton(DrawContext context, int x, int y, int width, int height) {
        int i;
        if (!this.enabled) {
            return;
        }
        if (this.drawsBackground()) {
            i = this.isFocused() ? -1 : -6250336;
            this.drawRectOutline(context, this.getX(), this.getY(), this.width, this.height, i);
            //context.fill(this.getX() - 1, this.getY() - 1, this.getX() + this.width + 1, this.getY() + this.height + 1, i);
            //context.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 1660944384);
        }
        i = this.editable ? this.editableColor : this.uneditableColor;
        int j = this.selectionStart - this.firstCharacterIndex;
        int k = this.selectionEnd - this.firstCharacterIndex;
        String string = this.textRenderer.trimToWidth(this.text.substring(this.firstCharacterIndex), this.getInnerWidth());
        boolean bl = j >= 0 && j <= string.length();
        boolean bl2 = this.isFocused() && this.focusedTicks / 6 % 2 == 0 && bl;
        int l = this.drawsBackground ? this.getX() + 4 : this.getX();
        int m = this.drawsBackground ? this.getY() + (this.height - 8) / 2 : this.getY();
        int n = l;
        if (k > string.length()) {
            k = string.length();
        }
        if (!string.isEmpty()) {
            String string2 = bl ? string.substring(0, j) : string;
            n = context.drawTextWithShadow(this.textRenderer, this.renderTextProvider.apply(string2, this.firstCharacterIndex), n, m, i);
        }
        boolean bl3 = this.selectionStart < this.text.length() || this.text.length() >= this.getMaxLength();
        int o = n;
        if (!bl) {
            o = j > 0 ? l + this.width : l;
        } else if (bl3) {
            --o;
            --n;
        }
        if (!string.isEmpty() && bl && j < string.length()) {
            context.drawTextWithShadow(this.textRenderer, this.renderTextProvider.apply(string.substring(j), this.selectionStart), n, m, i);
        }
        if (this.placeholder != null && string.isEmpty() && !this.isFocused()) {
            context.drawTextWithShadow(this.textRenderer, this.placeholder, n, m, i);
        }
        if (!bl3 && this.suggestion != null) {
            context.drawTextWithShadow(this.textRenderer, this.suggestion, o - 1, m, -8355712);
        }
        if (bl2) {
            if (bl3) {
                context.fill(RenderLayer.getGuiOverlay(), o, m - 1, o + 1, m + 1 + this.textRenderer.fontHeight, -3092272);
            } else {
                context.drawTextWithShadow(this.textRenderer, HORIZONTAL_CURSOR, o, m, i);
            }
        }
        if (k != j) {
            int p = l + this.textRenderer.getWidth(string.substring(0, k));
            this.drawSelectionHighlight(context, o, m - 1, p - 1, m + 1 + this.textRenderer.fontHeight);
        }
    }

    private void drawSelectionHighlight(DrawContext context, int x1, int y1, int x2, int y2) {
        int i;
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }
        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;
        }
        if (x2 > this.getX() + this.width) {
            x2 = this.getX() + this.width;
        }
        if (x1 > this.getX() + this.width) {
            x1 = this.getX() + this.width;
        }
        context.fill(RenderLayer.getGuiTextHighlight(), x1, y1, x2, y2, -16776961);
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        if (this.text.length() > maxLength) {
            this.text = this.text.substring(0, maxLength);
            this.onChanged(this.text);
        }
    }

    private int getMaxLength() {
        return this.maxLength;
    }

    public int getCursor() {
        return this.selectionStart;
    }

    private boolean drawsBackground() {
        return this.drawsBackground;
    }

    public void setDrawsBackground(boolean drawsBackground) {
        this.drawsBackground = drawsBackground;
    }

    public void setEditableColor(int editableColor) {
        this.editableColor = editableColor;
    }

    public void setUneditableColor(int uneditableColor) {
        this.uneditableColor = uneditableColor;
    }

    @Override
    public void setFocused(boolean focused) {
        if (!this.focusUnlocked && !focused) {
            return;
        }
        super.setFocused(focused);
        if (focused) {
            this.focusedTicks = 0;
        }
    }

    private boolean isEditable() {
        return this.editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public int getInnerWidth() {
        return this.drawsBackground() ? this.width - 8 : this.width;
    }

    public void setSelectionEnd(int index) {
        int i = this.text.length();
        this.selectionEnd = MathHelper.clamp(index, 0, i);
        if (this.textRenderer != null) {
            if (this.firstCharacterIndex > i) {
                this.firstCharacterIndex = i;
            }
            int j = this.getInnerWidth();
            String string = this.textRenderer.trimToWidth(this.text.substring(this.firstCharacterIndex), j);
            int k = string.length() + this.firstCharacterIndex;
            if (this.selectionEnd == this.firstCharacterIndex) {
                this.firstCharacterIndex -= this.textRenderer.trimToWidth(this.text, j, true).length();
            }
            if (this.selectionEnd > k) {
                this.firstCharacterIndex += this.selectionEnd - k;
            } else if (this.selectionEnd <= this.firstCharacterIndex) {
                this.firstCharacterIndex -= this.firstCharacterIndex - this.selectionEnd;
            }
            this.firstCharacterIndex = MathHelper.clamp(this.firstCharacterIndex, 0, i);
        }
    }

    public void setFocusUnlocked(boolean focusUnlocked) {
        this.focusUnlocked = focusUnlocked;
    }

    public void setSuggestion(@Nullable String suggestion) {
        this.suggestion = suggestion;
    }

    public int getCharacterX(int index) {
        if (index > this.text.length()) {
            return this.getX();
        }
        return this.getX() + this.textRenderer.getWidth(this.text.substring(0, index));
    }


    public void setPlaceholder(Text placeholder) {
        this.placeholder = placeholder;
    }
}
