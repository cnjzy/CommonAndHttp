package com.base.ui.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.listener.IOnClickListenerIntercept;
import com.base.listener.OnClickInterceptListenerImpl;
import com.base.ui.presenter.IBaseMvpPresenter;
import com.base.ui.presenter.PresenterFactory;
import com.base.ui.view.IBaseMvpView;
import com.base.ui.view.ITipBaseUI;
import com.base.ui.view.IViewHolderWrap;
import com.base.ui.view.IViewInit;
import com.base.ui.view.ViewHolderWrap;
import com.base.widget.DefaultTipUI;
import com.library.util.DeviceUtils;


/**
 * Created by jzy on 2017/5/25.
 */

public abstract class BaseFragment<P extends IBaseMvpPresenter> extends Fragment implements View.OnClickListener, ITipBaseUI, IViewInit, IBaseMvpView<P>, IViewHolderWrap {
    protected String TAG = getClass().getSimpleName();
    private ITipBaseUI mTipBaseUI;
    protected View mRootView;
    protected Context mContext;
    private IOnClickListenerIntercept mLoginIntercept;

    private IBaseMvpPresenter mPresenter = null;

    protected Handler mHandler = new Handler();

    /**
     * 是否可见
     */
    protected boolean mIsVisible = false;
    /**
     * 视图是否准备好，用于防止视图可见调用加载时视图未初始化造成空指针
     */
    protected boolean mIsPrepared = false;

    private ViewHolderWrap mViewHolderWrap = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        initTipUI();
        init();
        initIntercept();
        createPresenter();
    }

    private void initIntercept() {
        mLoginIntercept = new OnClickInterceptListenerImpl();
    }

    private void initTipUI() {
        mTipBaseUI = new DefaultTipUI(getContext());
    }

    @Override
    public void showToast(final String content, final boolean showInRelease) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTipBaseUI.showToast(content, showInRelease);
                }
            });
        }

    }

    @Override
    public void showTipDialog(final String title, final String content, final TipCallback callback) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTipBaseUI.showTipDialog(title, content, callback);
                }
            });
        }
    }

    @Override
    public void showLoadingDialog() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTipBaseUI.showLoadingDialog();
                }
            });
        }
    }

    @Override
    public void hideLoadingDialog() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTipBaseUI.hideLoadingDialog();
                }
            });
        }
    }

    @Override
    public void onTipDestroy() {
        mTipBaseUI.onTipDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null && getContentViewRsId() > 0) {
            mRootView = inflater.inflate(getContentViewRsId(), container, false);
            if (mRootView.getParent() != null) {
                ((ViewGroup) mRootView.getParent()).removeView(mRootView);
            }
            initViewHolderWrap();
            loadExpandView();
            initView();
            setPrepared(true);
            initListener();
            setViewsValue();
        }
        return mRootView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        onTipDestroy();
    }

    @Override
    public void onClick(View v) {
        // 关闭键盘
        if (getActivity() != null) {
            if (getActivity().getCurrentFocus() != null) {
                DeviceUtils.hideIMM(getContext(), getActivity().getCurrentFocus());
            }
        }
        if (mLoginIntercept.onInterceptClick(v)) {
            return;
        }
        onClickDispatch(v);
    }

    public abstract void onClickDispatch(View v);


    /**
     * 添加需要登录拦截的ID和类型
     *
     * @param id   需要拦截的ID
     * @param type 登录类型 UserUtils.LOGIN_LAUNCH_TYPE_DIALOG 弹窗 UserUtils.LOGIN_LAUNCH_TYPE_ACTIVITY
     */
    protected void addLoginInterceptId(int id, int type, int source) {
        mLoginIntercept.addOnClickInterceptId(id, type, source);
    }


    /**
     * 获取视图
     *
     * @param rsId
     * @param <T>
     * @return
     */
    protected <T extends View> T findViewById(int rsId) {
        if (mRootView == null || rsId < 1) {
            return null;
        }
        return (T) mRootView.findViewById(rsId);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            mIsVisible = true;
            onVisible();
        } else {
            mIsVisible = false;
            onInvisible();
        }
    }

    public void setPrepared(boolean mIsPrepared) {
        this.mIsPrepared = mIsPrepared;
    }

    /**
     * 是否可以使用懒加载，调用懒加载前需要调用
     *
     * @return
     */
    protected boolean isCanLazyLoad() {
        return mIsVisible && mIsPrepared;
    }

    /**
     * 显示fragment
     */
    protected void onVisible() {
        if (isCanLazyLoad()) {
            lazyLoad();
        }
    }

    /**
     * 懒加载方法
     */
    protected abstract void lazyLoad();

    /**
     * 隐藏fragment
     */
    protected void onInvisible() {
    }

    /**
     * 加载拓展view，用于拓展使用
     */
    protected void loadExpandView() {

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

    public static <F extends BaseFragment> F getInstance(Class<F> fClass) {
        return getInstance(fClass, null);
    }

    public static <F extends BaseFragment> F getInstance(Class<F> fClass, Bundle args) {
        F f = null;
        try {
            f = fClass.newInstance();
            f.setArguments(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    private void initViewHolderWrap() {
        mViewHolderWrap = new ViewHolderWrap(mContext, mRootView);
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
