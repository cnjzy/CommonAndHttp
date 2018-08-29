package com.base.ui.view;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by jzy on 2018/3/26.
 */

public interface IViewHolderWrap {
    <V extends View> V findViewById(View view, int id);

    IViewHolderWrap setText(int viewId, int textResId);

    IViewHolderWrap setText(int viewId, String text);

    IViewHolderWrap setOnClickListener(int viewId, View.OnClickListener listener);

    IViewHolderWrap setBackground(int viewId, int resId);

    IViewHolderWrap setBackground(int viewId, Drawable drawable);

    IViewHolderWrap setImageResource(int viewId, int resId);

    IViewHolderWrap setImageBitmap(int viewId, Bitmap bitmap);

    IViewHolderWrap setImageDrawable(int viewId, Drawable drawable);

    IViewHolderWrap setImageWithUrl(int viewId, String url);

    public IViewHolderWrap setViewVisible(int viewId, int visible);
}
