package com.base.ui.listwrap;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;

import com.common.http.bean.BaseBean;
import com.base.contract.DataListContract;
import com.base.contract.IDataListWrap;
import com.base.ui.adapter.recycler.BaseRecyclerViewAdapter;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

/**
 * Created by jzy on 2018/3/29.
 */

public class XRecyclerViewLoadingWrap<B extends BaseBean> implements IDataListWrap<B>, SwipeRefreshLayout.OnRefreshListener {

    private IXRecyclerViewLoadingSetting mSetting;
    private final long IS_SHOW_LOAD_MORE_DELAY = 200;
    private final int CODE_IS_SHOW_LOAD_MORE = 5001;
    private Handler mRecyclerHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_IS_SHOW_LOAD_MORE:
                    if (isShowLoadMore()) {
                        mSetting.getListView().showLoadMore();
                    }
                    break;
            }
        }
    };

    public XRecyclerViewLoadingWrap(IXRecyclerViewLoadingSetting setting) {
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
                }
                onLoadComplete();
                mRecyclerHandler.sendEmptyMessageDelayed(CODE_IS_SHOW_LOAD_MORE, IS_SHOW_LOAD_MORE_DELAY);
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
                onLoadComplete();
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
                onLoadComplete();
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
                onLoadComplete();
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
                onLoadComplete();
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
                if (mSetting.getListView() != null) {
                    mSetting.getListView().setNoMore(true);
                }
                onLoadComplete();
            }
        });
    }

    private void onLoadComplete() {
        if (!isInitSetting()) {
            return;
        }
        if (mSetting.getListView() != null) {
            mSetting.getListView().refreshComplete();
            mSetting.getListView().loadMoreComplete();
        }
    }

    private boolean isShowLoadMore() {
        if (mSetting == null || mSetting.getListView() == null) {
            return false;
        }
        return mSetting.getListView().getLastVisibleItemPosition() >= mSetting.getListView().getItemCount() - mSetting.getListView().getLimitNumberToCallLoadMore();
    }

    public interface IXRecyclerViewLoadingSetting {
        XRecyclerView getListView();

        BaseRecyclerViewAdapter getListAdapter();

        Activity getListActivity();

        DataListContract.Presenter getListPresenter();
    }
}
