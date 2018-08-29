package com.base.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.library.util.DeviceUtils;
import com.library.util.PhoneOSUtil;

public class StatusBarCompat {

    private static final String STATUS_BAR_TAG = "status_bar_tag";

    private int mColorResId = -1;
    private Activity mActivity = null;
    private ViewGroup mViewGroup = null;

    private int mListenerId = -1;

    public StatusBarCompat(Activity activity, ViewGroup viewGroup) {
        mActivity = activity;
        mViewGroup = viewGroup;
    }

    /**
     * 外部可直接调用，与皮肤无关；
     *
     * @param activity
     * @param rootView
     * @param colorResId
     */
    public static void setStatusBarColorRes(Activity activity, ViewGroup rootView, @ColorRes int colorResId) {
        if (activity == null) {
            return;
        }
        setStatusBarColor(activity, rootView, activity.getResources().getColor(colorResId));
    }

    /**
     * 外部可直接调用，与皮肤无关；
     *
     * @param activity
     * @param rootView
     * @param color
     */
    public static void setStatusBarColor(Activity activity, ViewGroup rootView, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStatusBarColorForLollipop(activity, color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setStatusBarColorForKitkat(activity, rootView, color);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void setStatusBarColorForKitkat(Activity activity, ViewGroup rootView, @ColorInt int color) {
        if (activity == null
                || rootView == null
                || DeviceUtils.hasSmartBar()) {
            return;
        }

        final Window window = activity.getWindow();
        if (window == null) {
            return;
        }

        final View decorView = window.getDecorView();
        if (decorView == null || !(decorView instanceof FrameLayout)) {
            return;
        }

        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        final FrameLayout frameLayout = (FrameLayout) decorView;
        View statusBarView = frameLayout.findViewWithTag(STATUS_BAR_TAG);
        if (statusBarView == null) {
            statusBarView = new View(activity);
            statusBarView.setTag(STATUS_BAR_TAG);
            addStatusBar(activity, rootView, frameLayout, statusBarView);
        }
        statusBarView.setBackgroundColor(color);
    }

    private static void addStatusBar(Activity activity, ViewGroup rootView, FrameLayout decorView, View statusBarView) {
        final int statusBarHeight = PhoneOSUtil.getStatusHeight(activity);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
        params.gravity = Gravity.TOP;
        statusBarView.setLayoutParams(params);
        decorView.addView(statusBarView);
        rootView.setFitsSystemWindows(true);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void setStatusBarColorForLollipop(Activity activity, @ColorInt int color) {
        if (activity == null) {
            return;
        }
        final Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        final int oldColor = window.getStatusBarColor();
        if (oldColor == color) {
            return;
        }
        window.setStatusBarColor(color);
    }

    /**
     * 全屏模式沉浸式，外部可直接调用
     *
     * @param activity
     */
    public static void setStatusBarFullScreen(Activity activity) {
        if (activity == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStatusBarFullScreenForLollipop(activity);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setStatusBarFullScreenForKitkat(activity);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    static void setStatusBarFullScreenForKitkat(Activity activity) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        ViewGroup mContentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
        View mContentChild = mContentView.getChildAt(0);
        if (mContentChild != null) {
            ViewCompat.setFitsSystemWindows(mContentChild, false);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void setStatusBarFullScreenForLollipop(Activity activity) {
        if (activity == null) {
            return;
        }
        final Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
            ViewCompat.requestApplyInsets(mChildView);
        }
    }

}
