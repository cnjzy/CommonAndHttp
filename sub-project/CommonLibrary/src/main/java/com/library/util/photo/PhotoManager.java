package com.library.util.photo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.library.R;

import junit.framework.Assert;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
/**
 * @author jzy
 */
public class PhotoManager {
    /**
     * 图片保存路径
     */
    public static String TEMP_PATH = Environment.getExternalStorageDirectory() + File.separator + "DCIM" + File.separator + "Camera" + File.separator;

    public static final int PHOTO_PICKED_WITH_DATA = 3021;
    public static final int CAMERA_WITH_DATA = 3023;
    public static final int SELECT_PICKED_LOCALTION = 3022;

    /**
     * 图片相关对象
     */
    private byte[] mContent;
    private ContentResolver resolver = null;
    private Bitmap photo = null;

    /**
     * 图片名称
     */
    private String fileName = "photo";
    /**
     * 图片对象
     */
    private File tempFile;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 一个Activity多个图片分类
     */
    private int type;

    private Map<String, String> checkImageTypeMap = new HashMap<String, String>();

    public PhotoManager() {
        checkImageTypeMap.put("jpg", "jpg");
        checkImageTypeMap.put("jpeg", "jpeg");
    }

    /**
     * 获取路径
     *
     * @param act
     */
    private void getPath(Activity act) {
        ContentValues values = new ContentValues();

        TEMP_PATH = getPath(act, act.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values));
        if (TextUtils.isEmpty(TEMP_PATH)) {
            TEMP_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "superc"
                    + File.separator + "TEMP" + File.separator;
            TEMP_PATH = showStorageToast(act) ? TEMP_PATH : getPath(act,
                    act.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values));
            // 判断文件夹是否存在，不存在则创建
            File folder = new File(TEMP_PATH);
            try {
                if (!folder.exists()) {
                    folder.mkdirs();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void showGetPhotoDialog(final Activity act, final String fileName) {
        View view = act.getLayoutInflater().inflate(R.layout.dialog_pic_layout, null);
        view.setMinimumWidth(10000);
        final Dialog dialog = new Dialog(act, R.style.DialogTheme_NoTitle);

        Button takePhotoBtn = (Button) view.findViewById(R.id.btn_take_photo);
        Button pickPhotoBtn = (Button) view.findViewById(R.id.btn_pick_photo);
        Button cancelBtn = (Button) view.findViewById(R.id.btn_cancel);

        takePhotoBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                getPhotoByCamera(act, fileName);
                dialog.dismiss();
            }
        });

        pickPhotoBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                getPhotoByGallery(act);
                dialog.dismiss();
            }
        });

        cancelBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // set a large value put it in bottom
        Window w = dialog.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        final int cMakeBottom = -1000;
        lp.y = cMakeBottom;
        lp.gravity = Gravity.BOTTOM;
        w.setWindowAnimations(R.style.custom_dialog_bottom_animstyle);
        dialog.onWindowAttributesChanged(lp);
        dialog.setCanceledOnTouchOutside(true);

        dialog.setContentView(view);
        dialog.show();
    }

    /**
     * 通过相机获取图片
     *
     * @param act
     */
    public void getPhotoByCamera(Activity act, String fileName) {
        getPath(act);
        this.fileName = fileName;
        if (TEMP_PATH.indexOf(".") == -1)
            TEMP_PATH += fileName;
        tempFile = new File(TEMP_PATH);
        try {
            if (tempFile.exists())
                tempFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        intent.putExtra("camerasensortype", 2);// 调用前置摄像头
        intent.putExtra("autofocus", true);// 自动对焦
        intent.putExtra("fullScreen", false);// 全屏
        intent.putExtra("showActionIcons", false);
        // 指定调用相机拍照后照片的储存路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        act.startActivityForResult(intent, CAMERA_WITH_DATA);
    }

    /**
     * 获取本地图片
     *
     * @param act
     */
    public void getPhotoByGallery(Activity act) {
        getPath(act);
        Intent localIntent1 = new Intent();
        localIntent1.setType("image/*");
        localIntent1.setAction("android.intent.action.GET_CONTENT");
        Intent localIntent2 = Intent.createChooser(localIntent1, "Select Picture");
        act.startActivityForResult(localIntent2, SELECT_PICKED_LOCALTION);
    }

    /**
     * 获取图片回调
     *
     * @param act
     * @param requestCode
     * @param resultCode
     * @param data
     * @return
     */
    public Bitmap onActivityResult(Activity act, int requestCode, int resultCode, Intent data) {
        return onActivityResult(act, requestCode, resultCode, data, 600, false);
    }

    /**
     * 获取图片回调
     *
     * @param act
     * @param requestCode
     * @param resultCode
     * @param data
     * @param size
     * @param isCrop
     * @return
     */
    public Bitmap onActivityResult(Activity act, int requestCode, int resultCode, Intent data, int size, boolean isCrop) {
        if (resultCode != Activity.RESULT_OK)
            return null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap tmp;
        int width = size;
        int height = size;
        float scale = 0;

        switch (requestCode) {
            case CAMERA_WITH_DATA:
                options.inJustDecodeBounds = true;
                tmp = BitmapFactory.decodeFile(TEMP_PATH, options);
                if (tmp != null) {
                    tmp.recycle();
                    tmp = null;
                }

                if (options.outHeight == 0) {
                    return null;
                }

                scale = (float) options.outWidth / width;
                height = (int) (options.outHeight / scale);
                filePath = TEMP_PATH;
                Bitmap b = extractThumbNail(filePath, height, width, false);
                Bitmap bm = null;
                if (b != null) {
                    b = rotateBitmap(b, filePath, -1);
                    bm = b.copy(Config.ARGB_8888, true);
                    // 创建新文件
                    try {
                        File tempFile = new File(filePath);
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tempFile));
                        bm.compress(CompressFormat.JPEG, 80, bos);
                        bos.flush();
                        bos.close();
                        bm.recycle();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return b;
            case SELECT_PICKED_LOCALTION:
                try {
                    // 获得图片的uri
                    Uri originalUri = data.getData();
                    // startPhotoZoom(act, getPath(act, originalUri), 200);

                    options.inJustDecodeBounds = true;
                    tmp = BitmapFactory.decodeFile(getPath(act, originalUri), options);
                    if (tmp != null) {
                        tmp.recycle();
                        tmp = null;
                    }

                    if (options.outHeight == 0) {
                        return null;
                    }

                    scale = (float) options.outWidth / width;
                    height = (int) (options.outHeight / scale);

                    release();
                    filePath = getPath(act, originalUri);
                    b = extractThumbNail(filePath, height, width, false);
                    bm = null;
                    if (b != null) {
                        if (TEMP_PATH.subSequence(TEMP_PATH.length() - 1, TEMP_PATH.length()).equals("/")) {
                            TEMP_PATH += fileName + ".jpg";
                        }
                        filePath = TEMP_PATH;
                        b = rotateBitmap(b, TEMP_PATH, -1);
                        bm = b.copy(Config.ARGB_8888, true);
                        // 创建新文件
                        try {
                            File tempFile = new File(TEMP_PATH);
                            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tempFile));
                            bm.compress(CompressFormat.JPEG, 80, bos);
                            bos.flush();
                            bos.close();
                            bm.recycle();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return b;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case PHOTO_PICKED_WITH_DATA:
                release();
                photo = data.getParcelableExtra("data");
                try {
                    if (photo == null) {
                        // 获得图片的uri
                        Uri originalUri = data.getData();
                        // 将图片内容解析成字节数组
                        mContent = readStream(resolver.openInputStream(Uri.parse(originalUri.toString())));
                        // 将字节数组转换为ImageView可调用的Bitmap对象
                        release();
                        photo = getPicFromBytes(mContent, null);
                    }
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (photo != null) {
                    return photo;
                }

        }
        return null;
    }

    public static Bitmap getPicFromBytes(byte[] bytes, BitmapFactory.Options opts) {
        if (bytes != null)
            if (opts != null)
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
            else
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;
    }

    public static byte[] readStream(InputStream inStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;
    }

    public void release() {
        if (photo != null && !photo.isRecycled()) {
            photo.recycle();
            photo = null;
        }
    }

    public Bitmap stringtoBitmap(String string) {
        // 将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public String bitmaptoString(Bitmap bitmap) {
        // 将Bitmap转换成字符串
        String string = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 100, bStream);
        byte[] bytes = bStream.toByteArray();
        string = Base64.encodeToString(bytes, Base64.DEFAULT);
        return string;
    }

    public static Bitmap cropBitmap(final String path, final int height, final int width) {
        Bitmap result = null;
        try {
            Bitmap tmp = BitmapFactory.decodeFile(path);

            final float beY = height * 1.0f / tmp.getHeight();
            final float beX = width * 1.0f / tmp.getWidth();

            float scale = beY < beX ? beX : beY;
            Matrix m = new Matrix();
            m.setScale(scale, scale);

            final Bitmap scaleBmp = Bitmap.createBitmap(tmp, 0, 0, tmp.getWidth(), tmp.getHeight(), m, true);
            if (tmp != null) {
                tmp.recycle();
            }
            if (scaleBmp != null) {
                result = Bitmap.createBitmap(width, height, Config.ARGB_8888);
                Canvas canvas = new Canvas(result);
                canvas.drawBitmap(scaleBmp, (result.getWidth() - scaleBmp.getWidth()) / 2,
                        (result.getHeight() - scaleBmp.getHeight()) / 2, null);
                scaleBmp.recycle();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 不失真改变Bitmap
     */
    private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;

    public static Bitmap extractThumbNail(final String path, final int height, final int width, final boolean crop) {
        Assert.assertTrue(path != null && !path.equals("") && height > 0 && width > 0);

        BitmapFactory.Options options = new BitmapFactory.Options();

        try {
            options.inJustDecodeBounds = true;
            Bitmap tmp = BitmapFactory.decodeFile(path, options);
            if (tmp != null) {
                tmp.recycle();
                tmp = null;
            }

            final double beY = options.outHeight * 1.0 / height;
            final double beX = options.outWidth * 1.0 / width;
            options.inSampleSize = (int) (crop ? (beY > beX ? beX : beY) : (beY < beX ? beX : beY));
            if (options.inSampleSize <= 1) {
                options.inSampleSize = 1;
            }

            // NOTE: out of memory error
            while (options.outHeight * options.outWidth / options.inSampleSize > MAX_DECODE_PICTURE_SIZE) {
                options.inSampleSize++;
            }

            int newHeight = height;
            int newWidth = width;
            if (crop) {
                if (beY > beX) {
                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
                } else {
                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
                }
            } else {
                if (beY < beX) {
                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
                } else {
                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
                }
            }

            options.inJustDecodeBounds = false;

            Bitmap bm = BitmapFactory.decodeFile(path, options);
            if (bm == null) {
                return null;
            }
            // 因为SDK4.1版本createScaleBitmap方法处理机制和其他版本不同，如果宽高没有改变的情况下，不会重新创建Bitmap对象，然后释放掉sourceBitmap会抛出异常
            Bitmap scale = Bitmap.createScaledBitmap(bm, newWidth - 1, newHeight - 1, true);
            if (scale != null) {
                if (!scale.equals(bm))
                    bm.recycle();
                bm = scale;
            }

            if (crop) {
                final Bitmap cropped = Bitmap.createBitmap(bm, (bm.getWidth() - width) >> 1,
                        (bm.getHeight() - height) >> 1, width, height);
                if (cropped == null) {
                    return bm;
                }

                bm.recycle();
                bm = cropped;
            }
            return bm;

        } catch (final OutOfMemoryError e) {
            options = null;
        }

        return null;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径 degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片 orientation -1任意旋转 0横向 1竖向
     */
    public static Bitmap rotateBitmap(Bitmap bm, String filePath, int orientation) {
        try {
            if (bm == null)
                return bm;
            int degress = readPictureDegree(filePath);
            if ((orientation != -1 && (orientation == 0 && bm.getWidth() > bm.getHeight()) || (orientation == 1 && bm
                    .getWidth() < bm.getHeight())) || degress == 0) {
                return bm;
            }
            Matrix m = new Matrix();
            m.setRotate(degress, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
            Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
            if (bm1 != null) {
                if (bm != null && !bm.isRecycled())
                    bm.recycle();
                return bm1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return bm;
        }

        return bm;
    }

    public String getFilePath() {
        return filePath == null ? "" : filePath;
    }

    public static final int NO_STORAGE_ERROR = -1;
    public static final int CANNOT_STAT_ERROR = -2;

    public static boolean showStorageToast(Activity activity) {
        return showStorageToast(activity, false);
    }

    public static boolean showStorageToast(Activity activity, boolean toast) {
        return showStorageToast(activity, calculatePicturesRemaining(), toast);
    }

    public static boolean showStorageToast(Activity activity, int remaining, boolean toast) {
        String noStorageText = null;
        boolean isStorage = true;
        ;

        if (remaining == NO_STORAGE_ERROR) {
            String state = Environment.getExternalStorageState();
            if (state == Environment.MEDIA_CHECKING) {
                noStorageText = "Preparing card";
                isStorage = true;
            } else {
                noStorageText = "No storage card";
                isStorage = false;
            }
        } else if (remaining < 1) {
            noStorageText = "没有足够的空间";
            isStorage = false;
        }

        if (noStorageText != null && toast) {
            Toast.makeText(activity, noStorageText, Toast.LENGTH_SHORT).show();
        }
        return isStorage;
    }

    public static int calculatePicturesRemaining() {
        try {
            /*
             * if (!ImageManager.hasStorage()) { return NO_STORAGE_ERROR; } else {
             */
            String storageDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();
            StatFs stat = new StatFs(storageDirectory);
            float remaining = ((float) stat.getAvailableBlocks() * (float) stat.getBlockSize()) / 400000F;
            return (int) remaining;
            // }
        } catch (Exception ex) {
            // if we can't stat the filesystem then we don't know how many
            // pictures are remaining. it might be zero but just leave it
            // blank since we really don't know.
            return CANNOT_STAT_ERROR;
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     *
     * @param context
     * @param imageUri
     * @author yaoxing
     * @date 2014-10-12
     */
    @TargetApi(19)
    public static String getPath(Activity context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT
                && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public boolean isImageType(String path) {
        if (TextUtils.isEmpty(path) || path.indexOf(".") == -1) {
            return false;
        } else {
            String format = path.substring(path.lastIndexOf(".") + 1, path.length());
            return !TextUtils.isEmpty(checkImageTypeMap.get(format));
        }
    }
}
