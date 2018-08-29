package com.base.ui.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.common.http.bean.BaseBean;
import com.base.ui.presenter.IBaseMvpPresenter;
import com.library.log.LogUtils;

/**
 * Created by jzy on 2017/6/13.
 * 列表型Activity基类，方便快速实现列表数据
 */

public abstract class BaseWebActivity<B extends BaseBean, P extends IBaseMvpPresenter> extends BaseActivity<P> {
    private WebView mWebView;
    private boolean mIsFullContent = false;

    @Override
    protected void loadExpandView() {
        super.loadExpandView();
        initListView();
        initListListener();
    }

    /**
     * 如果需要重新实现ListView的OnScrollListener需要把onScroll里面代码加上
     */
    protected void initListListener() {
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (mIsFullContent) {
                    measureWebViewHeight();
                }
            }
        });

    }


    private void initListView() {
        if (getWebViewId() > 0) {
            mWebView = (WebView) findViewById(getWebViewId());
            initWebViewSetting();
        }
    }

    private void initWebViewSetting() {
        if (mWebView == null) {
            return;
        }

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSavePassword(false);
        mWebView.getSettings().setBlockNetworkImage(false);
        mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mWebView.getSettings().setBuiltInZoomControls(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebView.getSettings().setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // 支持混合模式
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        //设置自适应屏幕，两者合用
        mWebView.getSettings().setUseWideViewPort(true); //将图片调整到适合webview的大小
        mWebView.getSettings().setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        mWebView.getSettings().setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        mWebView.getSettings().setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        mWebView.getSettings().setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        mWebView.getSettings().setAllowFileAccess(true); //设置可以访问文件
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        mWebView.getSettings().setLoadsImagesAutomatically(true); //支持自动加载图片
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");//设置编码格式

    }

    private void measureWebViewHeight() {
        //重新调整webview高度
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                mWebView.measure(0, 0);
                int measuredHeight = mWebView.getMeasuredHeight();
                ViewGroup.LayoutParams layoutParams = mWebView.getLayoutParams();
                layoutParams.height = measuredHeight;
                mWebView.setLayoutParams(layoutParams);
                LogUtils.e(TAG, "webview content height " + measuredHeight);
            }
        });

    }

    protected void setShowFullContent(boolean isFull) {
        mIsFullContent = isFull;
    }

    public WebView getWebView() {
        return mWebView;
    }

    @SuppressLint("JavascriptInterface")
    public void addJavascriptInterface(Object object, String name) {
        if (mWebView != null) {
            mWebView.addJavascriptInterface(object, name);
        }
    }

    public abstract int getWebViewId();
}
