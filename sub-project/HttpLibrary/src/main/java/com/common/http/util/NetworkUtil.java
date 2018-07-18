package com.common.http.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.telephony.TelephonyManager;

/**
 * @author jzy
 * created at 2018/6/4
 */
public class NetworkUtil {
    public static final int NET_OFF = 0;    //无网络
    public static final int NET_UNKNOWN = 1;    //未知网络
    public static final int NET_WIFI = 100;    //WIFI网络
    public static final int NET_2G = 2;    //2G网络
    public static final int NET_3G = 3;    //3G网络
    public static final int NET_4G = 4;   //4G网络
    public static final int NET_5G = 5;   //4G网络
    public static final int NET_ETHERNET = 101;
    public static final int NET_OTHER = 999;
    public static final int NET_EXCEPTION = 32;   //获取网络状态失败
    public static final int NET_DEFAULT = NET_WIFI + NET_3G + NET_4G;
    /**
     * 0--UNKNOWN_OPERA 1--CHINA_MOBILE 2--CHINA_TELECOM 3--CHINA_UNICOM 99--OTHER_OPERATOR
     *
     * @param context
     * @return
     */
    public static final int OPERA_UNKNOWN = 0;
    public static final int OPERA_CHINA_MOBILE = 1;
    public static final int OPERA_CHINA_TELECOM = 2;
    public static final int OPERA_CHINA_UNICOM = 3;
    public static final int OPERA_OTHER_OPERATOR = 99;

    public static boolean IsMobileNetworkAvailable(Context context) {
        if (IsNetworkAvailable(context)) {
            if (IsWifiNetworkAvailable(context))
                return false;
            return true;
        }
        return false;
    }

    public static boolean IsWifiNetworkAvailable(Context context) {
        // Monitor network connections (Wi-Fi, GPRS, UMTS, etc.)
        // mobile 3G ReportData Network
        ConnectivityManager conmgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conmgr == null) {
            return false;
        }
        NetworkInfo info = null;
        try {
            info = conmgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info == null) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        State wifi = info.getState(); // 显示wifi连接状态
        if (wifi == State.CONNECTED || wifi == State.CONNECTING)
            return true;
        return false;
    }

    public static boolean IsNetworkAvailable(Context context) {
        ConnectivityManager conmgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conmgr == null) {
            return false;
        }
        // 修改解决判断网络时的崩溃
        // mobile 3G ReportData Network
        try {
            NetworkInfo net3g = conmgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (net3g != null) {
                State mobile = net3g.getState();// 显示3G网络连接状态
                if (mobile == State.CONNECTED || mobile == State.CONNECTING)
                    return true;
            }
        } catch (Exception e) {
        }
        try {
            NetworkInfo netwifi = conmgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (netwifi != null) {
                State wifi = netwifi.getState(); // wifi
                // 如果3G网络和wifi网络都未连接，且不是处于正在连接状态 则进入Network Setting界面 由用户配置网络连接
                if (wifi == State.CONNECTED || wifi == State.CONNECTING)
                    return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    //判断网络是否存在
    public static boolean isNetworkAvailable(Context context) {
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                try {
                    if (cm.getActiveNetworkInfo() != null) {
                        if (cm.getActiveNetworkInfo().isAvailable()) {
                            return true;
                        }
                    }
                } catch (Exception e) {
                    return false;
                }
            }
            return false;
        }
        return false;
    }

    /**
     * 判断网络是否可用
     *
     * @return true:可用 false:不可用
     */
    public static boolean isNetworkActive(Context context) {
        if (context == null)
            return false;
        boolean bReturn = false;
        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        try {
            netInfo = conManager.getActiveNetworkInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (netInfo != null && netInfo.isConnected()) {
            bReturn = true;
        }
        return bReturn;
    }

    /**
     * 获取网络类型
     *
     * @param context
     * @return 网络类型
     */
    public static int getNetworkType(Context context) {
        if (context == null)
            return NET_UNKNOWN;
        int nReturn = NET_OFF;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                int type = info.getType();
                int subType = info.getSubtype();
                if (type == ConnectivityManager.TYPE_WIFI) {
                    //if (NetworkUtil.isWiFiActive(context)) {
                    nReturn = NET_WIFI;
                    //}
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    switch (subType) {
                        case TelephonyManager.NETWORK_TYPE_CDMA: // = 4 ~ 14-64 kbps
                        case TelephonyManager.NETWORK_TYPE_IDEN: // = 11 ~ 25 kbps
                        case TelephonyManager.NETWORK_TYPE_1xRTT: // = 7 2.5G或者
                            // 2.75G ~
                            // 50-100 kbps
                        case TelephonyManager.NETWORK_TYPE_GPRS: // = 1 2.5G ~ 171.2
                            // kbps
                        case TelephonyManager.NETWORK_TYPE_EDGE: // = 2 2.75G ~
                            // 384-473.6
                            // kbps
                            nReturn = NET_2G;
                            break;
                        case TelephonyManager.NETWORK_TYPE_EVDO_0: // = 5 ~ 400-1000
                            // kbps
                        case TelephonyManager.NETWORK_TYPE_UMTS: // = 3 ~ 400-7000
                            // kbps
                        case TelephonyManager.NETWORK_TYPE_EVDO_A: // = 6 ~ 600-1400
                            // kbps
                        case TelephonyManager.NETWORK_TYPE_HSPA: // = 10 3G ~
                            // 700-1700 kbps
                        case TelephonyManager.NETWORK_TYPE_EHRPD: // = 14 3.75g ~
                            // 1-2 Mbps
                        case TelephonyManager.NETWORK_TYPE_HSUPA: // = 9 ~ 1-23 Mbps
                        case TelephonyManager.NETWORK_TYPE_HSDPA: // = 8 ~ 2-14 Mbps
                        case TelephonyManager.NETWORK_TYPE_EVDO_B: // = 12 ~ 9 Mbps
                        case TelephonyManager.NETWORK_TYPE_HSPAP: // = 15 ~ 10-20
                            // Mbps
                            nReturn = NET_3G;
                            break;
                        case TelephonyManager.NETWORK_TYPE_LTE: // = 13 4G ~ 10+
                            // Mbps
                            nReturn = NET_4G;
                            break;
                        case TelephonyManager.NETWORK_TYPE_UNKNOWN:// = 0
                        default:
                            nReturn = NET_UNKNOWN;
                            break;
                    }
                } else {
                    nReturn = NET_UNKNOWN;
                }
            }
        } catch (NullPointerException e) {
            /**
             * java.lang.NullPointerException
             at android.os.Parcel.readException(Parcel.java:1431)
             at android.os.Parcel.readException(Parcel.java:1379)
             at android.net.IConnectivityManager$Stub$Proxy.getActiveNetworkInfo(IConnectivityManager.java:688)
             at android.net.ConnectivityManager.getActiveNetworkInfo(ConnectivityManager.java:460)
             at com.cleanmaster.kinfoc.ai.j(KInfocCommon.java:430)
             at com.cleanmaster.util.bt.b(DumpUploader.java:48)
             */
            nReturn = NET_EXCEPTION;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nReturn;
    }

    /**
     * 获取网络运营商
     *
     * @return
     */
    public static int getNetworkOpera(Context context) {
        int opera = OPERA_UNKNOWN;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imsi = telephonyManager.getSubscriberId();
            if (imsi == null) {
                if (TelephonyManager.SIM_STATE_READY == telephonyManager
                        .getSimState()) {
                    String operator = telephonyManager.getSimOperator();
                    if (operator != null) {
                        if (operator.equals("46000")
                                || operator.equals("46002")
                                || operator.equals("46007")) {
                            opera = OPERA_CHINA_MOBILE;
                        } else if (operator.equals("46001")) {
                            opera = OPERA_CHINA_UNICOM;
                        } else if (operator.equals("46003")) {
                            opera = OPERA_CHINA_TELECOM;
                        } else {
                            opera = OPERA_OTHER_OPERATOR;
                        }
                    }
                }
            } else {
                if (imsi.startsWith("46000") || imsi.startsWith("46002")
                        || imsi.startsWith("46007")) {
                    opera = OPERA_CHINA_MOBILE;
                } else if (imsi.startsWith("46001")) {
                    opera = OPERA_CHINA_UNICOM;
                } else if (imsi.startsWith("46003")) {
                    opera = OPERA_CHINA_TELECOM;
                } else {
                    opera = OPERA_OTHER_OPERATOR;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return opera;
    }
}
