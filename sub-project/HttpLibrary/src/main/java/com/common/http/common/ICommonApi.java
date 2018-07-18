package com.common.http.common;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * @author jzy
 * created at 2018/5/28
 */
public interface ICommonApi {
    /**
     * 表单post
     *
     * @param url
     * @param data
     * @return
     */
    @FormUrlEncoded
    @POST
    Observable<ResponseBody> postWithForm(@Url String url, @Field("data") String data);

    /**
     * post
     *
     * @param url
     * @param data
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST
    Observable<ResponseBody> post(@Url String url, @Body RequestBody data);
}
