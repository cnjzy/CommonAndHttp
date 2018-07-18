package com.common.http.base;

import android.util.Log;

import com.common.http.bean.ApiResult;
import com.common.http.callback.HttpCallback;
import com.common.http.interfaces.IApiCode;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author jzy
 * created at 2018/5/28
 */
public class BaseSubscribe<T> implements Observer<ApiResult<T>> {

    private HttpCallback mCallback;

    public BaseSubscribe(HttpCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onNext(ApiResult<T> tApiResult) {
        Log.i("http", tApiResult.getCode() + " " + tApiResult.getMsg() + " " + tApiResult.getData());
        if (mCallback == null) {
            return;
        }
        if (tApiResult == null) {
            mCallback.onError(ApiResult.HTTP_CODE_FAIL_EMPTY, new Throwable("result is empty"));
        } else if (!tApiResult.isOk()) {
            mCallback.onError(tApiResult.getCode(), new Throwable(tApiResult.getMsg()));
        } else if (mCallback != null) {
            mCallback.onSuccess(tApiResult.getData());
        }
    }

    @Override
    public void onError(Throwable e) {
        Log.e("http", e.toString());
        if (mCallback != null) {
            mCallback.onError(IApiCode.HTTP_CODE_FAIL_SERVER, e);
        }
    }

    @Override
    public void onComplete() {
        Log.e("http", "onComplete");
    }
}
