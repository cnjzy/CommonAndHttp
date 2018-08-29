package com.base.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Network;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.R;
import com.base.ui.view.IViewHolderWrap;
import com.base.widget.image.NetworkImageDrawable;
import com.base.widget.image.NetworkImageView;

/**
 * Created by jzy on 2018/3/26.
 */

public class ViewHolderWrap implements IViewHolderWrap {

    private Context mContext = null;
    private View mRootView = null;

    public ViewHolderWrap(Context context, View rootView) {
        this.mContext = context;
        this.mRootView = rootView;
    }

    public <T extends View> T findViewById(View view, int id) {
        if (view == null) {
            return null;
        }
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag(R.id.tag_list_view_arrray);
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }

        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }

        return (T) childView;
    }

    public IViewHolderWrap setText(int viewId, int textResId) {
        if (textResId == 0) {
            return this;
        }
        setText(viewId, mContext.getString(textResId));
        return this;
    }

    public IViewHolderWrap setText(int viewId, String text) {
        if (viewId == 0) {
            return this;
        }
        try {
            TextView textView = findViewById(mRootView, viewId);
            if (textView != null) {
                textView.setText(text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public IViewHolderWrap setOnClickListener(int viewId, View.OnClickListener listener) {
        try {
            View view = findViewById(mRootView, viewId);
            if (view != null) {
                view.setOnClickListener(listener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public IViewHolderWrap setBackground(int viewId, int resId) {
        try {
            View view = findViewById(mRootView, viewId);
            if (view != null) {
                view.setBackgroundResource(resId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public IViewHolderWrap setBackground(int viewId, Drawable drawable) {
        try {
            View view = findViewById(mRootView, viewId);
            if (view != null) {
                view.setBackgroundDrawable(drawable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public IViewHolderWrap setImageResource(int viewId, int resId) {
        try {
            View view = findViewById(mRootView, viewId);
            if (view != null && view instanceof ImageView) {
                ((ImageView) view).setImageResource(resId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public IViewHolderWrap setImageBitmap(int viewId, Bitmap bitmap) {
        try {
            View view = findViewById(mRootView, viewId);
            if (view != null && view instanceof ImageView) {
                ((ImageView) view).setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public IViewHolderWrap setImageDrawable(int viewId, Drawable drawable) {
        try {
            View view = findViewById(mRootView, viewId);
            if (view != null && view instanceof ImageView) {
                ((ImageView) view).setImageDrawable(drawable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public IViewHolderWrap setImageWithUrl(int viewId, String url) {
        try {
            View view = findViewById(mRootView, viewId);
            if (view != null && view instanceof NetworkImageView) {
                ((NetworkImageView) view).setUrl(url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public IViewHolderWrap setViewVisible(int viewId, int visible) {
        try {
            View view = findViewById(mRootView, viewId);
            if (view != null) {
                view.setVisibility(visible);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
}
