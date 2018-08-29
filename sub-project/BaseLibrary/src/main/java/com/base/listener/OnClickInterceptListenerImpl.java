package com.base.listener;

import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jzy on 2017/6/12.
 */

public class OnClickInterceptListenerImpl implements IOnClickListenerIntercept {
    // 用于过滤登录功能
    private static Map<Integer, Integer> mOnClickInterceptIds = new HashMap<>();


    public boolean onInterceptClick(final View v) {
        return false;
    }

    /**
     * 添加需要登录拦截的ID和类型
     *
     * @param id     需要拦截的ID
     * @param type   登录类型 UserUtils.LOGIN_LAUNCH_TYPE_DIALOG 弹窗 UserUtils.LOGIN_LAUNCH_TYPE_ACTIVITY
     * @param source 点击位置来源
     */
    public void addOnClickInterceptId(int id, int type, int source) {
        mOnClickInterceptIds.put(id, type);
    }
}
