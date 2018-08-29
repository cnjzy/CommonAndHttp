package com.base.ui.fragment;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.AbsListView;
import android.widget.ListView;

import com.base.R;
import com.common.http.bean.BaseBean;
import com.base.contract.DataListContract;
import com.base.ui.adapter.listview.BaseListViewAdapter;
import com.base.ui.listwrap.SwipeListViewLoadingWrap;

import java.util.List;

/**
 * Created by jzy on 2017/6/13.
 * 列表型fragment基类，方便快速实现列表数据
 */
public abstract class BaseListFragment<B extends BaseBean, P extends DataListContract.Presenter> extends BaseFragment<DataListContract.Presenter> implements DataListContract.IDataListView<B>, SwipeRefreshLayout.OnRefreshListener {
    protected SwipeRefreshLayout mSwipeRefresh;
    protected ListView mListView;
    protected BaseListViewAdapter<B> mAdapter;

    private SwipeListViewLoadingWrap<B> mSwipeListWrap = null;

    @Override
    public P getPresenter() {
        return (P) super.getPresenter();
    }

    @Override
    protected void loadExpandView() {
        super.loadExpandView();
        initListView();
        initListListener();
        initListWrap();
    }

    @Override
    public void onInitSuccess(final List<B> beanList, boolean isCache) {
        mSwipeListWrap.onInitSuccess(beanList, isCache);
    }



    @Override
    public void addData(final B bean) {
        mSwipeListWrap.addData(bean);
    }

    @Override
    public void addData(final List<B> beanList) {
        mSwipeListWrap.addData(beanList);
    }

    @Override
    public void clearData() {
        mSwipeListWrap.clearData();
    }

    @Override
    public void showEmptyView() {
        mSwipeListWrap.showEmptyView();
    }

    @Override
    public void isNotMoreData() {
        mSwipeListWrap.isNotMoreData();
    }

    @Override
    public void onRefresh() {
        mSwipeListWrap.onRefresh();
    }

    @Override
    public void showLoadingDialog() {
        if (mSwipeRefresh != null) {
            mSwipeRefresh.setRefreshing(true);
        }
    }

    @Override
    public void hideLoadingDialog() {
        if (mSwipeRefresh != null) {
            mSwipeRefresh.setRefreshing(false);
        }
    }

    /**
     * 如果需要重新实现ListView的OnScrollListener需要把onScroll里面代码加上
     */
    protected void initListListener() {
        if (mSwipeRefresh != null) {
            mSwipeRefresh.setOnRefreshListener(this);
        }

        if (mListView != null) {
            mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (firstVisibleItem + visibleItemCount > totalItemCount - 2 && getPresenter() != null && getPresenter().isLoadMore()) {
                        // 倒数第二条开始加载
                        if (getPresenter() != null) {
                            getPresenter().loadMore();
                        }
                    }
                }
            });
        }
    }

    private void initListWrap() {
        mSwipeListWrap = new SwipeListViewLoadingWrap<>(new SwipeListViewLoadingWrap.ISwipeListViewLoadingSetting() {
            @Override
            public SwipeRefreshLayout getSwipeRefreshLayout() {
                return mSwipeRefresh;
            }

            @Override
            public ListView getListView() {
                return mListView;
            }

            @Override
            public BaseListViewAdapter getListAdapter() {
                return mAdapter;
            }

            @Override
            public Activity getListActivity() {
                return getActivity();
            }

            @Override
            public DataListContract.Presenter getListPresenter() {
                return getPresenter();
            }
        });
    }

    private void initListView() {
        if (getListViewId() > 0) {
            mListView = (ListView) findViewById(getListViewId());
        }
        if (getSwipeLayoutId() > 0) {
            mSwipeRefresh = (SwipeRefreshLayout) findViewById(getSwipeLayoutId());
            if (mSwipeRefresh != null) {
                mSwipeRefresh.setColorSchemeColors(getResources().getColor(R.color.red));
            }
        }

        mAdapter = getAdapter();
    }

    protected void setAdapter() {
        if (mListView != null && mAdapter != null) {
            mListView.setAdapter(mAdapter);
        }
    }

    protected void initData(boolean isShowLoading) {
        if (getPresenter() != null) {
            getPresenter().initData(isShowLoading);
        }
    }

    public abstract int getListViewId();

    public abstract int getSwipeLayoutId();

    public abstract BaseListViewAdapter getAdapter();
}
