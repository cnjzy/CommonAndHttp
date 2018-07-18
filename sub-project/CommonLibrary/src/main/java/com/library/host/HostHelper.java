package com.library.host;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;

import com.library.bean.PuskDataBean;
import com.library.interfaces.IHostInfo;

public class HostHelper implements IHostInfo {
    private IHostInfo mIHostInfo = null;
    private String mRunUrl = "http://devnewsapi.zhuannews.cn/";

    public void setRunUrl(String runUrl) {
        mRunUrl = runUrl;
    }

    public String getRunUrl() {
        return mRunUrl;
    }

    private static class SingletonHolder {
        private static final HostHelper INSTANCE = new HostHelper();
    }

    private HostHelper() {
    }

    public static final HostHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void init(IHostInfo appInfo) {
        mIHostInfo = appInfo;
    }

    @Override
    public Context getAppContext() {
        if (mIHostInfo == null) {
            return null;
        }
        return mIHostInfo.getAppContext();
    }

    @Override
    public boolean isDebug() {
        if (mIHostInfo == null) {
            return false;
        }
        return mIHostInfo.isDebug();
    }

    @Override
    public int getNewsAdInsertInterval() {
        if (mIHostInfo == null) {
            return 5;
        }
        return mIHostInfo.getNewsAdInsertInterval();
    }

    @Override
    public String getUMengKey() {
        if (mIHostInfo == null) {
            return "5acefecff43e4814cf000011";
        }
        return mIHostInfo.getUMengKey();
    }

    @Override
    public String getUMengChannel() {
        if (mIHostInfo == null) {
            return "zhuannews";
        }
        return mIHostInfo.getUMengChannel();
    }

    @Override
    public Intent getClickNotificationIntent(PuskDataBean bean) {
        if (mIHostInfo == null) {
            return null;
        }
        return mIHostInfo.getClickNotificationIntent(bean);
    }

    @Override
    public int getNotificationSmallIconResId() {
        if (mIHostInfo == null) {
            return 0;
        }
        return mIHostInfo.getNotificationSmallIconResId();
    }

    @Override
    public void showPushDialog(Activity activity, PuskDataBean bean) {
        if (mIHostInfo == null || bean == null) {
            return;
        }
        mIHostInfo.showPushDialog(activity, bean);
    }

    @Override
    public void reportOpenApp() {
        if (mIHostInfo == null) {
            return;
        }
        mIHostInfo.reportOpenApp();
    }

    @Override
    public void reportShowNotification(String id) {
        if (mIHostInfo == null) {
            return;
        }
        mIHostInfo.reportShowNotification(id);
    }

    @Override
    public boolean checkEncryptWithUrl(String url) {
        if (mIHostInfo == null){
            return false;
        }
        return mIHostInfo.checkEncryptWithUrl(url);
    }
}
