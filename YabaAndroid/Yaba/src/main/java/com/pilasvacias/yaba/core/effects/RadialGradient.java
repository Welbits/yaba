package com.pilasvacias.yaba.core.effects;

import android.graphics.Shader;

import com.pilasvacias.yaba.core.widget.WTextView;

public class RadialGradient extends Gradient {

    private Float x, y, radius;

    private RadialGradient(WTextView gradientTextView) {
        super(gradientTextView);
    }

    private static RadialGradient create(WTextView gradientTextView) {
        return new RadialGradient(gradientTextView);
    }

    public RadialGradient with(int startColor, int endColor) {
        this.setStartColor(startColor);
        this.setEndColor(endColor);
        return this;
    }

    public RadialGradient with(int... colors) {
        this.setColors(colors);
        return this;
    }

    public RadialGradient x(float x) {
        this.setX(x);
        return this;
    }

    public RadialGradient y(float y) {
        this.setY(y);
        return this;
    }

    public RadialGradient tileMode(Shader.TileMode tileMode) {
        this.setTileMode(tileMode);
        return this;
    }

    public RadialGradient andRadius(float radius) {
        this.setRadius(radius);
        return this;
    }

    @Override
    protected Shader.TileMode getTileMode() {
        if (null == super.getTileMode()) {
            this.setTileMode(Shader.TileMode.MIRROR);
        }
        return tileMode;
    }

    @Override
    public Shader getShader() {
        return new android.graphics.RadialGradient(getX(), getY(),
                getRadius(), getColors(), null, getTileMode());
    }

    private float getX() {
        if (null == x) {
            int xCenter = getViewCenter(getTextView()).x;
            this.setX(xCenter);
        }
        return x;
    }

    private void setX(float x) {
        this.x = x;
    }

    private float getY() {
        if (null == y) {
            int yCenter = getViewCenter(getTextView()).y;
            this.setY(yCenter);
        }
        return y;
    }

    private void setY(float y) {
        this.y = y;
    }

    private float getRadius() {
        if (null == radius) {
            this.setRadius(getTextView().getHeight() / 2.0f);
        }
        return radius;
    }

    private void setRadius(float radius) {
        this.radius = radius;
    }

}
 

