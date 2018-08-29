package com.base.contract;

import java.util.List;

/**
 * Created by jzy on 2018/3/29.
 */

public interface IDataListWrap<B> {
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
