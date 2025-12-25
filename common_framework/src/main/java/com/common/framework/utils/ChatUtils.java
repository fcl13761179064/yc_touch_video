package com.common.framework.utils;

import android.provider.Settings.System;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.Utils;
import com.common.fragment.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ChatUtils {
    private static final int OTHER = 2014;
    private static final int TODAY = 6;
    private static final int YESTERDAY = 15;

    public ChatUtils() {
    }

    public static int judgeDate(Date date) {
        Calendar calendarToday = Calendar.getInstance();
        calendarToday.set(11, 0);
        calendarToday.set(12, 0);
        calendarToday.set(13, 0);
        calendarToday.set(14, 0);
        Calendar calendarYesterday = Calendar.getInstance();
        calendarYesterday.add(5, -1);
        calendarYesterday.set(11, 0);
        calendarYesterday.set(12, 0);
        calendarYesterday.set(13, 0);
        calendarYesterday.set(14, 0);
        Calendar calendarTomorrow = Calendar.getInstance();
        calendarTomorrow.add(5, 1);
        calendarTomorrow.set(11, 0);
        calendarTomorrow.set(12, 0);
        calendarTomorrow.set(13, 0);
        calendarTomorrow.set(14, 0);
        Calendar calendarTarget = Calendar.getInstance();
        calendarTarget.setTime(date);
        if (calendarTarget.before(calendarYesterday)) {
            return 2014;
        } else if (calendarTarget.before(calendarToday)) {
            return 15;
        } else {
            return calendarTarget.before(calendarTomorrow) ? 6 : 2014;
        }
    }

    private static String getWeekDay(int dayInWeek) {
        String weekDay = "";
        switch (dayInWeek) {
            case 1:
                weekDay = StringUtils.getString(R.string.fm_sunsay_format);
                break;
            case 2:
                weekDay = StringUtils.getString(R.string.fm_monday_format);
                break;
            case 3:
                weekDay = StringUtils.getString(R.string.fm_tuesday_format);
                break;
            case 4:
                weekDay = StringUtils.getString(R.string.fm_wednesday_format);
                break;
            case 5:
                weekDay = StringUtils.getString(R.string.fm_thuresday_format);
                break;
            case 6:
                weekDay = StringUtils.getString(R.string.fm_friday_format);
                break;
            case 7:
                weekDay = StringUtils.getString(R.string.fm_saturday_format);
        }

        return weekDay;
    }

    public static boolean isTime24Hour() {
        String timeFormat = System.getString(Utils.getApp().getContentResolver(), "time_12_24");
        return timeFormat != null && timeFormat.equals("24");
    }

    private static String getTimeString(long dateMillis) {
        if (dateMillis <= 0L) {
            return "";
        } else {
            Date date = new Date(dateMillis);
            String formatTime;
            if (isTime24Hour()) {
                formatTime = formatDate(date, "HH:mm");
            } else {
                Calendar calendarTime = Calendar.getInstance();
                calendarTime.setTimeInMillis(dateMillis);
                int hour = calendarTime.get(10);
                if (calendarTime.get(9) == 0) {
                    if (hour < 6) {
                        if (hour == 0) {
                            hour = 12;
                        }

                        formatTime = StringUtils.getString(R.string.fm_morning_format);
                    } else {
                        formatTime = StringUtils.getString(R.string.fm_morning_format);
                    }
                } else if (hour == 0) {
                    formatTime = StringUtils.getString(R.string.fm_noon_format);
                    hour = 12;
                } else if (hour <= 5) {
                    formatTime = StringUtils.getString(R.string.fm_noon_format);
                } else {
                    formatTime = StringUtils.getString(R.string.fm_noon_format);
                }

                int minuteInt = calendarTime.get(12);
                String minuteStr = Integer.toString(minuteInt);
                if (minuteInt < 10) {
                    minuteStr = "0" + minuteStr;
                }

                String timeStr = hour + ":" + minuteStr;
                if (Utils.getApp().getResources().getConfiguration().locale.getCountry().equals("CN")) {
                    formatTime = formatTime + timeStr;
                } else {
                    formatTime = timeStr + " " + formatTime;
                }
            }

            return formatTime;
        }
    }

    private static String getDateTimeString(long dateMillis, boolean showTime) {
        if (dateMillis <= 0L) {
            return "";
        } else {
            String formatDate = null;
            Date date = new Date(dateMillis);
            int type = judgeDate(date);
            long time = java.lang.System.currentTimeMillis();
            Calendar calendarCur = Calendar.getInstance();
            Calendar calendardate = Calendar.getInstance();
            calendardate.setTimeInMillis(dateMillis);
            calendarCur.setTimeInMillis(time);
            int month = calendardate.get(2);
            int year = calendardate.get(1);
            int weekInMonth = calendardate.get(4);
            int monthCur = calendarCur.get(2);
            int yearCur = calendarCur.get(1);
            int weekInMonthCur = calendarCur.get(4);
            switch (type) {
                case 6:
                    formatDate = getTimeString(dateMillis);
                    break;
                case 15:
                    String formatString = StringUtils.getString(R.string.fm_yesterday_format);
                    if (showTime) {
                        formatDate = formatString + " " + getTimeString(dateMillis);
                    } else {
                        formatDate = formatString;
                    }
                    break;
                case 2014:
                    if (year == yearCur) {
                        if (month == monthCur && weekInMonth == weekInMonthCur) {
                            formatDate = getWeekDay(calendardate.get(7));
                        } else if (Utils.getApp().getResources().getConfiguration().locale.getCountry().equals("CN")) {
                            formatDate = formatDate(date, "M" + StringUtils.getString(R.string.fm_month_format) + "d" + StringUtils.getString(R.string.fm_day_format));
                        } else {
                            formatDate = formatDate(date, "M/d");
                        }
                    } else if (Utils.getApp().getResources().getConfiguration().locale.getCountry().equals("CN")) {
                        formatDate = formatDate(date, "yyyy" + StringUtils.getString(R.string.fm_year_format) + "M" + StringUtils.getString(R.string.fm_month_format) + "d" + StringUtils.getString(R.string.fm_day_format));
                    } else {
                        formatDate = formatDate(date, "M/d/yy");
                    }

                    if (showTime) {
                        formatDate = formatDate + " " + getTimeString(dateMillis);
                    }
            }

            return formatDate;
        }
    }

    public static String getConversationListFormatDate(long dateMillis) {
        return getDateTimeString(dateMillis, false);
    }

    public static String getConversationFormatDate(long dateMillis) {
        return getDateTimeString(dateMillis, true);
    }

    public static boolean isShowChatTime(long currentTime, long preTime, int interval) {
        int typeCurrent = judgeDate(new Date(currentTime));
        int typePre = judgeDate(new Date(preTime));
        if (typeCurrent == typePre) {
            return currentTime - preTime > (long) (interval * 1000);
        } else {
            return true;
        }
    }

    public static String formatDate(Date date, String fromat) {
        SimpleDateFormat sdf = new SimpleDateFormat(fromat);
        return sdf.format(date);
    }
}
