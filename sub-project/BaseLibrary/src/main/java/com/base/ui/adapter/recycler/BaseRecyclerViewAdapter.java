package com.base.ui.adapter.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.base.ui.adapter.AdapterViewWrap;
import com.base.ui.adapter.IAdapterContract;
import com.base.ui.view.IBaseViewHolder;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 给予LitviewAdapter的封装类
 * Created by jzy on 2017/5/23.
 */

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<AbsViewHolder<T>> implements IAdapterContract, AdapterViewWrap.IAdapterViewSetting {
    protected Context mContext;
    protected List<T> mDataList = new ArrayList<>();

    /**
     * 初始化数据
     * false 未初始化， true已经初始化
     */
    private boolean mInitData = false;

    /**
     * 父布局
     */
    protected XRecyclerView mRecyclerView;

    /**
     * adapter 装饰类
     */
    private AdapterViewWrap mAdapterViewWrap = null;

    public BaseRecyclerViewAdapter(Context context) {
        this.mContext = context;
        mAdapterViewWrap = new AdapterViewWrap(this);
    }

    @Override
    public int getItemCount() {
        return mDataList.size() + (isShowLoadingView(0) ? 1 : 0) + (isShowEmptyView(0) ? 1 : 0);
    }

    public T getItem(int position) {
        return position < 0 || position >= mDataList.size() ? null : mDataList.get(position);
    }

    @Override
    public void onBindViewHolder(AbsViewHolder<T> holder, int position) {
        holder.bindData(getItem(position), position);
    }

    @Override
    public AbsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_EMPTY) {
            return createViewHoler(getEmptyView(), viewType);
        } else if (viewType == VIEW_TYPE_LOADING) {
            return createViewHoler(getLoadingView(), viewType);
        }

        View convertView = View.inflate(mContext, getConvertViewRsId(viewType), null);
        return createViewHoler(convertView, viewType);
    }

    @Override
    public void onBindViewHolder(AbsViewHolder<T> holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowEmptyView(position)) {
            return VIEW_TYPE_EMPTY;
        } else if (isShowLoadingView(position)) {
            return VIEW_TYPE_LOADING;
        }
        return super.getItemViewType(position);
    }

    public List<T> getDataList() {
        return mDataList;
    }

    public void addData(List<T> dataList) {
        if (dataList != null) {
            mDataList.addAll(dataList);
        }
        notifyDataSetChanged();
        mInitData = true;
    }

    public void addData(T t) {
        if (t != null) {
            mDataList.add(t);
        }
        notifyDataSetChanged();
        mInitData = true;
    }

    public void addDataToTop(List<T> dataList) {
        if (dataList != null) {
            mDataList.addAll(0, dataList);
            notifyDataSetChanged();
        }
        notifyDataSetChanged();
        mInitData = true;
    }

    public void addDataToTop(T t) {
        if (t != null) {
            mDataList.add(0, t);
            notifyDataSetChanged();
        }
        notifyDataSetChanged();
        mInitData = true;
    }

    public void delData(T t) {
        if (t != null) {
            mDataList.remove(t);
            notifyDataSetChanged();
        }
    }

    public void delData(int position) {
        if (position >= 0 && position < mDataList.size()) {
            mDataList.remove(position);
            notifyDataSetChanged();
        }
    }

    public void clearData() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    public BaseRecyclerViewAdapter setRecyclerView(XRecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
        return this;
    }

    public abstract AbsViewHolder createViewHoler(View parent, int viewType);

    public abstract int getConvertViewRsId(int viewType);


    /************************************************************************* adapter view **********************************************************************/
    private boolean isShowLoadingView(int position) {
        if (mAdapterViewWrap == null) {
            return false;
        }
        return mAdapterViewWrap.isShowLoadingView(position);
    }

    public IBaseViewHolder setLoadingViewResId(int resId) {
        if (mAdapterViewWrap == null) {
            return null;
        }
        return mAdapterViewWrap.setLoadingViewResId(resId);
    }

    public IBaseViewHolder getLoadingViewHolder() {
        if (mAdapterViewWrap == null) {
            return null;
        }
        return mAdapterViewWrap.getLoadingViewHolder();
    }

    public View getLoadingView() {
        if (mAdapterViewWrap == null) {
            return null;
        }
        return mAdapterViewWrap.getLoadingView();
    }


    private boolean isShowEmptyView(int position) {
        if (mAdapterViewWrap == null) {
            return false;
        }
        return mAdapterViewWrap.isShowEmptyView(position);
    }

    public IBaseViewHolder setEmptyViewResId(int resId) {
        if (mAdapterViewWrap == null) {
            return null;
        }
        return mAdapterViewWrap.setEmptyViewResId(resId);
    }

    public IBaseViewHolder getEmptyViewHolder() {
        if (mAdapterViewWrap == null) {
            return null;
        }
        return mAdapterViewWrap.getEmptyViewHolder();
    }

    public View getEmptyView() {
        if (mAdapterViewWrap == null) {
            return null;
        }
        return mAdapterViewWrap.getEmptyView();
    }


    @Override
    public boolean isInitData() {
        return mInitData;
    }

    @Override
    public int getDataListSize() {
        return mDataList.size();
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public View getParentView() {
        return mRecyclerView;
    }
}
