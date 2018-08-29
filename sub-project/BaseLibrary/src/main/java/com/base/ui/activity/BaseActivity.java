package com.base.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;

import com.base.R;
import com.base.listener.IOnClickListenerIntercept;
import com.base.listener.OnClickInterceptListenerImpl;
import com.base.ui.presenter.IBaseMvpPresenter;
import com.base.ui.presenter.PresenterFactory;
import com.base.ui.view.IBaseMvpView;
import com.base.ui.view.ITipBaseUI;
import com.base.ui.view.IViewHolderWrap;
import com.base.ui.view.IViewInit;
import com.base.ui.view.ViewHolderWrap;
import com.base.util.StatusBarCompat;
import com.base.widget.DefaultTipUI;
import com.library.log.LogUtils;
import com.library.util.DeviceUtils;
import com.library.util.EventBusUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * Created by jzy on 2017/5/17.
 */

public abstract class BaseActivity<P extends IBaseMvpPresenter> extends FragmentActivity implements View.OnClickListener, ITipBaseUI, IViewInit, IBaseMvpView<P>, IViewHolderWrap {
    protected Context mContext = this;
    protected Handler mHandler = new Handler();
    public final String TAG = getClass().getSimpleName();
    private ITipBaseUI mTipBaseUI = null;
    private IOnClickListenerIntercept mLoginIntercept = null;

    private int activityCloseEnterAnimation = 0;
    private int activityCloseExitAnimation = 0;

    protected ViewGroup mRootView = null;
    protected StatusBarCompat mStatusBarCompat = null;

    private IBaseMvpPresenter mPresenter = null;

    // 用于过滤点击功能
    private Map<Integer, Integer> mOnClickInterceptIds = new HashMap<>();

    private ViewHolderWrap mViewHolderWrap = null;


    protected void printLog(String content) {
        LogUtils.e(TAG, content);
    }

    @Override
    public void showToast(final String content, final boolean showInRelease) {
        runOnUiThread(new Runnable() {
            public void run() {
                mTipBaseUI.showToast(content, showInRelease);
            }
        });
    }

    @Override
    public void showTipDialog(final String title, final String content, final TipCallback callback) {
        runOnUiThread(new Runnable() {
            public void run() {
                mTipBaseUI.showTipDialog(title, content, callback);
            }
        });
    }

    @Override
    public void showLoadingDialog() {
        if (!DefaultTipUI.isDestroy(mContext))
            runOnUiThread(new Runnable() {
                public void run() {
                    if (!DefaultTipUI.isDestroy(mContext))
                        mTipBaseUI.showLoadingDialog();
                }
            });
    }

    @Override
    public void hideLoadingDialog() {
        runOnUiThread(new Runnable() {
            public void run() {
                mTipBaseUI.hideLoadingDialog();
            }
        });
    }

    @Override
    public void onTipDestroy() {
        mTipBaseUI.onTipDestroy();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        printLog("===================" + getClass().getSimpleName());
        if (getContentViewRsId() > 0) {
            setContentView(getContentViewRsId());
        }
        ButterKnife.bind(this);
        registerEventBus();
        initViewHolderWrap();
        createPresenter();
        initTipUI();
        getActivityAnimation();
        initIntercept();
        init();
        loadExpandView();
        initView();
        initListener();
        setViewsValue();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onTipDestroy();
        unregisterEventBus();
        destroyPresenter();
        if (getCurrentFocus() != null) {
            DeviceUtils.hideIMM(mContext, getCurrentFocus());
        }
    }

    @Override
    public void finish() {
        super.finish();
//        if (activityCloseEnterAnimation != 0 && activityCloseExitAnimation != 0) {
        overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);
//        }
    }

    @Override
    public final void onClick(final View v) {
        // 点击按钮后隐藏软键盘
        if (getCurrentFocus() != null) {
            DeviceUtils.hideIMM(mContext, getCurrentFocus());
        }

        if (mLoginIntercept.onInterceptClick(v)) {
            return;
        }
        onClickDispatch(v);

    }

    @SuppressLint("ResourceType")
    private void getActivityAnimation() {
        TypedArray activityStyle = getTheme().obtainStyledAttributes(new int[]{android.R.attr.windowAnimationStyle});
        if (activityStyle != null) {
            int windowAnimationStyleResId = activityStyle.getResourceId(0, 0);
            activityStyle.recycle();
            activityStyle = getTheme().obtainStyledAttributes(windowAnimationStyleResId, new int[]{android.R.attr.activityCloseEnterAnimation, android.R.attr.activityCloseExitAnimation});
            activityCloseEnterAnimation = activityStyle.getResourceId(0, 0);
            activityCloseExitAnimation = activityStyle.getResourceId(1, 0);
            activityStyle.recycle();
        }
    }

    public abstract void onClickDispatch(View v);

    /**
     * 添加需要登录拦截的ID和类型
     *
     * @param id   需要拦截的ID
     * @param type 登录类型 UserUtils.LOGIN_LAUNCH_TYPE_DIALOG 弹窗 UserUtils.LOGIN_LAUNCH_TYPE_ACTIVITY
     */
    protected void addOnClickLoginInterceptId(int id, int type, int source) {
        mLoginIntercept.addOnClickInterceptId(id, type, source);
    }

    private void initIntercept() {
        mLoginIntercept = new OnClickInterceptListenerImpl();
    }

    private void initTipUI() {
        mTipBaseUI = new DefaultTipUI(mContext);
    }

    /**
     * 加载拓展view，用于拓展使用
     */
    protected void loadExpandView() {

    }

    protected void clearActivityExitAnimation() {
        activityCloseEnterAnimation = 0;
        activityCloseExitAnimation = 0;
    }

    protected void initStatusBar() {
        if (mRootView == null) {
            mRootView = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        }
        if (mRootView == null) {
            return;
        }

        if (null != mStatusBarCompat) {
            // 已经初始化过了
            return;
        }

        mStatusBarCompat = new StatusBarCompat(this, mRootView);
        StatusBarCompat.setStatusBarColorRes(this, mRootView, R.color.status_bar_color);
    }

    @Override
    public void createPresenter() {
        try {
            mPresenter = PresenterFactory.bind(getClass()).create();
            if (mPresenter != null) {
                mPresenter.attach(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public P getPresenter() {
        return (P) mPresenter;
    }

    @Override
    public void destroyPresenter() {
        if (mPresenter != null) {
            mPresenter.detach();
        }
    }

    private void registerEventBus() {
        EventBus.getDefault().register(this); // 注册bus
    }

    private void unregisterEventBus() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 1)
    public void onMessageEventMain(EventBusUtils.Events event) {
    }

    private void initViewHolderWrap() {
        View rootView = (View) findViewById(android.R.id.content);
        if (rootView == null) {
            rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        } else if (rootView instanceof ViewGroup && ((ViewGroup) rootView).getChildCount() > 0) {
            rootView = ((ViewGroup) rootView).getChildAt(0);
        }
        mViewHolderWrap = new ViewHolderWrap(mContext, rootView);
    }

    @Override
    public <V extends View> V findViewById(View view, int id) {
        return mViewHolderWrap.findViewById(view, id);
    }

    @Override
    public IViewHolderWrap setText(int viewId, int textResId) {
        mViewHolderWrap.setText(viewId, textResId);
        return this;
    }

    @Override
    public IViewHolderWrap setText(int viewId, String text) {
        mViewHolderWrap.setText(viewId, text);
        return this;
    }

    @Override
    public IViewHolderWrap setOnClickListener(int viewId, View.OnClickListener listener) {
        mViewHolderWrap.setOnClickListener(viewId, listener);
        return this;
    }

    @Override
    public IViewHolderWrap setBackground(int viewId, int resId) {
        mViewHolderWrap.setBackground(viewId, resId);
        return this;
    }

    @Override
    public IViewHolderWrap setBackground(int viewId, Drawable drawable) {
        mViewHolderWrap.setBackground(viewId, drawable);
        return this;
    }

    @Override
    public IViewHolderWrap setImageResource(int viewId, int resId) {
        mViewHolderWrap.setImageResource(viewId, resId);
        return this;
    }

    @Override
    public IViewHolderWrap setImageBitmap(int viewId, Bitmap bitmap) {
        mViewHolderWrap.setImageBitmap(viewId, bitmap);
        return null;
    }

    @Override
    public IViewHolderWrap setImageDrawable(int viewId, Drawable drawable) {
        mViewHolderWrap.setImageDrawable(viewId, drawable);
        return this;
    }

    @Override
    public IViewHolderWrap setImageWithUrl(int viewId, String url) {
        mViewHolderWrap.setImageWithUrl(viewId, url);
        return this;
    }

    @Override
    public IViewHolderWrap setViewVisible(int viewId, int visible) {
        mViewHolderWrap.setViewVisible(viewId, visible);
        return this;
    }
}
