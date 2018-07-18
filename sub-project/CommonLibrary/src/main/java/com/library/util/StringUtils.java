package com.library.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jzy on 2017/6/9.
 */

public class StringUtils {

    /**
     * 是否是URL
     *
     * @param url
     * @return
     */
    public static boolean isUrl(String url) {
        if (TextUtils.isEmpty(url))
            return false;
        Pattern exp = Pattern.compile("^http://[\\w\\.\\-]+(?:/|(?:/[\\w\\.\\-]+)*)?$", Pattern.CASE_INSENSITIVE);
        return exp.matcher(url).matches();
    }

    /**
     * 是否是URL
     *
     * @param url
     * @return
     */
    public static boolean isNotHttpUrl(String url) {
        if (TextUtils.isEmpty(url))
            return false;
        Pattern exp = Pattern.compile("^[\\w\\.\\-]+(?:/|(?:/[\\w\\.\\-]+)*)?$", Pattern.CASE_INSENSITIVE);
        return exp.matcher(url).matches();
    }


    /**
     * 判断字符串是否是英文
     *
     * @param str
     * @return true：是   false：不是
     */
    public static boolean isABC(String str) {
        if (str == null || "".equals(str)) {
            return false;
        }
        String c = str.substring(0, 1);
        Pattern pattern = Pattern.compile("[a-z|A-Z]");
        Matcher m = pattern.matcher(c);
        if (m.find()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断字符串是否是数字
     *
     * @param str
     * @return true：是   false：不是
     */
    public static boolean isNumber(String str) {
        if (str == null || "".equals(str)) {
            return false;
        }
        String c = str.substring(0, 1);
        Pattern pattern = Pattern.compile("[0-9]");
        Matcher m = pattern.matcher(c);
        if (m.find()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断字符串是否是邮箱
     *
     * @param str
     * @return true：是   false：不是
     */
    public static boolean isEmail(String str) {
        if (str == null || "".equals(str)) {
            return false;
        }
        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(check);
        Matcher m = pattern.matcher(str);
        if (m.find()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 验证手机号的格式
     *
     * @param mobile
     * @return 是否为手机号
     */
    public static boolean isMobileNumber(String mobile) {
        if (TextUtils.isEmpty(mobile)) {
            return false;
        } else {
            Pattern pattern = Pattern.compile("^1[3|5|7|8]\\d{9}$");
            Matcher matcher = pattern.matcher(mobile);
            if (matcher.find()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否是正确的邮件或者手机号格式
     *
     * @param contect
     * @return
     */
    public static boolean verifyEmailOrMobile(String contect) {
        if (isMobileNumber(contect) || isEmail(contect)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 验证密码的格式
     *
     * @param password
     * @return 是否为6～24位字符（字母、数字、特殊符号）
     */
    public static boolean verifyNewPasswordFormat(String password) {
        if (TextUtils.isEmpty(password)) {
            return false;
        } else {
            Pattern pattern = Pattern.compile("^.{6,24}$");
            Matcher matcher = pattern.matcher(password);
            if (matcher.find()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证用户名的格式
     *
     * @param userName
     * @return 是否为字母、汉字开头的2~12个字符
     */
    public static boolean verifyUserNameFormat(String userName) {
        if (TextUtils.isEmpty(userName)) {
            return false;
        } else {
            Pattern pattern = Pattern.compile("^[a-zA-Z\u4E00-\u9FA5][0-9a-zA-Z\u4E00-\u9FA5]{1,11}$");
            Matcher matcher = pattern.matcher(userName);
            if (matcher.find()) {
                return true;
            }
        }
        return false;
    }

    /**
     * String类型的数字转换为整型
     * <p>
     * author renjie
     * date 2014.05.30
     */
    public static long string2Long(String sData, long def) {
        long data = def;
        if (sData != null) {
            try {
                data = Long.valueOf(sData);
            } catch (Exception e) {
            }
        }
        return data;
    }
}
