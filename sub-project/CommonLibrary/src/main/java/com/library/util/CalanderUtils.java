package com.library.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import com.library.log.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by jzy on 2017/6/7.
 */

public class CalanderUtils {

    //兼容Android2.2以上版本
    private static String calanderURL = "content://com.android.calendar/calendars";
    private static String calanderEventURL = "content://com.android.calendar/events";
    private static String calanderRemiderURL = "content://com.android.calendar/reminders";

    /**
     * 添加默认账户
     */
    public static void addAccount(Context context) {
        try {
            TimeZone timeZone = TimeZone.getDefault();
            ContentValues value = new ContentValues();
            value.put(CalendarContract.Calendars.NAME, "yy");

            value.put(CalendarContract.Calendars.ACCOUNT_NAME, "calander@gmail.com");
            value.put(CalendarContract.Calendars.ACCOUNT_TYPE, "com.android.exchange");
            value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, "calander");
            value.put(CalendarContract.Calendars.VISIBLE, 1);
            value.put(CalendarContract.Calendars.CALENDAR_COLOR, -9206951);
            value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
            value.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
            value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
            value.put(CalendarContract.Calendars.OWNER_ACCOUNT, "calander@gmail.com");
            value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0);

            Uri calendarUri = CalendarContract.Calendars.CONTENT_URI;
            calendarUri = calendarUri.buildUpon()
                    .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                    .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, "calander@gmail.com")
                    .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, "com.android.exchange")
                    .build();

            context.getContentResolver().insert(calendarUri, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取日历账户数量
     *
     * @param context 上下文
     * @return 账户数量
     */
    public static int getAccountCount(Context context) {
        int count = 0;
        Cursor userCursor = context.getContentResolver().query(Uri.parse(calanderURL), null, null, null, null);
        try {
            if (userCursor != null) {
                count = userCursor.getCount();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (userCursor != null) {
                userCursor.close();
            }
        }
        return count;
    }

    /**
     * 删除账户，
     *
     * @param context 上下文
     * @param id      -1为删除所有账户，慎用,慎用，慎用
     */
    public static void delAccount(Context context, int id) {
        try {
            int rownum = context.getContentResolver().delete(Uri.parse(calanderURL), "_id='" + id + "'", null);
            //可以令_id=你添加账户的id，以此删除你添加的账户
            LogUtils.e("====================delAllAccount:" + rownum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除预约
     *
     * @param context 上下文
     * @param title   预约
     */
    public static void delEvent(Context context, String title) {
        try {
            int rownum = context.getContentResolver().delete(Uri.parse(calanderEventURL), "title='" + title + "'", null);
            //可以令_id=你添加账户的id，以此删除你添加的账户
            LogUtils.e("====================delEvent:" + rownum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取已添加事件
     * 这里与添加事件时的账户相对应，都是向最后一个账户添加
     *
     * @param context 上下文
     */
    public static List<String> readEvent(Context context) {
        List<String> dataList = new ArrayList<>();
        try {
            Cursor eventCursor = context.getContentResolver().query(Uri.parse(calanderEventURL), null, null, null, null);
            try {
                if (eventCursor != null) {
                    for (eventCursor.moveToFirst(); !eventCursor.isAfterLast(); eventCursor.moveToNext()) {
                        dataList.add(eventCursor.getString(eventCursor.getColumnIndex("title")));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (eventCursor != null) {
                    eventCursor.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataList;
    }

    /**
     * 添加事件
     *
     * @param context      上下文
     * @param title        标题
     * @param desc         介绍
     * @param location     发生地点
     * @param startTime    开始时间(时间戳)
     * @param endTime      结束时间（时间戳）
     * @param preAlarmTime 提前提醒时间（分钟）
     */
    public static void addEvent(Context context, String title, String desc, String location, long startTime, long endTime, int preAlarmTime) {

        if (getAccountCount(context) < 1) {
            addAccount(context);
        }

        // 获取要出入的gmail账户的id
        String calId;
        Cursor userCursor = context.getContentResolver().query(Uri.parse(calanderURL), null, null, null, null);
        try {
            if (userCursor != null && userCursor.getCount() > 0) {
                userCursor.moveToLast();  //注意：是向最后一个账户添加，开发者可以根据需要改变添加事件 的账户
                calId = userCursor.getString(userCursor.getColumnIndex("_id"));
            } else {
                return;
            }

            ContentValues event = new ContentValues();
            event.put("title", title);
            event.put("description", desc);
            // 插入账户
            event.put("calendar_id", calId);
            System.out.println("calId: " + calId);
            event.put("eventLocation", location);

//        Calendar mCalendar = Calendar.getInstance();
//        mCalendar.set(Calendar.HOUR_OF_DAY, 11);
//        mCalendar.set(Calendar.MINUTE, 45);
//        long start = mCalendar.getTime().getTime();
//        mCalendar.set(Calendar.HOUR_OF_DAY, 12);
//        long end = mCalendar.getTime().getTime();

            event.put("dtstart", startTime);
            event.put("dtend", endTime);
            event.put("hasAlarm", preAlarmTime);

            event.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Shanghai");  //这个是时区，必须有，
            //添加事件
            Uri newEvent = context.getContentResolver().insert(Uri.parse(calanderEventURL), event);
            //事件提醒的设定
            if (newEvent != null) {
                long id = Long.parseLong(newEvent.getLastPathSegment());
                ContentValues values = new ContentValues();
                values.put("event_id", id);
                // 提前10分钟有提醒
                values.put("minutes", 10);
                context.getContentResolver().insert(Uri.parse(calanderRemiderURL), values);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (userCursor != null)
                userCursor.close();
        }
    }
}
