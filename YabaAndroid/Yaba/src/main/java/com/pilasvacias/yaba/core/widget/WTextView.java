package com.pilasvacias.yaba.core.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewPropertyAnimator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.core.effects.Gradient;
import com.pilasvacias.yaba.core.effects.LinearGradient;
import com.pilasvacias.yaba.util.FontUtils;

//import com.welvi.core.R;

/**
 * Created by Pablo on 6/07/13.
 * <p/>
 * Custom text that adds special attributes. @see #WTextView
 */
public class WTextView extends TextView {


    public static final int CAPITALIZE_WORDS = 0x3;
    public static final int CAPITALIZE_SENTENCES = 0x2;
    public static final int CAPITALIZE_ALL = 0x1;
    public static final int CAPITALIZE_NONE = 0x0;

    public static final int GRADIENT_HORIZONTAL = 0x0;
    public static final int GRADIENT_VERTICAL = 0x1;

    DecelerateInterpolator mDecelerator;
    OvershootInterpolator mOvershoot;
    boolean mAnimateTouch, mHasGradient, mScaleText;
    int mGradientOrientation;
    int mGradientStartColor, mGradientEndColor;
    int mCapitalizeTextStyle;
    int mMaxLines;
    float mMinTextSize, mDefaultTextSize, mTextSize;
    float mRotationAngle;
    Paint mTextPaint;
    Gradient mGradient;
    Typeface mTypeFace;

    int mTextColor;


    public WTextView(Context context) {
        super(context);
        initAttrs(context, null);
    }

    public WTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);

    }

    public WTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(context, attrs);

    }

    private void initAttrs(Context context, AttributeSet attrs) {

        mDecelerator = new DecelerateInterpolator();
        mOvershoot = new OvershootInterpolator(10f);

        //begin attrs
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.WTextView, 0, 0);

        if (!isInEditMode())
            mTypeFace = FontUtils.getFont(context, a.getString(R.styleable.WTextView_font));

        mHasGradient = a.getBoolean(R.styleable.WTextView_gradient, false);
        mGradientOrientation = a.getInt(R.styleable.WTextView_gradientOrientation, 0x1);
        mGradientStartColor = a.getColor(R.styleable.WTextView_gradientStart, Color.BLACK);
        mGradientEndColor = a.getColor(R.styleable.WTextView_gradientEnd, Color.BLACK);
        mAnimateTouch = a.getBoolean(R.styleable.WTextView_animateTouch, false);
        mCapitalizeTextStyle = a.getInteger(R.styleable.WTextView_caps, 0x0);
        mScaleText = a.getBoolean(R.styleable.WTextView_scaleText, false);
        mMinTextSize = a.getFloat(R.styleable.WTextView_minTextSize, 10.0f);
        mMaxLines = a.getInteger(R.styleable.WTextView_maxLines, Integer.MAX_VALUE);
        mRotationAngle = a.getFloat(R.styleable.WTextView_rotationAngle, 8);
        mDefaultTextSize = getTextSize();
        mTextPaint = new Paint();

        a.recycle();
        //end attrs

        if (!isInEditMode()) {
            setTypeface(mTypeFace);
        }
        this.setText(getText());

    }

    @Override
    //@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public boolean onTouchEvent(MotionEvent event) {
        if (!mAnimateTouch)
            return super.onTouchEvent(event);

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (isEnabled())

                animate().setInterpolator(mDecelerator).scaleX(0.85f).scaleY(0.85f).setDuration(100);
            else {
                animate().setInterpolator(new CycleInterpolator(2)).rotation(mRotationAngle).setDuration(80);
            }

        } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            if (isEnabled())
                animate().setInterpolator(mOvershoot).scaleX(1.0f).scaleY(1.0f).setDuration(300);
            else
                animate().setInterpolator(new LinearInterpolator()).rotation(0).setDuration(40);

        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed && mHasGradient && mGradient == null) {
            float xFactor = mGradientOrientation == GRADIENT_HORIZONTAL ? 0.2f : 0.0f;
            float yFactor = mGradientOrientation == GRADIENT_HORIZONTAL ? 0.0f : 0.2f;
            float xEnd = mGradientOrientation == GRADIENT_HORIZONTAL ? getWidth() : 0.0f;
            float yEnd = mGradientOrientation == GRADIENT_HORIZONTAL ? 0.0f : getHeight();

            mGradient = new LinearGradient(this)
                    .with(mGradientStartColor, mGradientEndColor)
                    .xStart(getWidth() * xFactor).yStart(getHeight() * yFactor)
                    .xEnd(xEnd * (1 - xFactor)).yEnd(yEnd * (1 - yFactor));

            this.getPaint().setShader(mGradient.getShader());
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        String t = text.toString();
        switch (mCapitalizeTextStyle) {
            case CAPITALIZE_ALL:
                t = t.toUpperCase();
                break;
        }

        //TODO Only for welvi
        t = t.replace("WELVI", "welvi");
        t = t.replace("Welvi", "welvi");

        super.setText(t, type);
    }

    private void refitText(String text, int textWidth) {
        if (textWidth <= 0 || text == null || text.length() == 0 || !mScaleText)
            return;

        // the width
        int targetWidth = textWidth - this.getPaddingLeft() - this.getPaddingRight();
        mTextSize = getTextSize();
        mTextPaint.set(getPaint());

        while (calculateLineCount(text, mTextSize) > mMaxLines) {
            mTextSize -= 0.1f;
        }
        // Use min size so that we undershoot rather than overshoot
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
    }

    public int calculateLineCount(String text, float size) {
        Rect bounds = new Rect();
        Paint paint = new Paint();
        paint.setTextSize(size);
        paint.getTextBounds(text, 0, text.length(), bounds);
        int wtext = (int) Math.ceil(bounds.width());
        int lines = (wtext + getMeasuredWidth() - 1) / getMeasuredWidth();
        return lines;
    }

    @Override
    protected void onTextChanged(final CharSequence text, final int start, final int before, final int after) {
        refitText(text.toString(), this.getWidth());
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        if (width != oldWidth)
            refitText(this.getText().toString(), width);
    }

    public void setAnimateTouch(boolean animateTouch) {
        mAnimateTouch = animateTouch;
    }
}
