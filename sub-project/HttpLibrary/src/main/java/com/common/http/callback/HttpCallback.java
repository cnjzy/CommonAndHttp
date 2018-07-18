package com.common.http.callback;

/**
 * @author jzy
 * created at 2018/5/26
 */
public abstract class HttpCallback<T> extends CallbackType {
    public abstract void onError(int code, Throwable e);

    public abstract void onSuccess(T t);
}
