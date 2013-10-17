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
        private List<Long> timeMarks = new LinkedList<Long>();
        private List<String> stringMarks = new LinkedList<String>();

        @Override public void begin(String tag) {
            clear();
            this.tag = tag;
        }

        public void clear() {
            tag = null;
            init = System.currentTimeMillis();
            timeMarks.clear();
            stringMarks.clear();
        }

        @Override public void addMark(String mark) {
            stringMarks.add(mark);
            if (timeMarks.isEmpty())
                timeMarks.add(System.currentTimeMillis() - init);
            else
                timeMarks.add(System.currentTimeMillis() - timeMarks.get(timeMarks.size() - 1));
        }

        @Override public void end() {
            String separator = "====================================";
            StringBuilder builder;
            Log.d("TimerProfiler", separator);
            long sum = 0;
            long total = System.currentTimeMillis() - init;
            for (int i = 0; i < timeMarks.size(); i++) {
                sum += timeMarks.get(i);
                builder = new StringBuilder();
                builder.append("+").append(format(timeMarks.get(i))).append(" | ").append(i + 1).append(" ")
                        .append(stringMarks.get(i)).append(" ").append(percent(total, sum));
                Log.d("TimerProfiler", builder.toString());
            }
            builder = new StringBuilder();
            if (timeMarks.isEmpty())
                builder.append(" ");
            else
                builder.append("=");
            builder.append(format(total)).append(" | # ").append(tag);
            Log.d("TimerProfiler", builder.toString());
            clear();
        }

        private String format(long time) {
            return String.format("%-4d", time);
        }

        private String percent(long total, long part) {
            return new DecimalFormat("#.%").format(((double) (total)) / part);
        }

    };
    public static TimeProfiler PROD = new TimeProfiler() {
        @Override public void begin(String tag) {

        }

        @Override public void addMark(String mark) {

        }

        @Override public void end() {

        }
    };

    void begin(String tag);

    void addMark(String mark);

    void end();
}
