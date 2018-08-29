package com.base.widget.image;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.base.R;
import com.library.util.glide.ImageLoader;


public class NetworkImageView extends ImageView {

    private int mRoundRadius = 0;
    private boolean mIsCircle = false;

    public NetworkImageView(Context context) {
        this(context, null);
    }

    public NetworkImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NetworkImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        TypedArray tArray = context.obtainStyledAttributes(attrs, R.styleable.NetworkImageView);
        if (tArray == null) {
            return;
        }

        mRoundRadius = (int) tArray.getDimension(R.styleable.NetworkImageView_roundRadius, 0);
        mIsCircle = tArray.getBoolean(R.styleable.NetworkImageView_circle, false);
    }

    public void setUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        ImageLoader.loadImage(this, url);
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        Bitmap bitmap = null;
        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        }
        if (bitmap != null) {
            setMeasuredDimension(0, 0);
            int width = getWidth() == 0 ? getMeasuredHeight() : getWidth();
            int height = getHeight() == 0 ? getMeasuredHeight() : getHeight();
            super.setImageDrawable(new NetworkImageDrawable(bitmap, width, height, mRoundRadius).setIsCircle(mIsCircle));
        } else {
            super.setImageDrawable(drawable);
        }
    }
}
