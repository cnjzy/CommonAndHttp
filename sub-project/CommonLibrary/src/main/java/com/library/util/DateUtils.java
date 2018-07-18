package com.library.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author jzy
 */
public class DateUtils {
    public static String MONTH_ENAME[] = {"JAN", "FEB", "MAR", "APR", "MAY",
            "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
    public static String DAY_CNAME[] = {"周日", "周一", "周二", "周三", "周四", "周五",
            "周六"};
    // 默认日期格式
    public static final String[] parsePatterns = new String[]{
            "yyyy-MM-dd HH:mm", "yyyy-MM-dd", "HH:mm",
            "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd", "yyyyMMdd",
            "MM月dd日 HH:mm", "yyyyMMddHHmmssSSS", "yyyy年MM月dd日 HH:mm",
            "yyyy-MM-dd HH:mm", "yyyy年MM月dd日", "yyyyMMddHHmmss"
            // 这里可以增加更多的日期格式，用得多的放在前面
    };

    /**
     * 根据当前给定的日期获取当前天是星期几(中国版的)
     *
     * @param date 任意时间
     * @return
     */
    public static String getChineseWeek(Date date) {
        final String dayNames[] = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五",
                "星期六"};
        Calendar c = getCalendar(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        return dayNames[dayOfWeek - 1];

    }

    /**
     * @param date 格式 2011-5-12
     * @return 星期
     */
    public static String getWeek(String date) {
        Date dat = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dat = simpleDateFormat.parse(date);
        } catch (ParseException e) {
        }
        return getChineseWeek(dat);
    }

    /**
     * 【由date类型获取字符串类型】
     *
     * @param date
     * @param parsePattern :转换后的格式，如"yyyy-MM-dd" 或"yyyy年MM月dd日"
     * @return
     * @author liuwei
     */
    public static String getStringByDate(Date date, String parsePattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(parsePattern);
        String dateStr = dateFormat.format(date);
        return dateStr;
    }

    /**
     * 使用默认的日期格式将字符串转换为日期
     *
     * @param str 要转换的字符串
     * @return 转换后的日期
     * @throws ParseException 默认匹配的日期格式  yyyy-MM-dd
     */
    public static Date parseDate(String str) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return simpleDateFormat.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Date();
    }

    /**
     * 使用给定的日期格式将字符串转换为日期
     *
     * @param str          要转换的字符串
     * @param parsePattern 日期格式字符串
     * @return 转换后的日期
     * @throws ParseException 日期格式不匹配
     */
    public static Date parseDate(String str, String parsePattern) {
        SimpleDateFormat format = new SimpleDateFormat(parsePattern);
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
        }
        return date;

    }

    /**
     * 使用给定的日期格式将字符串转换为日期
     *
     * @param timeLong     要转换的字符串
     * @param parsePattern 日期格式字符串
     * @return 转换后的日期
     * @throws ParseException 日期格式不匹配
     */
    public static String parseDateByLong(long timeLong, String parsePattern) {
        return getStringByDate(getDateByLong(timeLong), parsePattern);
    }

    /**
     * @param date
     * @return
     * @description 【获取当前日期日历】
     * @author zhangyun
     */
    public static Calendar getCalendar(Date date) {
        if (date == null)
            return null;
        Calendar c = new GregorianCalendar();
        c.setTime(date);
        return c;
    }

    /**
     * @param c
     * @param months
     * @return
     * @description 【date加months月】 如果要想直接用date对象，请这样调用 addMonth(getCalendar(Date
     * date), int months)
     * @author zhangyun
     */
    public static Date addMonth(Calendar c, int months) {
        if (c == null)
            return null;
        c.add(Calendar.MONTH, months);
        return c.getTime();
    }

    /**
     * @param c
     * @param days
     * @return
     * @description 【date加days日】 如果要想直接用date对象，请这样调用 addDay(getCalendar(Date
     * date), int days)
     * @author zhangyun
     */
    public static Date addDay(Calendar c, int days) {
        if (c == null)
            return null;
        c.add(Calendar.DAY_OF_MONTH, days);
        return c.getTime();
    }

    /**
     * 增加时间
     *
     * @param date
     * @param days
     * @return
     */
    public static Date addDay(Date date, int days) {
        Calendar c = getCalendar(date);
        if (c == null)
            return null;
        c.add(Calendar.DAY_OF_MONTH, days);
        return c.getTime();
    }

    /**
     * 【获取时间间隔分钟】
     *
     * @param minDate
     * @param maxDate
     * @return
     */
    public static double getIntervalMinutes(long minDate, long maxDate) {
        double mins = (maxDate - minDate) * 1.0 / 60;
        return mins;
    }

    /**
     * @param minDate
     * @param maxDate
     * @return
     * @description 【获取时间间隔分钟】
     * @author zhangyun
     */
    public static int getIntervalMinutes(Date minDate, Date maxDate) {
        int min = 0;
        if (minDate == null || maxDate == null) {
            return min;
        }
        try {
            long interval = maxDate.getTime() - minDate.getTime();
            min = (int) (Double.valueOf(interval) / 1000 / 60);
        } catch (Exception e) {
        }
        return min;
    }

    /**
     * @param minDate
     * @param maxDate
     * @return
     * @description 【获取时间间隔小时】
     * @author zhangyun
     */
    public static int getIntervalHours(Date minDate, Date maxDate) {
        int hours = 0;
        if (minDate == null || maxDate == null) {
            return hours;
        }
        try {
            hours = getIntervalMinutes(minDate, maxDate) / 60;
        } catch (Exception e) {
        }
        return hours;
    }

    /**
     * @param minDate
     * @param maxDate
     * @return
     * @description 【获取时间间隔天】
     * @author zhangyun
     */
    public static int getIntervalDays(Date minDate, Date maxDate) {
        int days = 0;
        if (minDate == null || maxDate == null) {
            return days;
        }
        try {
            days = getIntervalHours(minDate, maxDate) / 24;
        } catch (Exception e) {
        }
        return days;
    }

    /**
     * 校验是否隔年
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static boolean checkIsIntervalYear(Date startTime, Date endTime) {
        boolean isIntervalDay = false;
        Calendar c = Calendar.getInstance();
        c.setTime(startTime);
        int startYear = c.get(Calendar.YEAR);
        c.setTime(endTime);
        int endYear = c.get(Calendar.YEAR);
        return endYear - startYear > 0;
    }

    /**
     * @return boolean
     * @throws
     * @Title :isValidDate
     * @Description :验证yyyy-MM-dd 日期是否合法
     * @params @param sDate
     * @params @return
     */
    public static boolean isValidDate(String sDate) {
        String datePattern1 = "\\d{4}-\\d{2}-\\d{2}";
        String datePattern2 = "^((\\d{2}(([02468][048])|([13579][26]))"
                + "[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|"
                + "(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?"
                + "((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?("
                + "(((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?"
                + "((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
        if ((sDate != null)) {
            Pattern pattern = Pattern.compile(datePattern1);
            Matcher match = pattern.matcher(sDate);
            if (match.matches()) {
                pattern = Pattern.compile(datePattern2);
                match = pattern.matcher(sDate);
                return match.matches();
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * 判断一个时间所在月有多少天
     */
    public static int perMonthDays() {
        Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
        int day = aCalendar.getActualMaximum(Calendar.DATE);
        return day;
    }

    //判断某年是否是闰年
    public static boolean isLeap(int year) {
        boolean leap = false;
        if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
            leap = true;
        }
        return leap;
    }

    /**
     * 按照类型以及时间戳返回日期对象
     *
     * @param timeLong
     * @return
     */
    public static Date getDateByLong(long timeLong) {
        Date result = new Date();
        try {
            result = getCalendarByLong(timeLong).getTime();
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * 将long类型转换为Calendar类型
     *
     * @param timeLong
     * @return
     */
    public static Calendar getCalendarByLong(long timeLong) {
        Calendar c = null;
        try {
            c = Calendar.getInstance();
            c.setTimeInMillis(timeLong);
        } catch (Exception e) {
        }
        return c;
    }

    public static long getLongByStr(String dateStr) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = simpleDateFormat.parse(dateStr);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            return c.getTimeInMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }

    /**
     * 获取时间差
     *
     * @param timeLong
     * @return
     */
    public static CharSequence getTimeOffsetStrByLong(long timeLong) {
        String offset = android.text.format.DateUtils.getRelativeTimeSpanString(timeLong).toString();
        if (offset.startsWith("0") || "0分钟前".equals(offset)) {
            offset = "刚刚";
        }
        return offset;
    }

    /**
     * 获取当前时间戳与指定时间的差值字符串
     */
    public static String getTimeOffsetStr(Calendar offsetC) {
        Calendar nowC = Calendar.getInstance();
        long mills_offset = nowC.getTimeInMillis() - offsetC.getTimeInMillis();
        String util = "";
        if (mills_offset > 0)
            util = "前";
        else
            util = "后";

        mills_offset = Math.abs(mills_offset);

        int min_split = 60 * 1000;
        if (mills_offset < min_split) {
            // 小于1分钟
            return mills_offset / 1000 + "秒" + util;
        } else {
            int hour_split = 60 * min_split;
            int day_split = 24 * hour_split;
            if (mills_offset < hour_split) {
                // 小于1小时
                long min = mills_offset / min_split;
                long sec = mills_offset % min_split / 1000;
                String minStr = min + "分";
                String secStr = sec + "秒";
                return minStr + secStr + util;
            } else if (mills_offset < day_split) {
                // 小于一天
                long hour = mills_offset / hour_split;
                long min = mills_offset % hour_split / min_split;
                String hourStr = hour + "时";
                String minStr = min + "分";
                return hourStr + minStr + util;
            } else {
                // 大于一天
                long day = mills_offset / day_split;
                return day + "天" + util;
            }
        }
    }

    /**
     * 比较时间
     *
     * @param dateLong
     * @return
     */
    public static boolean compareByLong(long dateLong) {
        try {
            Calendar c = Calendar.getInstance();
            long curDateLong = c.getTimeInMillis();
            return (dateLong * 1000) > curDateLong;
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 返回日期
     *
     * @param dateString
     * @param state      0年 1月 2日 3星期几
     */
    public static int getDate(String dateString, int state) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            Date date = format.parse(dateString);
            Calendar c = Calendar.getInstance();
            c.setTime(date);

            switch (state) {
                case 0:
                    return c.get(Calendar.YEAR);
                case 1:
                    return c.get(Calendar.MONTH) + 1;
                case 2:
                    return c.get(Calendar.DATE);
                case 3:
                    return c.get(Calendar.DAY_OF_WEEK) - 1;
            }

        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 时间戳转换为时间
     *
     * @param timestampString
     */
    public static String TimeStamp2Date(String timestampString) {
        Long timestamp = Long.parseLong(timestampString);
//        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String date = sdf.format(new Date(Long.parseLong(String.valueOf(timestamp))* 1000));
        String date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(timestamp * 1000));
        return date;
    }

    public static String getVideoDateStrByLong(long timeLong) {
        if (timeLong > 0) {
            long hours = timeLong / 60 / 60 / 1000;
            long min = timeLong / 60 / 1000 % 60;
            long sec = timeLong / 1000 % 60;
            String prefix = hours + "" + min;
            String suffix = dateCellFormat(sec + "");
            return prefix + ":" + suffix;
        } else {
            return "00:00";
        }
    }

    public static boolean isToday(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);

        if (c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR) == 0 && c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH) == 0 && c1.get(Calendar.DAY_OF_MONTH) - c2.get(Calendar.DAY_OF_MONTH) == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 事件时间格式化
     *
     * @param date 指定时间
     * @return
     */
    public static String eventDateFormat(Date date) {
        Calendar c1 = Calendar.getInstance();

        Calendar c2 = Calendar.getInstance();
        c2.setTime(date);

        String monthAndDay = dateCellFormat((c2.get(Calendar.MONTH) + 1) + "") + "-" + dateCellFormat(c2.get(Calendar.DAY_OF_MONTH) + "") + " ";


        if (c1.get(Calendar.YEAR) < c2.get(Calendar.YEAR) || c1.get(Calendar.MONTH) < c2.get(Calendar.MONTH) || c1.get(Calendar.DAY_OF_MONTH) < c2.get(Calendar.DAY_OF_MONTH)) {
            // 大于当前时间
            return monthAndDay;
        }

        if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) && c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)) {
            // 当天
            return monthAndDay + "今天";
        }

        c1.set(Calendar.DATE, c1.get(Calendar.DATE) - 1);
        if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) && c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)) {
            // 昨天
            return monthAndDay + "昨天";
        }

        c1.set(Calendar.DATE, c1.get(Calendar.DATE) - 6);
        if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) && c1.get(Calendar.DAY_OF_MONTH) <= c2.get(Calendar.DAY_OF_MONTH)) {
            // 一周内
            return monthAndDay + dateWeekFormat(c2.get(Calendar.DAY_OF_WEEK));
        } else {
            // 一周外
            return DateUtils.getStringByDate(date, DateUtils.parsePatterns[1]);
        }
    }


    public static String dateWeekFormat(int week) {
        switch (week) {
            case Calendar.MONDAY:
                return "周一";
            case Calendar.TUESDAY:
                return "周二";
            case Calendar.WEDNESDAY:
                return "周三";
            case Calendar.THURSDAY:
                return "周四";
            case Calendar.FRIDAY:
                return "周五";
            case Calendar.SATURDAY:
                return "周六";
            case Calendar.SUNDAY:
                return "周日";
        }
        return "";
    }

    public static String dateCellFormat(String cell) {
        if (TextUtils.isEmpty(cell)) {
            return "00";
        } else if (cell.length() == 1) {
            return "0" + cell;
        } else {
            return cell;
        }
    }
}
