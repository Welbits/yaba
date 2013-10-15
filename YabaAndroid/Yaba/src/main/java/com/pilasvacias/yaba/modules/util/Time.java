package com.pilasvacias.yaba.modules.util;

/**
 * Created by pablo on 10/12/13.
 * welvi-android
 */
public class Time {

    private static final long ONE_SECOND = 1000;
    private static final long ONE_MINUTE = ONE_SECOND * 60;
    private static final long ONE_HOUR = ONE_MINUTE * 60;
    private static final long ONE_DAY = ONE_HOUR * 24;
    private static final long ONE_WEEK = ONE_DAY * 7;

    public static long seconds(float factor) {
        return (long) (ONE_SECOND * factor);
    }

    public static long mins(float factor) {
        return (long) (ONE_MINUTE * factor);
    }

    public static long hours(float factor) {
        return (long) (ONE_HOUR * factor);
    }

    public static long days(float factor) {
        return (long) (ONE_DAY * factor);
    }

    public static long weeks(float factor) {
        return (long) (ONE_WEEK * factor);
    }

}
