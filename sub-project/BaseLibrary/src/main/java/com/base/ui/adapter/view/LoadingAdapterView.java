package com.base.ui.adapter.view;

import android.view.View;
import android.widget.AbsListView;

import com.base.ui.adapter.AdapterViewHolder;
import com.base.ui.adapter.AdapterViewWrap;
import com.base.ui.adapter.IAdapterView;
import com.base.ui.view.IBaseViewHolder;
import com.library.log.LogUtils;

/**
 * Created by jzy on 2018/3/28.
 */

public class LoadingAdapterView implements IAdapterView {

    private AdapterViewWrap.IAdapterViewSetting mSetting = null;

    /**
     * 加载视图
     */
    private View mView = null;
    private IBaseViewHolder mViewHolder = null;

    @Override
    public boolean isShowView(int position) {
        return mSetting != null && !mSetting.isInitData() && mView != null && mSetting.getDataListSize() == 0 && position == 0;
    }

    @Override
    public IBaseViewHolder setViewResId(int resId) {
        if (resId == 0 || !isSettingInit()) {
            return null;
        }
        mView = View.inflate(mSetting.getContext(), resId, null);
        mViewHolder = new AdapterViewHolder(mView);
        return mViewHolder;
    }

    @Override
    public IBaseViewHolder getViewHolder() {
        return mViewHolder;
    }

    @Override
    public View getView() {
        if (mView == null || !isSettingInit()) {
            return null;
        }
        // 计算高度
        AbsListView.LayoutParams params = (AbsListView.LayoutParams) mView.getLayoutParams();
        if (params == null) {
            params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
        }

        View listView = mSetting.getParentView();
        if (listView != null) {
            int listViewHeight = listView.getHeight();
            if (listViewHeight == 0) {
                listView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                listViewHeight = listView.getMeasuredHeight();
            }
            params.height = listViewHeight;
            mView.setLayoutParams(params);
        }

        return mView;
    }

    @Override
    public IAdapterView setSetting(AdapterViewWrap.IAdapterViewSetting setting) {
        mSetting = setting;
        return this;
    }

    public boolean isSettingInit() {
        return mSetting != null && mSetting.getParentView() != null && mSetting.getContext() != null;
    }
}
