package com.base.ui.adapter.recycler;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.base.ui.view.IBaseViewHolder;
import com.base.ui.view.IViewHolderWrap;
import com.base.ui.view.ViewHolderWrap;

/**
 * Created by jzy on 2018/3/26.
 */

public abstract class AbsViewHolder<B> extends RecyclerView.ViewHolder implements IBaseViewHolder<B> {
    private View mRootView = null;
    private ViewHolderWrap mWrap = null;

    public AbsViewHolder(View itemView) {
        super(itemView);

        if (itemView == null) {
            throw new RuntimeException("list adapter converView is null");
        }

        bindView(itemView);
    }

    @Override
    public final void bindView(View convertView) {
        mRootView = convertView;
        mWrap = new ViewHolderWrap(mRootView.getContext(), mRootView);
    }

    @Override
    public <V extends View> V findViewById(int id) {
        return mWrap.findViewById(mRootView, id);
    }

    @Override
    public <V extends View> V findViewById(View view, int id) {
        return mWrap.findViewById(view, id);
    }

    @Override
    public IViewHolderWrap setText(int viewId, int textResId) {
        return mWrap.setText(viewId, textResId);
    }

    @Override
    public IViewHolderWrap setText(int viewId, String text) {
        return mWrap.setText(viewId, text);
    }

    @Override
    public IViewHolderWrap setOnClickListener(int viewId, View.OnClickListener listener) {
        return mWrap.setOnClickListener(viewId, listener);
    }

    @Override
    public IViewHolderWrap setBackground(int viewId, int resId) {
        return mWrap.setBackground(viewId, resId);
    }

    @Override
    public IViewHolderWrap setBackground(int viewId, Drawable drawable) {
        return mWrap.setBackground(viewId, drawable);
    }

    @Override
    public IViewHolderWrap setImageResource(int viewId, int resId) {
        return mWrap.setImageResource(viewId, resId);
    }

    @Override
    public IViewHolderWrap setImageBitmap(int viewId, Bitmap bitmap) {
        return mWrap.setImageBitmap(viewId, bitmap);
    }

    @Override
    public IViewHolderWrap setImageDrawable(int viewId, Drawable drawable) {
        return mWrap.setImageDrawable(viewId, drawable);
    }

    @Override
    public IViewHolderWrap setImageWithUrl(int viewId, String url) {
        return mWrap.setImageWithUrl(viewId, url);
    }

    @Override
    public IViewHolderWrap setViewVisible(int viewId, int visible) {
        return mWrap.setViewVisible(viewId, visible);
    }
}
