package com.library.util;


import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.library.bean.AppInfo;

import java.io.File;
import java.util.List;

public class PackageUtils {
    public static boolean isPkgInstalled(Context ctx, String pkgName) {
        return (getApplicationInfo(ctx, pkgName) != null);
    }

    public static ApplicationInfo getApplicationInfo(Context ctx, String pkgName) {
        if (TextUtils.isEmpty(pkgName) || ctx == null) return null;
        try {
            PackageInfo pkgInfo = ctx.getPackageManager().getPackageInfo(pkgName, 0);
            if (pkgInfo != null) {
                return pkgInfo.applicationInfo;
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static boolean isHasPackage(Context c, String packageName) {
        if (null == c || null == packageName) return false;

        boolean bHas = true;
        try {
            c.getPackageManager().getPackageInfo(packageName, PackageManager.GET_GIDS);
        } catch (/* NameNotFoundException */Exception e) {
            // 抛出找不到的异常，说明该程序已经被卸载
            bHas = false;
        }
        return bHas;
    }

    public static void startAppByPkgName(String pkgName, Context context) {

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = context.getPackageManager().queryIntentActivities(resolveIntent, 0);
        if (resolveinfoList == null || resolveinfoList.size() == 0) {
            return;
        }

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);
            intent.setComponent(cn);
            context.startActivity(intent);
        }
    }

    /**
     * 通过包名获取应用信息
     *
     * @param context
     * @param packageName
     * @return
     */
    public static AppInfo getAppInfoWithPackageName(Context context, String packageName) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            return new AppInfo(packageInfo.packageName, packageInfo.versionCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过文件获取apk包名
     *
     * @param context
     * @param file
     * @return
     */
    public static AppInfo getPackageNameWithFile(Context context, File file) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageArchiveInfo(file.getAbsolutePath(), 0);
            if (pi == null) {
                return null;
            }
            AppInfo appInfo = new AppInfo(pi.applicationInfo.packageName, pi.versionCode);
            return appInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 安装应用
     *
     * @param context
     */
    public static void installApp(Context context, String filePath) {
        if (context == null || TextUtils.isEmpty(filePath)) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, "com.oneweone.cashnews.fileprovider", new File(filePath));
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        context.startActivity(intent);
    }

    public static String getTaskTopPackageName(Context context) {
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> appTasks = activityManager.getRunningTasks(1);
            if (null != appTasks && !appTasks.isEmpty()) {
                return appTasks.get(0).topActivity.getPackageName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
