package com.axfex.dorkout.util;

import android.content.res.Resources;
import android.os.SystemClock;

import com.axfex.dorkout.R;
import com.axfex.dorkout.WorkoutApplication;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import androidx.core.content.ContextCompat;

/**
 * Created by alexanderkozlov on 11/27/17.
 */

public final class FormatUtils {
    public static int composeWeekDays(ArrayList<Boolean> isDayActive) {
        int result = 0;
        if (isDayActive == null) {
            return result;
        }
        for (int i = 0; i < 7; ++i) {
            result |= ((isDayActive.get(i) ? 1 : 0) << i);
        }
        return result;
    }

    public static ArrayList<Boolean> parseWeekDays(Integer fromBase) {
        ArrayList<Boolean> result = new ArrayList<>();
        for (Integer i = 0; i < 7; ++i) {
            if (fromBase!=null) {
                result.add(((fromBase >> i) & 1) == 1);
            } else {
                result.add(false);
            }
        }
        return result;
    }

    public static Long getTimeMillis(String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm",Locale.getDefault());
        if (time.length() == 0) {
            return 0L;
        }

        try {
            Date date = dateFormat.parse(time);
            return date.getTime();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0L;
    }

    public static String getTimeString(Long time_ms) {
            if (time_ms == null || time_ms < 0){
            return
                    WorkoutApplication.getContext().getString(R.string.empty_time);
        }
        String timeFormat;
        if (time_ms<3600000) {
            timeFormat="mm:ss.S";
        }else {
            timeFormat="h:mm:s";
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(timeFormat, Locale.getDefault());

        try {
            Date date = new Date(time_ms);
            return dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static Integer sec(Long milliseconds){
        if (milliseconds == null) {
            return null;
        }
        milliseconds=milliseconds/1000;
        return milliseconds.intValue();
    }
    public static Long now(){
        return SystemClock.elapsedRealtime();
    }

    public static int unwrap(Long l){
        if (l!=null) return l.intValue();
        return 0;
    }

}
