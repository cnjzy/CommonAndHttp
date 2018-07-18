package com.library.bean;

/**
 * @author jzy
 * created at 2018/6/4
 */
public class AppInfo extends BaseBean {
    private String packageName = "";
    private int versionCode;

    public AppInfo(String packageName, int versionCode) {
        this.packageName = packageName;
        this.versionCode = versionCode;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof AppInfo) {
            if (toString().equals(obj.toString())) {
                return true;
            }
        }
        return super.equals(obj);
    }
}