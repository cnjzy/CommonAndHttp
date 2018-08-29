package com.base.ui.adapter;

import android.view.View;

import com.base.ui.view.IBaseViewHolder;

/**
 * Created by jzy on 2018/3/28.
 */

public interface IAdapterView {
    boolean isShowView(int position);

    IBaseViewHolder setViewResId(int resId);

    IBaseViewHolder getViewHolder();

    View getView();

    IAdapterView setSetting(AdapterViewWrap.IAdapterViewSetting setting);
}
