package com.pilasvacias.yaba.core.experimental;

import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pilasvacias.yaba.util.L;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * Created by pablo on 10/17/13.
 * welvi-android
 */
public class InstanceSaver {

    private static final Gson gson = new GsonBuilder().serializeNulls().create();

    //===========================
    //         Saving
    //===========================

    public static void save(Object target, Bundle bundle) {
        if (bundle == null)
            return;

        L.time.begin("save state for " + target.getClass().getSimpleName());
        for (Field field : target.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(SaveObjectState.class)) {
                saveObjectState(target, bundle, field);
            } else if (field.isAnnotationPresent(SaveCollectionState.class)) {
                saveCollectionState(target, bundle, field);
            }
        }
        L.time.end();
    }

    private static void saveObjectState(Object target, Bundle bundle, Field field) {
        Object savingObject = getField(field, target);
        if (savingObject == null)
            return;
        String key = createBundleKey(target, field, 0);
        saveToBundle(key, savingObject, bundle);
    }

    private static void saveCollectionState(Object target, Bundle bundle, Field field) {
        Collection<?> collection = (Collection<?>) getField(field, target);

        if (collection == null)
            return;

        String collectionNameKey = createBundleKey(target, field, -1);
        bundle.putString(collectionNameKey, collection.getClass().getName());
        L.time.addMark(collectionNameKey);

        String collectionSizeKey = createBundleKey(target, field, -2);
        bundle.putInt(collectionSizeKey, collection.size());
        L.time.addMark(collectionSizeKey);

        int i = 0;
        for (Object item : collection) {
            String key = createBundleKey(target, field, i);
            saveToBundle(key, item, bundle);
            i++;
        }
    }

    private static void saveToBundle(String key, Object objectToSave, Bundle bundle) {
        L.og.d("saving [ %s -> %s ]", key, objectToSave.toString());
        String value = gson.toJson(objectToSave);
        L.time.addMark(key);
        bundle.putString(key, value);
    }

    //===========================
    //         Restoring
    //===========================

    public static void restore(Object target, Bundle bundle) {

        if (bundle == null)
            return;

        L.time.begin("restore state for " + target.getClass().getSimpleName());
        for (Field field : target.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(SaveObjectState.class)) {
                restoreObjectState(target, bundle, field);
            } else if (field.isAnnotationPresent(SaveCollectionState.class)) {
                restoreCollectionState(target, bundle, field);
            }
        }
        L.time.end();
    }

    private static void restoreObjectState(Object target, Bundle bundle, Field field) {
        String key = createBundleKey(target, field, 0);
        Object value = readFromBundle(key, field, bundle);
        setField(field, target, value);
    }

    private static void restoreCollectionState(Object target, Bundle bundle, Field field) {
        String collectionNameKey = createBundleKey(target, field, -1);
        String collectionClassName = bundle.getString(collectionNameKey);
        L.time.addMark(collectionNameKey);


        String collectionSizeKey = createBundleKey(target, field, -2);
        Integer collectionSize = bundle.getInt(collectionSizeKey, 0);
        L.time.addMark(collectionSizeKey);

        if (collectionClassName == null)
            return;
        try {
            Class<? extends Collection> collectionClass = (Class<? extends Collection>) Class.forName(collectionClassName);
            Collection collection = collectionClass.newInstance();


            for (int i = 0; i < collectionSize; i++) {
                String key = createBundleKey(target, field, i);
                Object value = gson.fromJson(bundle.getString(key), field.getAnnotation(SaveCollectionState.class).type());
                collection.add(value);
                L.time.addMark(key);
            }

            setField(field, target, collection);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
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

    private static String createBundleKey(Object target, Field field, int position) {
        StringBuilder builder = new StringBuilder()
                .append(target.getClass().getSimpleName())
                .append(".")
                .append(field.getName())
                .append(".").append(position);

        return builder.toString();
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


}
