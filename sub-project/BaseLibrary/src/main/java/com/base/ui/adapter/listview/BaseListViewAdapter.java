package com.base.ui.adapter.listview;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.base.R;
import com.base.ui.adapter.AdapterViewWrap;
import com.base.ui.adapter.IAdapterContract;
import com.base.ui.view.IBaseViewHolder;
import com.base.ui.view.ITipBaseUI;
import com.base.widget.DefaultTipUI;
import com.base.widget.progress.CircularProgressView;

import java.util.ArrayList;
import java.util.List;

/**
 * 给予LitviewAdapter的封装类
 * Created by jzy on 2017/5/23.
 */

public abstract class BaseListViewAdapter<T> extends BaseAdapter implements ITipBaseUI, IAdapterContract, AdapterViewWrap.IAdapterViewSetting {
    protected Context mContext;
    protected List<T> mDataList = new ArrayList<>();

    /**
     * 是否需要加载更多
     */
    protected boolean mIsLoadMore = false;
    private View mLoadMoreView = null;

    /**
     * adapter 装饰类
     */
    private AdapterViewWrap mAdapterViewWrap = null;

    /**
     * 提示信息
     */
    private ITipBaseUI mTipBaseUI;

    /**
     * listview 当前显示view下标
     */
    private int mListViewScrollPosition;
    /**
     * listview 当前滚动坐标点
     */
    private int mListViewScrollPointTop;
    private ListView mListView;

    /**
     * 初始化数据
     * false 未初始化， true已经初始化
     */
    private boolean mInitData = false;

    @Override
    public void showToast(String content, boolean showInRelease) {
        mTipBaseUI.showToast(content, showInRelease);
    }

    @Override
    public void showTipDialog(String title, String content, TipCallback callback) {
        mTipBaseUI.showTipDialog(title, content, callback);
    }

    @Override
    public void showLoadingDialog() {
        mTipBaseUI.showLoadingDialog();
    }

    @Override
    public void hideLoadingDialog() {
        mTipBaseUI.hideLoadingDialog();
    }

    @Override
    public void onTipDestroy() {
        mTipBaseUI.onTipDestroy();
    }

    public BaseListViewAdapter(Context context) {
        this.mContext = context;
        mTipBaseUI = new DefaultTipUI(context);
        mAdapterViewWrap = new AdapterViewWrap(this);
    }

    @Override
    public int getCount() {
        return mDataList.size() + (isShowLoadingView(0) ? 1 : 0) + (isShowEmptyView(0) ? 1 : 0);
    }

    public int getDataCount() {
        return mDataList.size();
    }

    public List<T> getDataList() {
        return mDataList;
    }

    @Override
    public T getItem(int position) {
        return position < 0 || position >= mDataList.size() ? null : mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowEmptyView(position)) {
            return VIEW_TYPE_EMPTY;
        } else if (isLoadMorePosition(position)) {
            return VIEW_TYPE_LOAD_MORE;
        } else if (isShowLoadingView(position)) {
            return VIEW_TYPE_LOADING;
        }
        return super.getItemViewType(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (getItemViewType(position) == VIEW_TYPE_EMPTY) {
            return getEmptyView();
        } else if (getItemViewType(position) == VIEW_TYPE_LOAD_MORE) {
            return getLoadMoreView();
        } else if (getItemViewType(position) == VIEW_TYPE_LOADING) {
            return getLoadingView();
        }

        // convertView
        final IBaseViewHolder holder;
        if (isNotConvertView(convertView)) {
            convertView = View.inflate(mContext, getConvertViewRsId(getItemViewType(position)), null);

            holder = createViewHoler(convertView, getItemViewType(position));
            if (holder != null) {
                holder.bindView(convertView);
                convertView.setTag(holder);
            }
        } else {
            holder = (IBaseViewHolder) convertView.getTag();
        }

        T bean = getItem(position);
        if (bean != null && holder != null) {
            holder.bindData(bean, position);
        }

        return convertView;
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
        addDataToTop(dataList, false);
    }

    public void addDataToTop(List<T> dataList, boolean isNotPointChange) {
        if (dataList != null) {
            if (isNotPointChange && dataList.size() > 0) {
                saveListViewScrollPoint();
            }
            mDataList.addAll(0, dataList);
            notifyDataSetChanged();
            if (isNotPointChange && dataList.size() > 0) {
                resetListViewScrollPoint(dataList.size());
            }
        }
        notifyDataSetChanged();
        mInitData = true;
    }

    public void addDataToTop(T t) {
        addDataToTop(t, false);
    }

    public void addDataToTop(T t, boolean isNotPointChange) {
        if (t != null) {
            if (isNotPointChange) {
                saveListViewScrollPoint();
            }
            mDataList.add(0, t);
            notifyDataSetChanged();
            if (isNotPointChange) {
                resetListViewScrollPoint(1);
            }
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


    /**
     * 是否不是convertView
     *
     * @param convertView
     * @return
     */
    protected boolean isNotConvertView(View convertView) {
        if (convertView == null || convertView.getTag() == null || !(convertView.getTag() instanceof IBaseViewHolder)) {
            return true;
        }
        return false;
    }

    public BaseListViewAdapter setListView(ListView listView) {
        this.mListView = listView;
        return this;
    }

    private void saveListViewScrollPoint() {
        mListViewScrollPosition = mListView.getFirstVisiblePosition();
        View v = mListView.getChildAt(mListViewScrollPosition);
        mListViewScrollPointTop = (v == null) ? 0 : v.getTop();
    }

    private void resetListViewScrollPoint(int addSize) {
        if (mListView != null) {
            mListView.setSelectionFromTop(mListViewScrollPosition + addSize, mListViewScrollPointTop);
        }
    }


    /************************************************************************* load more view **********************************************************************/

    public void setLoadMore(boolean mIsLoadMore) {
        this.mIsLoadMore = mIsLoadMore;
        notifyDataSetChanged();
    }


    /**
     * 获取加载更多布局
     *
     * @return
     */
    public View getLoadMoreView() {
        return getLoadMoreView(View.VISIBLE);
    }

    public void setLoadMoreView(int visible, String moreStr) {
        getLoadMoreView(visible, moreStr);
        notifyDataSetChanged();
    }

    public void setLoadMoreView(String moreStr) {
        getLoadMoreView(0, moreStr);
        notifyDataSetChanged();
    }

    public void setLoadMoreView(int visible) {
        getLoadMoreView(visible, null);
        notifyDataSetChanged();
    }

    /**
     * 获取加载更多布局
     *
     * @param moreStr
     * @return
     */
    public View getLoadMoreView(String moreStr) {
        return getLoadMoreView(View.VISIBLE, moreStr);
    }

    /**
     * 获取加载更多布局
     *
     * @param visible
     * @return
     */
    public View getLoadMoreView(int visible) {
        return getLoadMoreView(visible, null);
    }

    /**
     * 获取加载更多布局
     *
     * @param visible
     * @param moreStr 显示，初始化时设置setLoadMore(true);
     *                设置内容以及状态，getView中
     *                if(mIsLoadMore && position == getCount-1){
     *                return getLoadMoreView();
     *                }
     *                隐藏，setLoadMore(false)
     * @return
     */
    public View getLoadMoreView(int visible, String moreStr) {
        if (mLoadMoreView == null) {
            mLoadMoreView = View.inflate(mContext, R.layout.view_listview_footer, null);
            mLoadMoreView.setTag(R.id.view_listview_footer_type, true);
        }

        CircularProgressView progressBar = (CircularProgressView) mLoadMoreView.findViewById(R.id.listview_foot_progress);
        TextView listviewFootMore = (TextView) mLoadMoreView.findViewById(R.id.listview_foot_more);

        if (visible != -1) {
            progressBar.setVisibility(visible);
        }
        if (!TextUtils.isEmpty(moreStr)) {
            listviewFootMore.setText(moreStr);
        }

        return mLoadMoreView;
    }


    protected boolean isLoadMorePosition(int position) {
        return mIsLoadMore && position == getCount() - 1;
    }

    /**
     * 设置加载颜色
     *
     * @param color
     */
    public void setFootBackground(int color) {
        getLoadMoreView(-1, null).setBackgroundColor(mContext.getResources().getColor(color));
    }


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
        return mListView;
    }

    /************************************************************************* abs memthod **********************************************************************/

    public abstract IBaseViewHolder createViewHoler(View parent, int viewType);

    public abstract int getConvertViewRsId(int viewType);
}
