package com.common.http.interceptor;

import com.library.data.source.LoadDataCallback;

/**
 * @author jzy
 * created at 2018/6/4
 */
public class InterceptLoadDataCallback<T> implements LoadDataCallback<T> {
    private LoadDataCallback<T> mCallback;

    public InterceptLoadDataCallback(LoadDataCallback<T> callback) {
        mCallback = callback;
    }

    public void onSuccess(T t) {
        if (mCallback != null) {
            mCallback.onSuccess(t);
        }
    }

    public void onError(int err, String msg) {
//        if (!InterceptPresenter.getInstance().onInterceptRequestError(err, msg)) {
        if (mCallback != null) {
            mCallback.onError(err, msg);
        }
//        }
    }
}