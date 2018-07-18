package com.common.http.common;

import android.text.TextUtils;

import com.common.http.base.BaseRemoteLoader;
import com.common.http.base.BaseSubscribe;
import com.common.http.base.ParserFunc;
import com.common.http.bean.BaseReq;
import com.common.http.callback.HttpCallback;
import com.common.http.interfaces.IApiCode;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author jzy
 * created at 2018/5/28
 */
public class CommonRemoteLoader extends BaseCommonRemoteLoader {

    private CommonRemoteLoader() {

    }

    private static class HOLDER {
        static final CommonRemoteLoader INS = new CommonRemoteLoader();
    }

    public static ICommonSource getInstance() {
        return HOLDER.INS;
    }
}
