package com.axys.redeflexmobile.shared.util;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import timber.log.Timber;

/**
 * @author Vitor Otero on 27/04/18.
 */

public class DateUtils {

    public static final String DATA_MINIMA_BR = "01/01/1753";
    public static final String DATA_SEPARATOR_BAR = "/";
    public static final String DATE_TIME_FULL_WEB_SERVICE = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_TIME_FULL_WEB_SERVICE_MOCK = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'";
    public static final String DATE_DAY = "dd";
    public static final String DATE_YEAR = "yyyy";
    public static final String DATE_LOCAL = "dd/MM/yyyy";
    public static final String DATE_LOCAL_SIMPLE = "dd/MM/yy";
    public static final String DATE_TIME_LOCAL = "dd/MM/yyyy HH:mm:ss";
    private static final Locale LOCALE = new Locale("pt", "BR");
    private static final String DATE_PATTERN = "(0?[1-9]|1[012]) [/.-] (0?[1-9]|[12][0-9]|3[01]) [/.-] ((19|20)\\d\\d)";

    private DateUtils() {
    }

    public static String insereBarraData(String pDate) {
        if (pDate.trim().length() == 8 && !pDate.contains(DATA_SEPARATOR_BAR)) {
            return pDate.substring(0, 2)
                    + DATA_SEPARATOR_BAR
                    + pDate.substring(2, 4)
                    + DATA_SEPARATOR_BAR
                    + pDate.substring(4);
        }

        return pDate;
    }

    public static String toSimpleBrDate(String date) {
        try {
            DateTimeFormatter converter = DateTimeFormatter.ofPattern(DATE_TIME_FULL_WEB_SERVICE_MOCK);
            LocalDate localDate = LocalDate.from(converter.parse(date));
            return localDate.format(DateTimeFormatter.ofPattern(DATE_LOCAL_SIMPLE));
        } catch (Exception e) {
            return date;
        }
    }

    public static boolean isDateValid(String date, Boolean before) {
        return isDateValid(date, before, null);
    }

    public static boolean isDateValid(String date, Boolean before, Integer limitYear) {
        if (StringUtils.isEmpty(date)) {
            return false;
        }

        try {

            DateFormat df = new SimpleDateFormat(DATE_LOCAL, LOCALE);
            df.setLenient(false);
            df.parse(date);

            if (limitYear != null && isDateBeforeYear(date, limitYear)) {
                return false;
            }

            if (before) {
                return isDateBeforeToday(date);
            }

            return true;

        } catch (ParseException e) {
            return false;
        }
    }

    private static boolean isDateBeforeToday(String date) {
        try {
            DateTimeFormatter converter = DateTimeFormatter.ofPattern(DATE_LOCAL);
            LocalDate localDate = LocalDate.from(converter.parse(date));
            return localDate.isBefore(LocalDate.now());
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isDateBeforeYear(String date, int year) {
        try {
            DateTimeFormatter converter = DateTimeFormatter.ofPattern(DATE_LOCAL);
            LocalDate localDate = LocalDate.from(converter.parse(date));
            LocalDate localDateCompare = LocalDate.of(year, 1, 1);
            return localDate.isBefore(localDateCompare);
        } catch (Exception e) {
            return false;
        }
    }

    public static int getDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static boolean validate(final String date) {
        Matcher matcher = Pattern.compile(DATE_PATTERN).matcher(date);
        if (!matcher.matches()) return false;
        matcher.reset();
        if (!matcher.find()) return false;

        String day = matcher.group(1);
        String month = matcher.group(2);
        int year = Integer.parseInt(matcher.group(3));

        if (day.equals("31") && (month.equals("4") || month.equals("6") || month.equals("9") ||
                month.equals("11") || month.equals("04") || month.equals("06") || month.equals("09"))) {
            return false;
        } else if ((month.equals("2") || month.equals("02")) && year % 4 == 0) {
            return !day.equals("30") && !day.equals("31");
        } else if ((month.equals("2") || month.equals("02"))) {
            return !day.equals("29") && !day.equals("30") && !day.equals("31");
        } else {
            return true;
        }
    }

    public static Date formatToDateBr(String date) {
        if (date == null) {
            return null;
        }

        if (!validate(date)) {
            return null;
        }

        SimpleDateFormat simpledateformat = new SimpleDateFormat(DATE_LOCAL, Locale.getDefault());
        try {
            return simpledateformat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Calendar dateToCalendar(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static Date calendarToDate(Calendar calendar) {
        return calendar.getTime();
    }

    public static LocalDateTime getLocalDateTimeFromHour(String hourMinutes) {
        if (StringUtils.isEmpty(hourMinutes)) {
            return null;
        }

        try {
            String[] time = hourMinutes.split(":");
            Calendar now = Calendar.getInstance();
            now.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
            now.set(Calendar.MINUTE, Integer.parseInt(time[1]));
            now.set(Calendar.SECOND, 0);

            DateFormat format = new SimpleDateFormat(DATE_TIME_LOCAL, Locale.getDefault());
            String dateString = format.format(now.getTime());

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_LOCAL);
            return LocalDateTime.parse(dateString, dateTimeFormatter);
        } catch (Exception e) {
            Timber.d(e);
            return null;
        }
    }
}
