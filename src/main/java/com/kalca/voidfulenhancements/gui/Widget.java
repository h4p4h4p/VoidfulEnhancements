package com.kalca.voidfulenhancements.gui;

public abstract class Widget {

    protected float x;
    protected float y;
    protected float width;
    protected float height = 14;

    public void setPosition(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public float getHeight() {
        return height;
    }

    public boolean contains(float mx, float my) {
        return mx >= x && mx <= x + width && my >= y && my <= y + height;
    }

    public abstract void draw(float mx, float my);

    public boolean onClick(float mx, float my, int button) {
        return false;
    }

    public void onDrag(float mx, float my) {
    }

    public float getPreferredWidth() {
        return 100;
    }
}