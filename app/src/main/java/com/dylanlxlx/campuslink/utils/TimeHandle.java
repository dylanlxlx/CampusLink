package com.dylanlxlx.campuslink.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class TimeHandle {
    public static String formatDateTime(String dateTimeStr) {
        // 解析输入的日期时间字符串
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        // 获取当前日期时间
        LocalDateTime now = LocalDateTime.now();

        // 定义日期时间格式
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dayOfWeekFormatter = DateTimeFormatter.ofPattern("EEEE", Locale.CHINA);
        DateTimeFormatter monthDayFormatter = DateTimeFormatter.ofPattern("MM/dd");
        DateTimeFormatter yearMonthDayFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        // 判断日期时间之间的关系并返回相应格式的字符串
        if (dateTime.toLocalDate().equals(now.toLocalDate())) {
            // 如果是当天日期
            return dateTime.format(timeFormatter);
        } else if (isSameWeek(dateTime, now)) {
            // 如果在同一周
            return dateTime.format(dayOfWeekFormatter);
        } else if (dateTime.getYear() == now.getYear()) {
            // 如果在同一年
            return dateTime.format(monthDayFormatter);
        } else {
            // 如果不在同一年
            return dateTime.format(yearMonthDayFormatter);
        }
    }

    private static boolean isSameWeek(LocalDateTime date1, LocalDateTime date2) {
        // 获取每周的第一天和周数
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int weekNumber1 = date1.get(weekFields.weekOfWeekBasedYear());
        int weekNumber2 = date2.get(weekFields.weekOfWeekBasedYear());
        return date1.getYear() == date2.getYear() && weekNumber1 == weekNumber2;
    }

    public static boolean compareTime(String dateTimeStr1, String dateTimeStr2, int minutes) {
        // 解析输入的日期时间字符串
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime dateTime1 = LocalDateTime.parse(dateTimeStr1, formatter);
        LocalDateTime dateTime2 = LocalDateTime.parse(dateTimeStr2, formatter);

        // 计算两个时间之间的差值
        Duration duration = Duration.between(dateTime1, dateTime2);

        // 判断差值是否大于5分钟
        return Math.abs(duration.toMinutes()) > minutes;
    }
}
