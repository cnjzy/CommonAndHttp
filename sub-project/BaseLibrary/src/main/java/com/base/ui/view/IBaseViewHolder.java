package com.base.ui.view;

import android.view.View;

/**
 * Created by jzy on 2017/5/25.
 * 用户ViewHolder实现，统一实现方式
 */

public interface IBaseViewHolder<T> extends IViewHolderWrap {
    void bindView(View convertView);
    void bindData(T t, int position, Object... args);
    <V extends View> V findViewById(int id);
}
