package com.common.http.common;


import com.common.http.bean.BaseReq;
import com.common.http.callback.HttpCallback;

/**
 * @author jzy
 * created at 2018/5/28
 */
public interface ICommonSource {
    void postWithForm(BaseReq req, HttpCallback callback);

    void post(BaseReq req, HttpCallback callback);

    void post(BaseReq req, HttpCallback callback, boolean isEncrypt);
}
