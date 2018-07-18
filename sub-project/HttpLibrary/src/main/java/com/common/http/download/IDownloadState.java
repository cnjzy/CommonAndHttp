package com.common.http.download;

/**
 * @author jzy
 * created at 2018/5/28
 */
public interface IDownloadState {
    int DOWNLOAD_STATE_FAIL = 0;
    int DOWNLOAD_STATE_PROGRESS = 1;
    int DOWNLOAD_STATE_SUCCESS = 2;
}
