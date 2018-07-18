package com.common.http.download;

/**
 * @author jzy
 * created at 2018/5/28
 */
public interface IOnDownloadListener {
    /**
     * 下载成功
     */
    void onDownloadSuccess(String filePath);

    /**
     * 下载进度
     *
     * @param progress
     * @param total
     * @param percentage
     */
    void onDownloadProgress(long progress, long total, int percentage);

    /**
     * 下载失败
     */
    void onDownloadFailed(Throwable e);
}
