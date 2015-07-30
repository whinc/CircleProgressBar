package com.whinc.circleprogressbar;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

public class CircleProgressBar extends View {
    private float mMaxValue;
    private float mValue;
    private int mBackgroundColor;
    private int mProgressColor;
    private float mTextSize;
    private int mTextColor;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CircleProgressBar(Context context) {
        super(context);
        init(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public float getMaxValue() {
        return mMaxValue;
    }

    public void setMaxValue(float maxValue) {
        mMaxValue = maxValue;
        invalidate();
    }

    public float getValue() {
        return mValue;
    }

    public void setValue(float value) {
        mValue = value;
        invalidate();
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        mBackgroundColor = backgroundColor;
    }

    public int getProgressColor() {
        return mProgressColor;
    }

    public void setProgressColor(int progressColor) {
        mProgressColor = progressColor;
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float textSize) {
        mTextSize = textSize;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    private void init(Context c, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }

        TypedArray typedArray = c.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
        mMaxValue = typedArray.getFloat(R.styleable.CircleProgressBar_maxValue, 100.0f);
        mValue = typedArray.getFloat(R.styleable.CircleProgressBar_value, 0.0f);
        mBackgroundColor = typedArray.getColor(R.styleable.CircleProgressBar_backgroudColor, Color.GRAY);
        mProgressColor = typedArray.getColor(R.styleable.CircleProgressBar_progressColor, Color.YELLOW);
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.CircleProgressBar_textSize, 12);
        mTextColor = typedArray.getColor(R.styleable.CircleProgressBar_textColor, Color.WHITE);

        mMaxValue = Math.max(0, mMaxValue);
        mValue = Math.max(0, Math.min(mMaxValue, mValue));

        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.UNSPECIFIED) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                    (int)mTextSize, MeasureSpec.EXACTLY);
        }
        if (heightMode == MeasureSpec.UNSPECIFIED) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    (int)mTextSize, MeasureSpec.EXACTLY);
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        float radius = Math.min(width, height) / 2.0f;
        float percentage = mValue / mMaxValue;
        float cx = width / 2.0f + getPaddingLeft();
        float cy = height / 2.0f + getPaddingTop();

        // 画进度条背景色(圆形)
        mPaint.setColor(mBackgroundColor);
        canvas.drawCircle(cx, cy, radius, mPaint);

        // 画进度条前景色(扇形)
        mPaint.setColor(mProgressColor);
        RectF rect = new RectF(cx - radius, cy - radius, cx + radius, cy + radius);
        canvas.drawArc(rect, 0.0f, percentage * 360.0f, true, mPaint);

        // 画百分比文字（注意：drawText 函数的坐标系中垂直向上为Y轴正方向，这与Android坐标系相反）
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);
        String text = String.format("%.1f%%", percentage * 100);
        float fontHeight = mPaint.measureText("0");     // 测量单个字符的宽度作为高度，有点粗糙，不过没关系
        canvas.drawText(text, cx - mPaint.measureText(text) / 2.0f, cy + fontHeight/2.0f, mPaint);
    }
}
