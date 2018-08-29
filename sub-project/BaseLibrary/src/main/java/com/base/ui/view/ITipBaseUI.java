package com.base.ui.view;

/**
 * Created by jzy on 2017/5/25.
 * 提示窗口接口类
 */

public interface ITipBaseUI {
    /**
     * 显示Toast窗口
     * @param content
     */
    public void showToast(String content, boolean showInRelease);

    /**
     * 显示dialog提示窗口
     * @param title
     * @param content
     * @param callback
     */
    public void showTipDialog(String title, String content, TipCallback callback);

    /**
     * 显示加载提示窗口
     */
    public void showLoadingDialog();

    /**
     * 隐藏加载提示窗口
     */
    public void hideLoadingDialog();

    /**
     * 释放资源方法
     * 由于dialog在activity退出时需要cancel掉，否则会报异常，所以增加此方法释放dialog资源
     */
    public void onTipDestroy();

    public interface TipCallback{
        public void onTipCallback(int which, Object obj);
    }
}
