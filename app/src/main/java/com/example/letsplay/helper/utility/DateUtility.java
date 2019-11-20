package com.example.letsplay.helper.utility;

import android.util.Log;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by rysha on 25.07.2017.
 */

public class DateUtility {

    public static String dateToDayMonth(Calendar date) {

        return getDayMonthFormat().format(date.getTime());
    }

    public static String getMonthString(int mon) {
        String month = "fail";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (mon >= 0 && mon <= 11) {
            month = months[mon];
        }
        return month;
    }

    public static Calendar stringToCalendar(SimpleDateFormat format, String input) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(format.parse(input));
        return calendar;
    }

    public static SimpleDateFormat getDayMonthFormat() {
        return new SimpleDateFormat("dd MMMM");
    }

    public static SimpleDateFormat getDashedYMDdateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    }

    public static SimpleDateFormat getSlashedDMYdateFormat() {
        return new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
    }

    public static SimpleDateFormat getISO8601DateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    }

    public static String convertDashYMDtoSlashDMY(String dashedYMDstring) {
        String slashedDMYstring = null;
        SimpleDateFormat dashedDMYformatter = getDashedYMDdateFormat();
        SimpleDateFormat slashedYMDformatter = getSlashedDMYdateFormat();
        try {
            slashedDMYstring = slashedYMDformatter.format(dashedDMYformatter.parse(dashedYMDstring));
        } catch (ParseException e) {
            Log.e("DATE", "cant parsed date", e);
        }
        return slashedDMYstring;
    }
}
