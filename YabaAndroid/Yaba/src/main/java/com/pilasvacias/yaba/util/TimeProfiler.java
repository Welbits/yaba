package com.pilasvacias.yaba.util;

import android.util.Log;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by pablo on 10/17/13.
 * welvi-android
 */
public interface TimeProfiler {

    public static TimeProfiler DEBUG = new TimeProfiler() {

        private String tag;
        private long init;
        private long last;
        private List<Long> timeMarks = new LinkedList<Long>();
        private List<String> stringMarks = new LinkedList<String>();

        @Override public void begin(String tag, Object... args) {
            reset();
            this.tag = String.format(tag, args);
        }

        public void reset() {
            tag = null;
            last = init = System.currentTimeMillis();
            timeMarks.clear();
            stringMarks.clear();
        }

        @Override public void addMark(String mark, Object... args) {
            stringMarks.add(String.format(mark, args));
            long now = System.currentTimeMillis();
            timeMarks.add(now - last);
            last = now;
        }

        @Override public void end() {
            String separator = String.format("·----------> begin %s", tag);
            StringBuilder builder;
            Log.d("TimerProfiler", separator);
            long sum = 0;
            long total = System.currentTimeMillis() - init;
            for (int i = 0; i < timeMarks.size(); i++) {
                sum += timeMarks.get(i);
                builder = new StringBuilder();
                builder.append("| +").append(format(timeMarks.get(i))).append(" | ").append(percent(total, timeMarks.get(i))).append(" \t")
                        .append(stringMarks.get(i));
                Log.d("TimerProfiler", builder.toString());
            }

            builder = new StringBuilder();
            builder.append("···").append(total).append(" ms <==== end ").append(tag);
            Log.d("TimerProfiler", builder.toString());
            //Log.d("TimerProfiler", separator);
            reset();
        }

        private String format(long time) {
            return String.format("%-4d", time);
        }

        private String percent(long total, long part) {
            return new DecimalFormat("##%").format(((double) (part)) / total);
        }

    };
    public static TimeProfiler PROD = new TimeProfiler() {
        @Override public void begin(String tag, Object... args) {

        }

        @Override public void addMark(String mark, Object... args) {

        }

        @Override public void end() {

        }
    };

    void begin(String tag, Object... args);

    void addMark(String mark, Object... args);

    void end();
}
