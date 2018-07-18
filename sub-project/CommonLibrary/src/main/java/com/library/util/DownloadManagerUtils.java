package com.library.util;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.library.R;

import java.io.File;

/**
 * Created by jzy on 2017/6/13.
 */

public class DownloadManagerUtils {

    public static final String APK_NAME = "zhuannews.apk";
    public static final String APK_PATH = Environment.getExternalStoragePublicDirectory("down").getAbsolutePath() + File.separator + APK_NAME;


    public static void downloadApk(Context context, String url) {
        if (TextUtils.isEmpty(url) || !url.endsWith(".apk"))
            return;
        // 先删除文件
        File file = new File(APK_PATH);
        if (file.exists()) {
            file.delete();
        }

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(context.getString(R.string.app_name));
        request.setDescription(context.getString(R.string.app_name));
        request.setMimeType("application/vnd.android.package-archive");
        request.setDestinationInExternalPublicDir("down", APK_NAME);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        downloadManager.enqueue(request);
    }
}
