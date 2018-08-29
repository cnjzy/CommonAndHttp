package com.base.widget.image;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

public class NetworkImageDrawable extends Drawable {

    private Paint mPaint;
    private Bitmap mBitmap;

    private int mWidth = 0;
    private int mHeight = 0;
    private int mRadius = 0;
    private boolean mIsCircle;

    private RectF rectF;

    public NetworkImageDrawable(Bitmap bitmap, int width, int height, int radius) {
        this.mBitmap = bitmap;
        this.mWidth = width;
        this.mHeight = height;
        this.mRadius = radius;
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        if (width != 0 && height != 0) {
            Matrix matrix = new Matrix();
            float scale = width > mBitmap.getWidth() || height > mBitmap.getHeight() ? Math.max(width / mBitmap.getWidth(), height / mBitmap.getHeight()) : Math.min(width / mBitmap.getWidth(), height / mBitmap.getHeight());
            matrix.setScale(scale, scale);
            bitmapShader.setLocalMatrix(matrix);
        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(bitmapShader);
    }

    public NetworkImageDrawable setWidht(int width) {
        this.mWidth = width;
        return this;
    }

    public NetworkImageDrawable setmHeight(int height) {
        this.mHeight = height;
        return this;
    }

    public NetworkImageDrawable setRadius(int radius) {
        this.mRadius = radius;
        return this;
    }

    public NetworkImageDrawable setIsCircle(boolean isCircle) {
        this.mIsCircle = isCircle;
        return this;
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        rectF = new RectF(left, top, right, bottom);
    }

    @Override
    public void draw(Canvas canvas) {
        if (mIsCircle) {
            canvas.drawCircle(mWidth / 2, mHeight / 2, Math.min(mWidth, mHeight) / 2, mPaint);
        } else {
            canvas.drawRoundRect(rectF, mRadius, mRadius, mPaint);
        }
    }

    @Override
    public int getIntrinsicWidth() {
        return mWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return mHeight;
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

}