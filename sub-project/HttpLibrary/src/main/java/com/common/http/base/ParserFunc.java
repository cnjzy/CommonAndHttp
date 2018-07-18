package com.common.http.base;

import android.text.TextUtils;
import android.util.Log;

import com.common.http.bean.ApiResult;
import com.common.http.callback.HttpCallback;
import com.google.gson.Gson;

import org.json.JSONObject;

import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * @author jzy
 * created at 2018/5/28
 */
public class ParserFunc<T> implements Function<ResponseBody, ApiResult<T>> {

    private final String TAG = "ParserFunc";
    private HttpCallback<T> mCallback;

    public ParserFunc(HttpCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public ApiResult<T> apply(ResponseBody responseBody) throws Exception {
        String json = responseBody.string();
//        Log.e(TAG, json);
        ApiResult<T> apiResult = new ApiResult<>();
        JSONObject jsonObject = new JSONObject(json);
        if (!jsonObject.isNull(ApiResult.CODE_NAME)) {
            apiResult.setCode(jsonObject.getInt(ApiResult.CODE_NAME));
        }
        if (!jsonObject.isNull(ApiResult.MSG_NAME)) {
            apiResult.setMsg(jsonObject.getString(ApiResult.MSG_NAME));
        }
        String data = "";
        if (!jsonObject.isNull(ApiResult.DATA_NAME)) {
            data = jsonObject.getString(ApiResult.DATA_NAME);
        }
        if (!TextUtils.isEmpty(data) && mCallback != null) {
            if (!apiResult.isOk()) {
                apiResult.setData(null);
            } else if (mCallback.getType().toString().indexOf(String.class.getName()) != -1) {
                apiResult.setData((T) data);
            } else {
                apiResult.setData((T) new Gson().fromJson(data, mCallback.getType()));
            }
        } else {
            apiResult.setData((T) data);
        }
        return apiResult;
    }
}
