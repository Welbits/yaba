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
        private boolean error = false;
        private String tag;
        private long init;
        private long last;
        private List<Long> timeMarks = new LinkedList<Long>();
        private List<String> stringMarks = new LinkedList<String>();

        @Override public void begin(String tag, Object... args) {
            reset();
            this.tag = String.format(tag, args);
        }

        private void reset() {
            error = false;
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
            String separator = String.format("·----------> %s", tag);
            StringBuilder builder;
            if (!error)
                Log.d("TimerProfiler", separator);
            else
                Log.e("TimerProfiler", separator);
            long sum = 0;
            long total = System.currentTimeMillis() - init;
            for (int i = 0; i < timeMarks.size(); i++) {
                sum += timeMarks.get(i);
                builder = new StringBuilder();
                builder.append("| +").append(format(timeMarks.get(i))).append(" | ").append(percent(total, timeMarks.get(i))).append(" \t")
                        .append(stringMarks.get(i));
                if (!error)
                    Log.d("TimerProfiler", builder.toString());
                else
                    Log.e("TimerProfiler", builder.toString());
            }

            builder = new StringBuilder();
            builder.append("···").append(total).append(" ms <==== ").append(tag);
            if (!error)
                Log.d("TimerProfiler", builder.toString());
            else
                Log.e("TimerProfiler", builder.toString());
            //Log.d("TimerProfiler", separator);
            reset();
        }

        @Override public void error() {
            error = true;
            end();
        }

        private String format(long time) {
            return String.format("%-4d", time);
        }

        private String percent(long total, long part) {
            //String regex = "\\G0";
            String regex = "^0(\\d)";
            return new DecimalFormat("00%").format(((double) (part)) / total).replaceAll(regex, " $1");
        }

    };
    //I am special!
    public static TimeProfiler PROD = new TimeProfiler() {
        @Override public void begin(String tag, Object... args) {

        }

        @Override public void addMark(String mark, Object... args) {

        }

        @Override public void end() {

        }

        @Override public void error() {

        }
    };

    void begin(String tag, Object... args);

    void addMark(String mark, Object... args);

    void end();

    void error();
}
