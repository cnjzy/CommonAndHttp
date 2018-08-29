package com.base.ui.presenter;

import com.common.http.bean.BaseBean;
import com.base.contract.DataListContract;

import java.util.List;

/**
 * Created by jzy on 2017/6/13.
 */

public abstract class DataListPresenter<V extends DataListContract.IDataListView> extends AbsBaseListMvpPresenter implements DataListContract.Presenter {
    protected int page = 1;
    protected Boolean mIsLoadMore = true;
    protected boolean mIsInit = false;

    @Override
    public V getView() {
        return (V) super.getView();
    }

    @Override
    public void initData(boolean isShowLoading) {
        mIsInit = true;
        mIsLoadMore = false;
        page = 1;
        if (isShowLoading && getView() != null) {
            getView().showLoadingDialog();
        }
    }

    @Override
    public boolean isLoadMore() {
        return mIsLoadMore && mIsInit;
    }


    protected <B extends BaseBean> void initDataSuccess(List<B> dataList) {
        initDataSuccess(dataList, true, false);
    }

    protected <B extends BaseBean> void initDataSuccess(List<B> dataList, boolean isCleanData) {
        initDataSuccess(dataList, isCleanData, false);
    }

    protected <B extends BaseBean> void initDataSuccess(List<B> dataList, boolean isClearData, boolean isCache) {
        if (dataList != null && dataList.size() > 0) {
            page++;
            mIsLoadMore = true;
            mIsInit = true;
        } else if (page == 1) {
            if (getView() != null) {
                getView().showEmptyView();
            }
        }
        if (isClearData) {
            if (getView() != null) {
                getView().clearData();
            }
        }
        if (getView() != null) {
            getView().onInitSuccess(dataList, isCache);
            getView().hideLoadingDialog();
        }
    }

    protected void initDataError(String msg) {
        mIsInit = true;
        mIsLoadMore = false;
        if (getView() != null) {
            getView().showToast(msg, false);
            getView().showEmptyView();
            getView().addData(null);
            getView().hideLoadingDialog();
        }
    }

    protected <B extends BaseBean> void loadMoreSuccess(List<B> dataList) {
        if (dataList != null && dataList.size() > 0) {
            mIsLoadMore = true;
            page++;
            if (getView() != null) {
                getView().addData(dataList);
            }
        } else {
            if (getView() != null) {
                getView().isNotMoreData();
            }
        }
    }

    protected void loadMoreError(String msg) {
        mIsLoadMore = false;
        if (getView() != null) {
            getView().showToast(msg, false);
            getView().showEmptyView();
        }
    }
}
