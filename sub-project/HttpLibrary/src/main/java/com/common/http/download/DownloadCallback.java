package com.common.http.download;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.common.http.util.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author jzy
 * created at 2018/5/28
 */
public class DownloadCallback implements Callback, IDownloadState {

    private final String TAG = "DownloadCallback";

    private String mFileDirPath = "";
    private IOnDownloadListener mOnDownloadListener = null;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWNLOAD_STATE_FAIL:
                    if (mOnDownloadListener != null) {
                        mOnDownloadListener.onDownloadFailed((Throwable) msg.obj);
                    }
                    break;
                case DOWNLOAD_STATE_PROGRESS:
                    if (mOnDownloadListener != null && msg.obj != null) {
                        String[] content = msg.obj.toString().split(",");
                        if (content == null || content.length < 3) {
                            return;
                        }
                        long progress = Long.parseLong(content[0]);
                        long total = Long.parseLong(content[1]);
                        int percentage = Integer.parseInt(content[2]);
                        mOnDownloadListener.onDownloadProgress(progress, total, percentage);
                    }
                    break;
                case DOWNLOAD_STATE_SUCCESS:
                    if (mOnDownloadListener != null) {
                        mOnDownloadListener.onDownloadSuccess((String) msg.obj);
                    }
                    break;
            }
        }
    };

    public DownloadCallback(Context context, IOnDownloadListener onDownloadListener) {
        this(context, onDownloadListener, FileUtils.getFileDirPath(context));
    }

    public DownloadCallback(Context context, IOnDownloadListener onDownloadListener, String fileDirPath) {
        this.mOnDownloadListener = onDownloadListener;
        if (!TextUtils.isEmpty(fileDirPath)) {
            this.mFileDirPath = fileDirPath;
        } else {
            this.mFileDirPath = FileUtils.getFileDirPath(context);
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Message message = Message.obtain();
        message.what = IDownloadState.DOWNLOAD_STATE_FAIL;
        message.obj = e;
        mHandler.sendMessage(message);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        //储存下载文件的目录
        String savePath = FileUtils.createDir(mFileDirPath);
        try {
            String url = response.request().url().toString();
            Log.e(TAG, url);
            File file = new File(savePath, FileUtils.getNameFromUrl(url));

            is = response.body().byteStream();
            long total = response.body().contentLength();
            fos = new FileOutputStream(file);

            long progress = 0;
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
                progress += len;
                int percentage = (int) (progress * 1.0f / total * 100);
                //下载中
                String content = progress + "," + total + "," + percentage;
                Message message = Message.obtain();
                message.what = DOWNLOAD_STATE_PROGRESS;
                message.obj = content;
                mHandler.sendMessage(message);
            }
            fos.flush();
            //下载完成
            Message message = Message.obtain();
            message.what = DOWNLOAD_STATE_SUCCESS;
            message.obj = file.getAbsolutePath();
            mHandler.sendMessage(message);
        } catch (Exception e) {
            Message message = Message.obtain();
            message.what = DOWNLOAD_STATE_FAIL;
            message.obj = e;
            mHandler.sendMessage(message);
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {

            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {

            }
        }
    }
}