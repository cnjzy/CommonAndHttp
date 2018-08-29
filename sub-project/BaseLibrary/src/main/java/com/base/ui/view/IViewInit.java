package com.base.ui.view;

/**
 * Created by jzy on 2017/6/12.
 * View初始化接口类
 */

public interface IViewInit {
    /**
     * 获取布局资源ID
     *
     * @return
     */
    abstract int getContentViewRsId();

    /**
     * 初始化
     * 初始化变量成员、获取intent的参数值
     */
    abstract void init();

    /**
     * 初始化view
     */
    abstract void initView();

    /**
     * 初始化事件监听
     */
    abstract void initListener();

    /**
     * 初始化视图数据
     */
    abstract void setViewsValue();
}
