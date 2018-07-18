package com.common.http.download;

import android.content.Context;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class DownloadManager {
    private final OkHttpClient mClient;

    private DownloadManager() {
        mClient = new OkHttpClient();
    }

    private static class HOLDER {
        static final DownloadManager INS = new DownloadManager();
    }

    public static DownloadManager getInstance() {
        return HOLDER.INS;
    }

    /**
     * 下载
     *
     * @param url
     * @param listener
     */
    public void download(Context context, final String url, final IOnDownloadListener listener) {
        this.download(context, url, "", listener);
    }

    /**
     * 下载
     *
     * @param url
     * @param fileDirPath
     * @param listener
     */
    public synchronized void download(Context context, final String url, final String fileDirPath, final IOnDownloadListener listener) {
        Request request = new Request.Builder().url(url).build();
        mClient.newCall(request).enqueue(new DownloadCallback(context, listener, fileDirPath));
    }
}
