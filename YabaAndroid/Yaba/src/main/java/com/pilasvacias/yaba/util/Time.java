package com.pilasvacias.yaba.util;

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
    private long sum = 0;

    private Time(long init) {
        this.sum = init;
    }

    public static long seconds(double factor) {
        return (long) (ONE_SECOND * factor);
    }

    public static long minutes(double factor) {
        return (long) (ONE_MINUTE * factor);
    }

    public static long hours(double factor) {
        return (long) (ONE_HOUR * factor);
    }

    public static long days(double factor) {
        return (long) (ONE_DAY * factor);
    }

    public static long weeks(double factor) {
        return (long) (ONE_WEEK * factor);
    }

    public static long millis(double factor) {
        return (long) factor;
    }

    public static TimeBuilder w(int amount) {
        return new TimeBuilder(amount * ONE_WEEK);
    }

    public static TimeBuilder d(int amount) {
        return new TimeBuilder(amount * ONE_DAY);
    }

    public static TimeBuilder h(int amount) {
        return new TimeBuilder(amount * ONE_HOUR);
    }

    public static TimeBuilder m(int amount) {
        return new TimeBuilder(amount * ONE_MINUTE);
    }

    public static TimeBuilder s(int amount) {
        return new TimeBuilder(amount * ONE_SECOND);
    }

    public long get() {
        return sum;
    }

    public static class TimeBuilder {
        private long sum;

        public TimeBuilder(long sum) {
            this.sum = sum;
        }

        public long get() {
            return sum;
        }

        public TimeBuilder w(final int amount) {
            sum += amount * ONE_WEEK;
            return this;
        }

        public TimeBuilder d(final int amount) {
            sum += amount * ONE_DAY;
            return this;
        }

        public TimeBuilder h(final int amount) {
            sum += amount * ONE_HOUR;
            return this;
        }

        public TimeBuilder m(final int amount) {
            sum += amount * ONE_MINUTE;
            return this;
        }

        public TimeBuilder s(final int amount) {
            sum += amount * ONE_SECOND;
            return this;
        }

        public TimeBuilder ms(final int amount) {
            sum += amount;
            return this;
        }
    }


}
