package com.common.http.base;

//import com.library.data.InterceptLoadDataCallback;

import com.common.http.interceptor.InterceptLoadDataCallback;
import com.common.http.mgr.OkHttpClientMgr;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.library.data.remote.Result;
import com.library.data.source.LoadDataCallback;
import com.library.host.HostHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jzy on 2017/5/18.
 */

public class BaseRemoteLoader<T> {
    protected T mServiceApi = null;
    private static final String BASE_URL = "http://devnewsapi.zhuannews.cn/";//HostHelper.getInstance().getRunUrl();

    protected BaseRemoteLoader(Class<T> service) {
        this(service, BASE_URL, OkHttpClientMgr.CLIENT_API);
    }

    protected BaseRemoteLoader(Class<T> service, String url) {
        this(service, url, OkHttpClientMgr.CLIENT_API);
    }

    protected BaseRemoteLoader(Class<T> service, int clientType) {
        this(service, BASE_URL, clientType);
    }

    private BaseRemoteLoader(Class<T> service, String url, int clientType) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(OkHttpClientMgr.getIns().getClient(clientType))
                .build();
        mServiceApi = mRetrofit.create(service);
    }

    protected T getServiceApi() {
        return mServiceApi;
    }

    protected static class EnqueueCallback<T> implements Callback<Result<T>> {
        private LoadDataCallback<T> mCallback;

        public EnqueueCallback(LoadDataCallback<T> callback) {
            // 这里做拦截
            mCallback = new InterceptLoadDataCallback<>(callback);
        }

        @Override
        public void onResponse(Call<Result<T>> call, Response<Result<T>> response) {
            Result<T> result = response.body();
            if (result != null) {
                if (result.getCode() == Result.STATUS_OK) {
                    mCallback.onSuccess(result.getData());
                } else {
                    mCallback.onError(result.getCode(), result.getMsg());
                }
            } else {
                mCallback.onError(Result.STATUS_LOCAL_ERROR, "服务器出错");
            }
        }

        @Override
        public void onFailure(Call<Result<T>> call, Throwable throwable) {
            String errMsg = "";
            if (throwable != null) {
                errMsg = throwable.getMessage();
            }
            mCallback.onError(Result.STATUS_LOCAL_ERROR, errMsg);
        }
    }

    protected static class EnqueueCallback2<T> implements Callback<T> {
        private LoadDataCallback<T> mCallback;

        public EnqueueCallback2(LoadDataCallback<T> callback) {
            // 这里做拦截
            mCallback = new InterceptLoadDataCallback<>(callback);
        }

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            mCallback.onSuccess(response.body());
        }

        @Override
        public void onFailure(Call<T> call, Throwable throwable) {
            String errMsg = "";
            if (throwable != null) {
                errMsg = throwable.getMessage();
            }
            mCallback.onError(Result.STATUS_LOCAL_ERROR, errMsg);
        }
    }

    protected <B> void request(Call<Result<B>> call, LoadDataCallback<B> callback) {
        call.enqueue(new EnqueueCallback<B>(callback));
    }
}
