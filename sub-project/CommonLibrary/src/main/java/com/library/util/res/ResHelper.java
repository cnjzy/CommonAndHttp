package com.library.util.res;

import android.content.Context;

/**
 * Created by jzy on 2017/5/25.
 */

public class ResHelper {
    private static class HOLDER {
        public static ResHelper INS = new ResHelper();
    }

    private ResHelper() {
    }

    public static ResHelper getInstance() {
        return HOLDER.INS;
    }

    private Context mContext;

    public void init(Context context) {
        this.mContext = context;
    }

    public String getString(int rsId) {
        if (mContext != null) {
            return mContext.getString(rsId);
        }
        return "";
    }

    public String getString(int rsId, String arg) {
        if (mContext != null) {
            return mContext.getString(rsId, arg);
        }
        return "";
    }

    public int getColor(int rsId) {
        if (mContext != null) {
            return mContext.getResources().getColor(rsId);
        }
        return 0;
    }

    public Context getContext() {
        return mContext;
    }

    public String[] getArray(int rsId) {
        if (mContext != null) {
            return mContext.getResources().getStringArray(rsId);
        }
        return null;
    }



}
