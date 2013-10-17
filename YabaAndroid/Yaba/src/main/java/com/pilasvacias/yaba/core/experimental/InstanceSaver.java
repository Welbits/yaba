package com.pilasvacias.yaba.core.experimental;

import android.os.Bundle;

import com.google.gson.Gson;
import com.pilasvacias.yaba.modules.util.L;

import java.lang.reflect.Field;

/**
 * Created by pablo on 10/17/13.
 * welvi-android
 */
public class InstanceSaver {

    private static final Gson gson = new Gson();

    public static void save(Object target, Bundle bundle) {
        for (Field field : target.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(SaveState.class)) {

                Object saving = getField(field, target);
                String key = createBundleKey(target, field);
                String value = gson.toJson(saving);

                L.og.d("saving %s -> %s", field.getName(), value);
                bundle.putString(key, value);
            }
        }
    }

    public static void restore(Object target, Bundle bundle) {
        for (Field field : target.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(SaveState.class)) {

                String restoring = bundle.getString(createBundleKey(target, field));
                String key = target.getClass().getName() + field.getName();
                Object value = gson.fromJson(restoring, field.getType());
                L.og.d("restoring %s -> %s", field.getName(), value);
                setField(field, target, value);
            }
        }
    }

    private static Object getField(Field field, Object target) {
        try {
            field.setAccessible(true);
            return field.get(target);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            L.og.e("failed to save state of %s", field.getName());
        }
        return null;
    }

    private static void setField(Field field, Object target, Object value) {
        try {
            field.setAccessible(true);
            field.set(target, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            L.og.e("failed to restore state of %s", field.getName());
        }
    }

    private static String createBundleKey(Object target, Field field) {
        return target.getClass().getSimpleName() + "." + field.getName();
    }


}
