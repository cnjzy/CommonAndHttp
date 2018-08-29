package com.base.ui.adapter;

import android.content.Context;
import android.view.View;

import com.base.ui.adapter.view.EmptyAdapterView;
import com.base.ui.adapter.view.LoadingAdapterView;
import com.base.ui.view.IBaseViewHolder;

/**
 * Created by jzy on 2018/3/28.
 */

public class AdapterViewWrap {

    private IAdapterView mLoadingView = null;
    private IAdapterView mEmptyView = null;

    /**
     * adapter 相关设置
     */
    private IAdapterViewSetting mSetting = null;


    public AdapterViewWrap(IAdapterViewSetting setting) {
        this.mSetting = setting;
    }

    /******************************************************* empty view **********************************************************/

    /**
     * @return
     */
    public boolean isShowEmptyView(int position) {
        return mEmptyView != null && mEmptyView.isShowView(position);
    }

    public IBaseViewHolder setEmptyViewResId(int resId) {
        if (mEmptyView == null) {
            mEmptyView = new EmptyAdapterView().setSetting(mSetting);
        }
        return mEmptyView.setViewResId(resId);
    }

    public IBaseViewHolder getEmptyViewHolder() {
        if (mEmptyView == null) {
            return null;
        }
        return mEmptyView.getViewHolder();
    }

    public View getEmptyView() {
        if (mEmptyView == null) {
            return null;
        }
        return mEmptyView.getView();
    }

    /************************************************************************* loading view **********************************************************************/

    public boolean isShowLoadingView(int position) {
        return mLoadingView != null && mLoadingView.isShowView(position);
    }

    public IBaseViewHolder setLoadingViewResId(int resId) {
        if (mLoadingView == null) {
            mLoadingView = new LoadingAdapterView().setSetting(mSetting);
        }
        return mLoadingView.setViewResId(resId);
    }

    public IBaseViewHolder getLoadingViewHolder() {
        if (mLoadingView == null) {
            return null;
        }
        return mLoadingView.getViewHolder();
    }

    /**
     * 获取空视图item
     *
     * @return
     */
    public View getLoadingView() {
        if (mLoadingView == null) {
            return null;
        }
        return mLoadingView.getView();
    }

    /******************************************************* interface **********************************************************/

    public interface IAdapterViewSetting {
        boolean isInitData();

        int getDataListSize();

        Context getContext();

        View getParentView();
    }

}
