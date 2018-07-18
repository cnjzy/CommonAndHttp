package com.common.http.imageloader;

import android.util.Base64;
import android.webkit.WebView;


import com.common.http.mgr.OkHttpClientMgr;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jzy on 2016/9/28.
 */

public class WebViewImageLoader {
    private WebView mWebView;
    private OkHttpClient mClient;
    private final List<Call> mCalls = new ArrayList<>();

    public WebViewImageLoader(WebView webView) {
        mWebView = webView;
        mClient = OkHttpClientMgr.getIns().getClient(OkHttpClientMgr.CLIENT_IMAGE_REQUEST);
    }

    public void release() {
        mWebView = null;
        mClient = null;
        synchronized (mCalls) {
            for (Call call : mCalls) {
                call.cancel();
            }
            mCalls.clear();
        }
    }

    public void loadImage(String imgSrc, String callBackDom) {
        Request request = new Request.Builder().url(imgSrc).build();
        Call call = mClient.newCall(request);
        synchronized (mCalls) {
            mCalls.add(call);
        }
        call.enqueue(new LoadImageCallback(this, callBackDom));
    }

    private static final class LoadImageCallback implements Callback {
        private SoftReference<WebViewImageLoader> mImageLoaderRef;
        private String mCallBackDom;

        LoadImageCallback(WebViewImageLoader loader, final String callBackDom) {
            mImageLoaderRef = new SoftReference<>(loader);
            mCallBackDom = callBackDom;
        }

        @Override
        public void onFailure(Call call, IOException e) {
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            WebViewImageLoader imageLoader = mImageLoaderRef.get();
            final WebView webView;
            if (imageLoader == null || (webView = imageLoader.mWebView) == null) {
                return;
            }
            try {
                String base64Img = Base64.encodeToString(response.body().bytes(), Base64.NO_WRAP);
                final String url = String.format(Locale.CHINA, "javascript:setDomImg('%s', 'data:image/gif;base64,%s')", mCallBackDom, base64Img);
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            webView.loadUrl(url);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

}
