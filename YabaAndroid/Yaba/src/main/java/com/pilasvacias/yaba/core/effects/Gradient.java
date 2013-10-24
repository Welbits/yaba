package com.pilasvacias.yaba.core.effects;

import android.graphics.Point;
import android.graphics.Shader;
import android.view.View;

import com.pilasvacias.yaba.core.widget.WTextView;

/**
 * Created by pablo on 26/07/13.
 */
public abstract class Gradient {
    protected int[] colors;
    protected int startColor, endColor;
    protected Shader.TileMode tileMode;
    protected WTextView textView;

    protected Gradient(WTextView gradientTextView) {
        this.setTextView(gradientTextView);
    }

    public int[] getColors() {
        if (null == colors) {
            this.setColors(new int[]{getStartColor(), getEndColor()});
        }
        return colors;
    }

    protected void setColors(int[] colors) {
        this.colors = colors;
    }

    public abstract Shader getShader();

    protected int getStartColor() {
        return startColor;
    }

    protected void setStartColor(int startColor) {
        this.startColor = startColor;
    }

    protected int getEndColor() {
        return endColor;
    }

    protected void setEndColor(int endColor) {
        this.endColor = endColor;
    }

    protected Shader.TileMode getTileMode() {
        return tileMode;
    }

    protected void setTileMode(Shader.TileMode tileMode) {
        this.tileMode = tileMode;
    }

    protected WTextView getTextView() {
        return textView;
    }

    protected void setTextView(WTextView textView) {
        this.textView = textView;
    }

    protected static int getViewX(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location[0];
    }

    protected static int getViewY(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location[1];
    }

    protected static Point getViewCenter(View view) {
        Point point = new Point(0, 0);

        int xCenterView = view.getWidth() / 2;
        int yCenterView = view.getHeight() / 2;
        int xCenterOnScreen = view.getLeft() + xCenterView;
        int yCenterOnScreen = view.getTop() + yCenterView;

        point.x = xCenterOnScreen;
        point.y = yCenterOnScreen;

        return point;
    }

}