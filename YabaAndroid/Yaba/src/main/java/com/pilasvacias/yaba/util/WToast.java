package com.pilasvacias.yaba.util;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Method;

/**
 * Created by Izan Rodrigo on 22/09/13.
 */
public class WToast {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static String fromObject(Object object) {
        return "\n" + gson.toJson(object) + "\n";
    }

    private static boolean isTextResource(Context context, int resource) {
        try {
            return context.getResources().getText(resource) != null;
        } catch (Resources.NotFoundException ex) {
            return false;
        }
    }

    public static void showShort(Context context, Object object) {
        Toast.makeText(context, fromObject(object), Toast.LENGTH_SHORT).show();
    }

    public static void showLong(Context context, Object object) {
        Toast.makeText(context, fromObject(object), Toast.LENGTH_LONG).show();
    }

    public static void showShort(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static void showShort(Context context, int textResource) {
        if (isTextResource(context, textResource)) {
            Toast.makeText(context, textResource, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showLong(Context context, int textResource) {
        if (isTextResource(context, textResource)) {
            Toast.makeText(context, textResource, Toast.LENGTH_LONG).show();
        }
    }

    public static void showShort(Context context, String format, Object... args) {
        showShort(context, String.format(format, args));
    }

    public static void showLong(Context context, String format, Object... args) {
        showLong(context, String.format(format, args));
    }

    ///////////////////////////////////////////////
    // CRAZY METHODS (WITHOUT CONTEXT) MAY CRASH //
    ///////////////////////////////////////////////
    public static void crazyShort(Object object) {
        showShort(getContext(), object);
    }

    public static void crazyLong(Object object) {
        showLong(getContext(), object);
    }

    public static void crazyShort(String text) {
        showShort(getContext(), text);
    }

    public static void crazyLong(String text) {
        showLong(getContext(), text);
    }

    public static void crazyShort(int textResource) {
        showShort(getContext(), textResource);
    }

    public static void crazyLong(int textResource) {
        showLong(getContext(), textResource);
    }

    public static void crazyShort(String format, Object... args) {
        showShort(getContext(), String.format(format, args));
    }

    public static void crazyLong(String format, Object... args) {
        showLong(getContext(), String.format(format, args));
    }

    private static Context getContext() {
        try {
            final Class<?> activityThreadClass =
                    Class.forName("android.app.ActivityThread");
            final Method method = activityThreadClass.getMethod("currentApplication");
            return (Application) method.invoke(null, (Object[]) null);
        } catch (Exception e) {
            return null;
        }
    }

}
