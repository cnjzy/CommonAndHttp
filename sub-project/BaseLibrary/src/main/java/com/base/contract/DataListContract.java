package com.base.contract;


import com.base.ui.presenter.IBaseMvpPresenter;
import com.base.ui.view.IBaseMvpView;

import java.util.List;

/**
 * Created by jzy on 2017/6/12.
 */

public interface DataListContract {
    interface IDataListView<B> extends IBaseMvpView<DataListContract.Presenter> {

        /**
         * 初始化数据成功
         *
         * @param beanList
         */
        void onInitSuccess(List<B> beanList, boolean isCache);

        /**
         * 添加数据
         *
         * @param bean
         */
        void addData(B bean);

        /**
         * 添加数据
         *
         * @param beanList
         */
        void addData(List<B> beanList);

        /**
         * 清空数据
         */
        void clearData();

        /**
         * 显示空结果页
         */
        void showEmptyView();

        /**
         * 最后的数据
         */
        void isNotMoreData();
    }


    interface Presenter<V extends DataListContract.IDataListView> extends IBaseMvpPresenter<V> {
        /**
         * 初始化数据&下拉刷新
         *
         * @param isShowLoading
         */
        void initData(boolean isShowLoading);

        /**
         * 加载更多
         */
        void loadMore();

        /**
         * 是否可加载
         *
         * @return
         */
        boolean isLoadMore();
    }
}
