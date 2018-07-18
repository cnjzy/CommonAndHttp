package com.common.http.mgr;


import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.common.http.interceptor.HttpLoggingInterceptor;
import com.common.http.util.HttpLog;
import com.common.http.util.HttpUtil;
import com.common.http.util.NetworkUtil;
import com.library.host.HostHelper;
import com.library.log.LogUtils;
import com.library.util.DeviceUtils;
import com.library.util.PreferencesUtils;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.http.CallServerInterceptor;
import okhttp3.internal.http.HttpCodec;
import okhttp3.internal.http.RealInterceptorChain;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.http.Body;

/**
 * Created by jzy on 2016/10/18.
 */

public class OkHttpClientMgr {
    private final String TAG = "OkHttpClientMgr";

    private static final long FILE_READ_TIME_MIN = 5L;
    private static final long FILE_WRITE_TIME_MIN = 5L;
    private static final long DEFAULT_CONNECT_TIME_SEC = 10L;
    private static final long FILE_CONNECT_TIME_SEC = 100L;

    private static class Inner {
        private static final OkHttpClientMgr mIns = new OkHttpClientMgr();
    }

    private SparseArray<OkHttpClient> mClients = new SparseArray<>(CLIENT_COUNT);

    private OkHttpClientMgr() {
    }

    public static OkHttpClientMgr getIns() {
        return Inner.mIns;
    }

    public
    @NonNull
    OkHttpClient getClient() {
        OkHttpClient client = mClients.get(CLIENT_DEFAULT);
        if (client == null) {
            initClient(CLIENT_DEFAULT, null);
        }
        return mClients.get(CLIENT_DEFAULT);
    }

    public void initClient(@Client int client, ClientBuilder builder) {
        OkHttpClient.Builder defaultBuilder = null;
        if (client == CLIENT_DEFAULT) {
            defaultBuilder = new OkHttpClient.Builder().connectTimeout(DEFAULT_CONNECT_TIME_SEC, TimeUnit.SECONDS);
        } else if (client == CLIENT_FILE) {
            defaultBuilder = getClient().newBuilder().connectTimeout(FILE_CONNECT_TIME_SEC, TimeUnit.SECONDS).writeTimeout(FILE_WRITE_TIME_MIN, TimeUnit.MINUTES).readTimeout(FILE_READ_TIME_MIN, TimeUnit.MINUTES);
        } else {
            defaultBuilder = getClient().newBuilder();
        }

//        defaultBuilder.sslSocketFactory(SSLSocketClient.getInstance().getSslSocketFactory(), SSLSocketClient.getInstance().getTrustManager());

        OkHttpClient httpClient = createClient(defaultBuilder, builder);
        mClients.put(client, httpClient);
    }

    public String getTimeStamp() {
        String timeStamp = System.currentTimeMillis() + "";
        getCommonHeader().put("timeStamp", timeStamp);
        return timeStamp;
    }

    private Map<String, String> getCommonHeader() {
        Map<String, String> headers = new HashMap<>();
        headers.put("versionCode", DeviceUtils.getVersionCode(HostHelper.getInstance().getAppContext()) + "");
        headers.put("version", DeviceUtils.getVersionName(HostHelper.getInstance().getAppContext()) + "");
        headers.put("channel", DeviceUtils.getChannelName(HostHelper.getInstance().getAppContext(), "UMENG_CHANNEL"));
        headers.put("imei", DeviceUtils.getImei(HostHelper.getInstance().getAppContext()));
        headers.put("platform", "2");
        headers.put("token", PreferencesUtils.getInstance().getString("save_token", ""));
        headers.put("androidId", DeviceUtils.getAndroidID(HostHelper.getInstance().getAppContext()));
        headers.put("mac", DeviceUtils.getMacAddress(HostHelper.getInstance().getAppContext()));
        headers.put("model", Build.MODEL);
        headers.put("vendor", Build.BRAND);
        headers.put("screenWidth", DeviceUtils.getScreenPx(HostHelper.getInstance().getAppContext()).width() + "");
        headers.put("screenHeight", DeviceUtils.getScreenPx(HostHelper.getInstance().getAppContext()).height() + "");
        headers.put("operatorType", NetworkUtil.getNetworkOpera(HostHelper.getInstance().getAppContext()) + "");
        headers.put("connectionType", NetworkUtil.getNetworkType(HostHelper.getInstance().getAppContext()) + "");
        headers.put("deviceType", (DeviceUtils.isTabletDevice(HostHelper.getInstance().getAppContext()) ? 2 : 1) + "");
        headers.put("ipv4", DeviceUtils.getLocalIpAddress(HostHelper.getInstance().getAppContext()) + "");
        headers.put("osVersion", Build.VERSION.RELEASE);
        headers.put("appPackage", HostHelper.getInstance().getAppContext().getPackageName());
        headers.put("appName", "zhuannews");
        return headers;
    }

    private OkHttpClient createClient(OkHttpClient.Builder builder, ClientBuilder clientBuilder) {
        if (clientBuilder != null) {
            clientBuilder.buildClient(builder);
        }
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Map<String, String> headers = getCommonHeader();
                Request.Builder builder = chain.request().newBuilder();
                if (headers != null && headers.size() > 0) {
                    try {
                        for (Map.Entry<String, String> entry : headers.entrySet()) {
                            builder.header(entry.getKey(), entry.getValue()).build();
                        }
                    } catch (Exception e) {
                        HttpLog.e(e);
                    }
                }
                Request request = chain.request();
                String url = request.url().toString();

                if (HostHelper.getInstance().checkEncryptWithUrl(url)) {
                    RequestBody newBody = HttpUtil.encryptRequestBody(request.body());
                    if (newBody == null) {
                        return chain.proceed(builder.build());
                    }

                    Request.Builder requestBuilder = request.newBuilder();
                    requestBuilder.method(request.method(), newBody);
                    return chain.proceed(requestBuilder.build());
                } else {
                    return chain.proceed(builder.build());
                }
            }
        });
        builder.addInterceptor(new HttpLoggingInterceptor(TAG));
        return builder.build();
    }

    public
    @NonNull
    OkHttpClient getClient(@Client int client) {
        OkHttpClient httpClient = mClients.get(client);
        if (httpClient == null) {
            initClient(client, null);
            httpClient = mClients.get(client);
            if (httpClient == null) {
                return getClient();
            }
        }
        return httpClient;
    }

    private static final int CLIENT_COUNT = 4;
    public static final int CLIENT_DEFAULT = 0;
    public static final int CLIENT_API = 1;
    public static final int CLIENT_IMAGE_REQUEST = 2;
    public static final int CLIENT_FILE = 3;

    @IntDef({CLIENT_DEFAULT, CLIENT_API, CLIENT_IMAGE_REQUEST, CLIENT_FILE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Client {
    }

    public interface ClientBuilder {
        void buildClient(OkHttpClient.Builder builder);
    }

}
