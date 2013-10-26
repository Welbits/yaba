package com.pilasvacias.yaba.util;

import java.text.SimpleDateFormat;

/**
 * Created by IzanRodrigo on 18/10/13.
 */
public class DateUtils {
    private static final String DATE_FORMAT = "dd-MM-yyyy";

    /**
     * @return today date like {@code 29-04-1993}
     */
    public static String getToday() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        return dateFormat.format(new java.util.Date());
    }
}
