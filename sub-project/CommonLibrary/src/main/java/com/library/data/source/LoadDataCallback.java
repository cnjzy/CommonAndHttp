package com.library.data.source;

/**
 * @author jzy
 * created at 2018/6/4
 */
public interface LoadDataCallback<T> {
    public void onSuccess(T t);
    public void onError(int err, String msg);
}
