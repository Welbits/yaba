package com.pilasvacias.yaba.util;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Izan Rodrigo on 22/09/13.
 */
public class WToast {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static String fromObject(Object object) {
        return "\n" + gson.toJson(object) + "\n";
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
        Toast.makeText(context, textResource, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(Context context, int textResource) {
        Toast.makeText(context, textResource, Toast.LENGTH_LONG).show();
    }

}
