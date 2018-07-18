package com.library.interfaces;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;

import com.library.bean.PuskDataBean;


/**
 * Created by Administrator on 2017/10/25.
 */

public interface IHostInfo {
    Context getAppContext();

    boolean isDebug();

    int getNewsAdInsertInterval();

    String getUMengKey();

    String getUMengChannel();

    Intent getClickNotificationIntent(PuskDataBean bean);

    int getNotificationSmallIconResId();

    void showPushDialog(Activity activity, PuskDataBean bean);

    void reportOpenApp();

    void reportShowNotification(String id);

    boolean checkEncryptWithUrl(String url);
}
