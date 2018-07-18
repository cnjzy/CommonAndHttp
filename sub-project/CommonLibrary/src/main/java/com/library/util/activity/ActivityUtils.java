package com.library.util.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.library.bean.BaseBean;


/**
 * Created by jzy on 2017/5/25.
 */

public class ActivityUtils {
    public static final String BEAN = "bean";

    /**
     * 启动activity
     */
    public static void launchActivity(Context context, Intent intent) {
        context.startActivity(intent);
    }

    /**
     * 显示Activity
     *
     * @param context 上下文
     * @param c       类对象
     */
    public static void launchActivity(Context context, Class<?> c) {
        launchActivity(context, c, "");
    }

    /**
     * 显示Acitivty
     *
     * @param context 上下文
     * @param c       类对象
     * @param bean    实体类对象
     */
    public static void launchActivity(Context context, Class<?> c, String bean) {
        Bundle data = new Bundle();
        data.putString(BEAN, bean);
        launchActivity(context, c, data);
    }

    /**
     * 显示Acitivty
     *
     * @param context 上下文
     * @param c       类对象
     * @param bean    实体类对象
     */
    public static <T extends BaseBean> void launchActivity(Context context, Class<?> c, T bean) {
        Bundle data = new Bundle();
        data.putSerializable(BEAN, bean);
        launchActivity(context, c, data);
    }

    /**
     * 显示Activity
     *
     * @param context 上下文
     * @param cls     类对象
     * @param data    参数
     */
    public static void launchActivity(Context context, Class<?> cls, Bundle data) {
        if (data == null)
            data = new Bundle();
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtras(data);
        if(context instanceof Application){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        launchActivity(context, intent);
    }

    /**
     * 可以接受返回数据跳转Activity
     *
     * @param context     上下文
     * @param c           类对象
     * @param requestCode 请求code
     * @param data        参数
     */
    public static void launchActivityForResult(Activity context, Class<?> c, int requestCode, Bundle data) {
        if (data == null)
            data = new Bundle();
        Intent intent = new Intent();
        intent.setClass(context, c);
        intent.putExtras(data);
        context.startActivityForResult(intent, requestCode);
    }
}
