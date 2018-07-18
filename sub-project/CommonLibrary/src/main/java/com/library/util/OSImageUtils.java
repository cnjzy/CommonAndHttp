package com.library.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jzy on 2017/5/31.
 */

public class OSImageUtils {
    private static List<ImageFolder> ifList = new ArrayList<ImageFolder>();
    private static List<ImageFolder> videoList = new ArrayList<>();

    public synchronized static List<ImageFolder> getImageList(Context context) {
        return getAllImageByFolder(context, null);
    }

    public synchronized static List<ImageFolder> getImageListByFolder(Context context, ImageFolder folder) {
        return getAllImageByFolder(context, folder);
    }

    public synchronized static List<ImageFolder> getImageFolderList(Context context) {
        return getAllSDImageFolder(context);
    }

    public synchronized static List<ImageFolder> getVideoList(Context context) {
        return getSDCardVideoList(context);
    }

    /**
     * 获取SD卡图片文件夹目录
     */
    private static List<ImageFolder> getAllSDImageFolder(Context context) {
        ifList.clear();
        String[] projection = new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_ID, // 直接包含该图片文件的文件夹ID，防止在不同下的文件夹重名
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, // 直接包含该图片文件的文件夹名
                MediaStore.Images.Media.DISPLAY_NAME, // 图片文件名
                MediaStore.Images.Media.DATA, // 图片绝对路径
                "count(" + MediaStore.Images.Media._ID + ")"// 统计当前文件夹下共有多少张图片
        };
        String selection = " 0==0) group by bucket_display_name order by " + MediaStore.Images.Media._ID + " desc --(";

        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, null, "");

        if (null != cursor) {
            while (cursor.moveToNext()) {
                ImageFolder ifoler = new ImageFolder();
                String folderId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));
                String folder = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                long fileId = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID));
                String finaName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                int count = cursor.getInt(5);// 该文件夹下一共有多少张图片

                ifoler.setFolderId(folderId);
                ifoler.setName(folder);
                ifoler.setFinaName(finaName);
                ifoler.setPath(path);
                ifoler.setCount(count);
                ifList.add(ifoler);
            }
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
        return ifList;
    }


    private static List<ImageFolder> getAllImageByFolder(Context context, ImageFolder imageFolder) {
        List<ImageFolder> imageList = new ArrayList<ImageFolder>();
        String[] projection = new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_ID, // 直接包含该图片文件的文件夹ID，防止在不同下的文件夹重名
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, // 直接包含该图片文件的文件夹名
                MediaStore.Images.Media.DISPLAY_NAME, // 图片文件名
                MediaStore.Images.Media.DATA, // 图片绝对路径
                MediaStore.Images.Media.DATE_ADDED,
        };
        String selection = " 0==0 ";
        if (imageFolder != null) {
            selection += " and " + MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " = '" + imageFolder.getName() + "' ) ";
        }else{
            selection += ")";
        }

        selection += " order by " + MediaStore.Images.Media.DATE_TAKEN + " desc --(";

        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, null, "");

        if (null != cursor) {
            while (cursor.moveToNext()) {
                ImageFolder ifoler = new ImageFolder();
                String folderId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));
                String folder = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                long fileId = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID));
                String finaName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                String date = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                ifoler.setFolderId(folderId);
                ifoler.setFileId(fileId);
                ifoler.setName(folder);
                ifoler.setFinaName(finaName);
                ifoler.setPath(path);
                ifoler.setDate(date);
                imageList.add(ifoler);
            }
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }

        return imageList;
    }

    private static List<ImageFolder> getSDCardVideoList(Context context) {
        // 如果有sd卡（外部存储卡）
        videoList.clear();
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            Uri originalUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            ContentResolver cr = context.getApplicationContext().getContentResolver();
            Cursor cursor = cr.query(originalUri, null, null, null, null);
            if (cursor == null) {
                return videoList;
            }
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                long duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                String date = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED));

//                // 为了防止视频没有缩略图,创建视频缩略图
//                Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MICRO_KIND);
//                if (bitmap != null && !bitmap.isRecycled()){
//                    bitmap.recycle();
//                }

//                //获取当前Video对应的Id，然后根据该ID获取其缩略图的uri
//
//                String[] selectionArgs = new String[]{id + ""};
//                String[] thumbColumns = new String[]{MediaStore.Video.Thumbnails.DATA,
//                        MediaStore.Video.Thumbnails.VIDEO_ID};
//                String selection = MediaStore.Video.Thumbnails.VIDEO_ID + "=?";
//
//                String uri_thumb = "";
//                Cursor thumbCursor = (context.getApplicationContext().getContentResolver()).query(
//                        MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, thumbColumns, selection, selectionArgs,
//                        null);
//
//                if (thumbCursor != null && thumbCursor.moveToFirst()) {
//                    uri_thumb = thumbCursor.getString(thumbCursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA));
//
//                }

                ImageFolder imageFolder = new ImageFolder();
                imageFolder.setVideo(true);
                imageFolder.setName(title);
                imageFolder.setPath(path);
                imageFolder.setFileId(id);
                imageFolder.setDate(date);
                imageFolder.setDuration(duration);
                videoList.add(imageFolder);
            }
            if (cursor != null) {
                cursor.close();
            }
        }

        return videoList;
    }
}
