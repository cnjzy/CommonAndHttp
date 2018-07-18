package com.library.util;

import java.util.regex.Pattern;

public class NumberUtils {

    /**
     * 检测IP地址
     *
     * @param ip
     * @return
     */
    public static boolean checkIp(String ip) {
        String regex = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(ip).matches();
    }
}
