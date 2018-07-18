package com.common.http.common;

import android.text.TextUtils;

import com.common.http.base.BaseRemoteLoader;
import com.common.http.base.BaseSubscribe;
import com.common.http.base.ParserFunc;
import com.common.http.bean.BaseReq;
import com.common.http.callback.HttpCallback;
import com.common.http.interfaces.IApiCode;
import com.common.http.util.RSAUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author jzy
 * created at 2018/7/11
 */
public abstract class BaseCommonRemoteLoader extends BaseRemoteLoader<ICommonApi> implements ICommonSource {
    public BaseCommonRemoteLoader() {
        super(ICommonApi.class);
    }

    public BaseCommonRemoteLoader(String url) {
        super(ICommonApi.class, url);
    }

    public BaseCommonRemoteLoader(int clientType) {
        super(ICommonApi.class, clientType);
    }

    /********************************************************** single end **********************************************************/

    @Override
    public void postWithForm(BaseReq req, HttpCallback callback) {
        if (req == null || TextUtils.isEmpty(req.getUrlPath())) {
            if (callback != null) {
                callback.onError(IApiCode.HTTP_CODE_FAIL_EMPTY, new Throwable("req or url path is empty"));
            }
            return;
        }

        String urlPath = req.getUrlPath();
        String data = req.toString();
        request(getServiceApi().postWithForm(urlPath, data), callback);
    }

    @Override
    public void post(BaseReq req, HttpCallback callback) {
        post(req, callback, false);
    }

    @Override
    public void post(BaseReq req, HttpCallback callback, boolean isEncrypt) {
        if (req == null || TextUtils.isEmpty(req.getUrlPath())) {
            if (callback != null) {
                callback.onError(IApiCode.HTTP_CODE_FAIL_EMPTY, new Throwable("req or url path is empty"));
            }
            return;
        }

        String urlPath = req.getUrlPath();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (isEncrypt ? RSAUtils.encryptByPublic(req.toString()) : req.toString()));
        request(getServiceApi().post(urlPath, body), callback);
    }

    private void request(Observable observable, HttpCallback callback) {
        try {
            observable.compose(schedulersTransformer())
                    .map(new ParserFunc(callback))
                    .subscribe(new BaseSubscribe(callback));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /********************************************************** request end **********************************************************/

    /**
     * 设置执行线程
     *
     * @return
     */
    protected ObservableTransformer schedulersTransformer() {
        return new ObservableTransformer() {
            @Override
            public ObservableSource apply(Observable upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /********************************************************** util end **********************************************************/
}
