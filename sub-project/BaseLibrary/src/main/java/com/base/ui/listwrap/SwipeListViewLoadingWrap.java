package com.base.ui.listwrap;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;

import com.common.http.bean.BaseBean;
import com.base.contract.DataListContract;
import com.base.contract.IDataListWrap;
import com.base.ui.adapter.listview.BaseListViewAdapter;

import java.util.List;

/**
 * Created by jzy on 2018/3/29.
 */

public class SwipeListViewLoadingWrap<B extends BaseBean> implements IDataListWrap<B>, SwipeRefreshLayout.OnRefreshListener {

    private ISwipeListViewLoadingSetting mSetting;

    public SwipeListViewLoadingWrap(ISwipeListViewLoadingSetting setting) {
        this.mSetting = setting;
    }

    private boolean isInitSetting() {
        return mSetting != null && mSetting.getListActivity() != null;
    }

    @Override
    public void onRefresh() {
        if (!isInitSetting()) {
            return;
        }
        if (mSetting.getListPresenter() != null) {
            mSetting.getListPresenter().initData(false);
        }
    }

    @Override
    public void onInitSuccess(final List<B> beanList, boolean isCache) {
        if (!isInitSetting()) {
            return;
        }
        mSetting.getListActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mSetting.getListAdapter() != null) {
                    mSetting.getListAdapter().addDataToTop(beanList);
                    if (beanList != null && beanList.size() > 0) {
                        mSetting.getListAdapter().setLoadMore(true);
                    }
                }
                if (mSetting.getSwipeRefreshLayout() != null) {
                    mSetting.getSwipeRefreshLayout().setRefreshing(false);
                }

            }
        });
    }

    @Override
    public void addData(final B bean) {
        if (!isInitSetting()) {
            return;
        }
        mSetting.getListActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mSetting.getListAdapter() != null) {
                    mSetting.getListAdapter().addData(bean);
                }
            }
        });
    }

    @Override
    public void addData(final List<B> beanList) {
        if (!isInitSetting()) {
            return;
        }
        mSetting.getListActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mSetting.getListAdapter() != null) {
                    mSetting.getListAdapter().addData(beanList);
                }
            }
        });
    }

    @Override
    public void clearData() {
        if (!isInitSetting()) {
            return;
        }
        mSetting.getListActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mSetting.getListAdapter() != null) {
                    mSetting.getListAdapter().clearData();
                }
            }
        });
    }

    @Override
    public void showEmptyView() {
        if (!isInitSetting()) {
            return;
        }
        mSetting.getListActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mSetting.getListAdapter() != null) {
                    mSetting.getListAdapter().setLoadMore(false);
                }
                if (mSetting.getSwipeRefreshLayout() != null) {
                    mSetting.getSwipeRefreshLayout().setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void isNotMoreData() {
        if (!isInitSetting()) {
            return;
        }
        mSetting.getListActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mSetting.getListAdapter() != null) {
                    mSetting.getListAdapter().setLoadMore(false);
                }
            }
        });
    }

    public interface ISwipeListViewLoadingSetting {
        SwipeRefreshLayout getSwipeRefreshLayout();

        ListView getListView();

        BaseListViewAdapter getListAdapter();

        Activity getListActivity();

        DataListContract.Presenter getListPresenter();
    }
}
