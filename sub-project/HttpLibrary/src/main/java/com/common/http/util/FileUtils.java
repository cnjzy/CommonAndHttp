package com.common.http.util;

import android.content.Context;

import java.io.File;
import java.io.IOException;

/**
 * @author jzy
 * created at 2018/5/28
 */
public class FileUtils {

    public static String getFileDirPath(Context context) {
        try {
            return context.getExternalFilesDir("").getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 通过URL获取文件名
     *
     * @param url
     * @return
     */
    public static String getNameFromUrl(String url) {
        String name = url.substring(url.lastIndexOf("/") + 1);
        if (name.indexOf("?") != -1) {
            name = name.substring(0, name.indexOf("?"));
        }
        return name;
    }

    /**
     * 创建文件夹
     *
     * @param saveDir
     * @return
     * @throws IOException
     */
    public static String createDir(String saveDir) throws IOException {
        File downloadFile = new File(saveDir);
        if (!downloadFile.exists()) {
            downloadFile.mkdirs();
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }
}
