package com.pilasvacias.yaba.core.effects;

import android.graphics.Shader;

import com.pilasvacias.yaba.core.widget.WTextView;


public class LinearGradient extends Gradient {

    private float xStart;
    private float yStart;
    private float xEnd;
    private float yEnd;
    private float angle;

    public LinearGradient(WTextView gradientTextView) {
        super(gradientTextView);
    }

    public static LinearGradient create(WTextView gradientTextView) {
        return new LinearGradient(gradientTextView);
    }

    public LinearGradient with(int startColor, int endColor) {
        this.setStartColor(startColor);
        this.setEndColor(endColor);
        return this;
    }

    public LinearGradient with(int... colors) {
        this.setColors(colors);
        return this;
    }

    public LinearGradient xStart(float x) {
        this.setxStart(x);
        return this;
    }

    public LinearGradient yStart(float y) {
        this.setyStart(y);
        return this;
    }

    public LinearGradient xEnd(float x) {
        this.setxEnd(x);
        return this;
    }

    public LinearGradient yEnd(float y) {
        this.setyEnd(y);
        return this;
    }

    private double bound(double min, double val, double max) {
        return Math.max(min, Math.min(max, val));
    }

    public LinearGradient andTileMode(Shader.TileMode tileMode) {
        this.setTileMode(tileMode);
        return this;
    }

    @Override
    protected Shader.TileMode getTileMode() {
        if (null == super.getTileMode()) {
            this.setTileMode(Shader.TileMode.CLAMP);
        }
        return tileMode;
    }

    @Override
    public Shader getShader() {
        return new android.graphics.LinearGradient(getxStart(),
                getyStart(), getxEnd(), getyEnd(), getColors(), new float[]{0, 1},
                getTileMode());
    }

    private float getxStart() {
        return xStart;
    }

    private void setxStart(float xStart) {
        this.xStart = xStart;
    }

    private float getyStart() {
        return yStart;
    }

    private void setyStart(float yStart) {
        this.yStart = yStart;
    }

    private float getxEnd() {
        return xEnd;
    }

    private void setxEnd(float xEnd) {
        this.xEnd = xEnd;
    }

    private float getyEnd() {
        if (yEnd == 0) {
            this.setyEnd(getTextView().getHeight());
        }
        return yEnd;
    }

    private void setyEnd(float yEnd) {
        this.yEnd = yEnd;
    }

}