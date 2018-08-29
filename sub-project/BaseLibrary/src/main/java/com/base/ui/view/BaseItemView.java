package com.base.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.base.listener.IOnClickListenerIntercept;
import com.base.listener.OnClickInterceptListenerImpl;
import com.base.widget.DefaultTipUI;


/**
 * Created by jzy on 2017/5/26.
 * 所有自定义itemview基类
 */

public abstract class BaseItemView<T> extends LinearLayout implements ITipBaseUI, View.OnClickListener, IViewInit {

    private ITipBaseUI mTipUI;
    private IOnClickListenerIntercept mLoginIntercept;

    public BaseItemView(Context context) {
        super(context);
        init();
    }

    public BaseItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        initIntercept();
        initTipUI();
        if (getContentViewRsId() > 0) {
            View.inflate(getContext(), getContentViewRsId(), this);
        }
        initView();
        initListener();
        setViewsValue();
    }

    @Override
    public final void onClick(View v) {
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

    private void initIntercept(){
        mLoginIntercept = new OnClickInterceptListenerImpl();
    }

    private void initTipUI() {
        mTipUI = new DefaultTipUI(getContext());
    }

    @Override
    public void showToast(String content, boolean showInRelease) {
        mTipUI.showToast(content, showInRelease);
    }

    @Override
    public void showTipDialog(String title, String content, TipCallback callback) {
        mTipUI.showTipDialog(title, content, callback);
    }

    @Override
    public void showLoadingDialog() {
        mTipUI.showLoadingDialog();
    }

    @Override
    public void hideLoadingDialog() {
        mTipUI.hideLoadingDialog();
    }

    @Override
    public void onTipDestroy() {
        mTipUI.onTipDestroy();
    }

    public abstract void bindData(int position, T t, Object... args);


}
