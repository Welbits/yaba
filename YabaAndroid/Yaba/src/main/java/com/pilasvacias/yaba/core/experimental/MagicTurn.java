package com.pilasvacias.yaba.core.experimental;

import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.pilasvacias.yaba.util.L;

import java.lang.reflect.Field;

/**
 * Created by pablo on 10/17/13.
 * welvi-android
 */
public class MagicTurn {

    private static final Gson gson = new GsonBuilder().serializeNulls().create();

    //===========================
    //         Saving
    //===========================

    public static void save(Object target, Bundle bundle) {
        if (bundle == null)
            return;

        L.time.begin("save state for " + target.getClass().getSimpleName());

        for (Field field : target.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Save.class)) {
                String bundleKey = createBundleKey(target, field);
                Object savedObject = getField(target, field);
                String json = gson.toJson(savedObject);
                bundle.putString(bundleKey, json);
                L.time.addMark(bundleKey);
            }
        }

        L.time.end();
    }

    //===========================
    //         Restoring
    //===========================

    public static void restore(Object target, Bundle bundle) {

        if (bundle == null)
            return;

        L.time.begin("restore state for %s", target.getClass().getSimpleName());

        for (Field field : target.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Save.class)) {

                String bundleKey = createBundleKey(target, field);
                Object restoredObject;

                String tokenName = field.getAnnotation(Save.class).token();
                if (tokenName.isEmpty()) {
                    //No token, use the type of the field
                    restoredObject = gson.fromJson(bundle.getString(bundleKey), field.getType());
                } else {
                    //Token is present, use it
                    Field tokenField = findField(target, tokenName);
                    TypeToken<?> token = (TypeToken<?>) getField(target, tokenField);
                    restoredObject = gson.fromJson(bundle.getString(bundleKey), token.getType());
                }
                setField(target, field, restoredObject);
                L.time.addMark(bundleKey);
            }//end annotation process
        }//end for

        L.time.end();
    }


    private static Object readFromBundle(String key, Field field, Bundle bundle) {
        String restoring = bundle.getString(key);
        Object result = gson.fromJson(restoring, field.getType());
        L.og.d("restoring [ %s -> %s ]", key, restoring);
        L.time.addMark(key);
        return result;
    }

    //===========================
    //         Utility
    //===========================

    private static String createBundleKey(Object target, Field field) {
        StringBuilder builder = new StringBuilder()
                .append(target.getClass().getName())
                .append(".")
                .append(field.getName());

        return builder.toString();
    }


    private static void setField(Object target, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(target, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            L.og.e("failed to restore state of %s", field.getName());
        }
    }

    private static Object getField(Object target, Field field) {
        try {
            field.setAccessible(true);
            return field.get(target);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            L.og.e("failed to save state of %s", field.getName());
        }
        return null;
    }

    private static Field findField(Object target, String fieldName) {
        try {
            return target.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return null;
    }


}
