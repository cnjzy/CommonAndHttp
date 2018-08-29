package com.base.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.base.R;
import com.common.http.bean.BaseBean;
import com.base.contract.DataListContract;
import com.base.ui.adapter.recycler.BaseRecyclerViewAdapter;
import com.base.ui.listwrap.XRecyclerViewLoadingWrap;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

/**
 * Created by jzy on 2017/6/13.
 * 列表型Activity基类，方便快速实现列表数据
 */

public abstract class BaseRecyclerViewFragment<B extends BaseBean, P extends DataListContract.Presenter> extends BaseFragment<DataListContract.Presenter> implements DataListContract.IDataListView<B>, SwipeRefreshLayout.OnRefreshListener {
    private Activity mAct = null;

    protected XRecyclerView mRecyclerView;
    protected BaseRecyclerViewAdapter<B> mAdapter;

    private XRecyclerViewLoadingWrap<B> mRecyclerViewLoadingWrap = null;

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
        mRecyclerViewLoadingWrap.onInitSuccess(beanList, isCache);
    }

    @Override
    public void addData(final B bean) {
        mRecyclerViewLoadingWrap.addData(bean);
    }

    @Override
    public void addData(final List<B> beanList) {
        mRecyclerViewLoadingWrap.addData(beanList);
    }

    @Override
    public void clearData() {
        mRecyclerViewLoadingWrap.clearData();
    }

    @Override
    public void showEmptyView() {
        mRecyclerViewLoadingWrap.showEmptyView();
    }

    @Override
    public void isNotMoreData() {
        mRecyclerViewLoadingWrap.isNotMoreData();
    }

    @Override
    public void onRefresh() {
        mRecyclerViewLoadingWrap.onRefresh();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mAct = (Activity) context;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroyView();
        if (mRecyclerView != null) {
            mRecyclerView.destroy();
        }
    }

    public void setEmptyView(View view) {
        if (mRecyclerView != null) {
            mRecyclerView.setEmptyView(view);
        }
    }

    /**
     * 如果需要重新实现ListView的OnScrollListener需要把onScroll里面代码加上
     */
    protected void initListListener() {
        if (mRecyclerView != null) {
            mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
                @Override
                public void onRefresh() {
                    if (getPresenter() != null) {
                        getPresenter().initData(false);
                    }
                }

                @Override
                public void onLoadMore() {
                    if (getPresenter() != null) {
                        getPresenter().loadMore();
                    }
                }
            });
        }

    }

    private void initListWrap() {
        mRecyclerViewLoadingWrap = new XRecyclerViewLoadingWrap<>(new XRecyclerViewLoadingWrap.IXRecyclerViewLoadingSetting() {
            @Override
            public XRecyclerView getListView() {
                return mRecyclerView;
            }

            @Override
            public BaseRecyclerViewAdapter getListAdapter() {
                return mAdapter;
            }

            @Override
            public Activity getListActivity() {
                return mAct;
            }

            @Override
            public DataListContract.Presenter getListPresenter() {
                return getPresenter();
            }
        });
    }

    private void initListView() {
        if (getRecyclerId() > 0) {
            mRecyclerView = (XRecyclerView) this.findViewById(getRecyclerId());
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(layoutManager);

            mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
//            mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

//            mRecyclerView.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
//            View header = LayoutInflater.from(this).inflate(R.layout.recyclerview_header, (ViewGroup) findViewById(android.R.id.content), false);
//            mRecyclerView.addHeaderView(header);

            mRecyclerView.getDefaultFootView().setLoadingHint(getString(R.string.recycler_view_loading_tip));
            mRecyclerView.getDefaultFootView().setNoMoreHint(getString(R.string.recycler_view_loading_complete_tip));
        }

        mAdapter = getAdapter();
    }

    protected void setAdapter() {
        if (mRecyclerView != null && mAdapter != null) {
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    protected void initData(boolean isShowLoading) {
        if (getPresenter() != null) {
            getPresenter().initData(isShowLoading);
        }
    }

    protected XRecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public abstract int getRecyclerId();

    public abstract BaseRecyclerViewAdapter getAdapter();

}
