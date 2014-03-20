package net.wohlfart.photon.hud.txt;

/*
 * dimension info for a single character
 */
public class CharInfo {
    protected final float x;
    protected final float y;
    protected final float w;
    protected final float h;
    protected final float g;

    CharInfo(float x, float y, float w, float h, float g) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.g = g;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return w;
    }

    public float getHeight() {
        return h;
    }

    public float getG() {
        return g;
    }

}
