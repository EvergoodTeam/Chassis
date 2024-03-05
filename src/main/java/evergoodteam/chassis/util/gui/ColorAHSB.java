package evergoodteam.chassis.util.gui;

import java.awt.*;

public class ColorAHSB {

    private int value = -1;
    private float alpha;
    private float hue;
    private float saturation;
    private float brightness;
    private ColorUpdateCallback callback;

    public ColorAHSB(int value) {
        setValue(value);
        updateAll();
    }

    public void setCallback(ColorUpdateCallback updateCallback){
        this.callback = updateCallback;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getHexValue() {
        return ColorUtils.Hex.getHexFromARGB(this.value);
    }

    public int getValue(){
        return this.value;
    }

    public void updateAll() {
        updateAlpha();
        updateHue();
        updateSB();
    }

    /**
     * Calls alpha callback
     */
    public void updateAlpha() {
        setAlpha(this.value);
        if (callback != null) callback.onAlphaUpdate(this.alpha);
    }

    private void setAlpha(int value) {
        this.alpha = ColorUtils.ARGB.getAlphaFloatFromARGB(value);
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
        recalculateValue();
    }

    public float getAlpha(){
        return this.alpha;
    }

    /**
     * Calls hue callback
     */
    public void updateHue() {
        setHue(this.value);
        if (callback != null) callback.onHueUpdate(this.hue);
    }

    private void setHue(int value) {
        this.hue = ColorUtils.ARGB.getHueFromARGB(value);
    }

    public void setHue(float hue) {
        this.hue = hue;
        recalculateValue();
    }

    public float getHue(){
        return this.hue;
    }

    /**
     * Calls saturation/brightness callback
     */
    public void updateSB() {
        setSB(this.value);
        if (callback != null) callback.onSBUpdate(this.saturation, this.brightness);
    }

    private void setSB(int value) {
        this.saturation = ColorUtils.ARGB.getSaturationFromARGB(value);
        this.brightness = ColorUtils.ARGB.getBrightnessFromARGB(value);
    }

    public void setSB(float saturation, float brightness) {
        this.saturation = saturation;
        this.brightness = brightness;
        recalculateValue();
    }

    public float getSaturation(){
        return this.saturation;
    }

    public float getBrightness(){
        return this.brightness;
    }

    private void recalculateValue() {
        Color c = Color.getHSBColor(this.hue, this.saturation, this.brightness);
        setValue(new Color(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, this.alpha).getRGB());
        if (callback != null) callback.onRecalculate(this.value);
    }

    public interface ColorUpdateCallback {

        default void onAlphaUpdate(float alpha) {
        }

        default void onHueUpdate(float hue) {
        }

        default void onSBUpdate(float saturation, float brightness) {
        }

        default void onRecalculate(int value) {
        }
    }
}
