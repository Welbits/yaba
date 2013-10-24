package com.pilasvacias.yaba.core.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.pilasvacias.yaba.util.L;

import java.lang.reflect.Type;

/**
 * Created by creania on 10/09/13.
 */
public class JsonPreferences {

    public static final String PREFERENCES_FILE = "yabaprefs";
    private static final Gson DEFAULT_GSON = new Gson();
    private Gson gson;
    private Context context;

    //========================
    // Constructors
    //========================

    private JsonPreferences(Context context, Gson gson) {
        this.gson = gson;
        this.context = context;
    }

    public static JsonPreferences get(Context context) {
        return get(context, DEFAULT_GSON);
    }

    public static JsonPreferences get(Context context, Gson gson) {
        return new JsonPreferences(context, gson);
    }


    //========================
    // Util
    //========================

    private String getPreferenceString(String preference, String defValue) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPrefs.getString(preference, defValue);
    }

    private void setPreferenceString(String preference, String string) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCES_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(preference, string);
        editor.commit();
    }

    //========================
    // Setters
    //========================

    public void setPreferenceObject(String preference, Object object) {
        setPreferenceString(preference, gson.toJson(object));
    }

    public void setPreferenceObject(PreferenceItemDescriptor pref, Object object) {
        setPreferenceString(pref.getName(), gson.toJson(object));
    }

    //========================
    // Getters
    //========================

    @SuppressWarnings("unchecked")
    public <T> T getPreferenceObject(PreferenceItemDescriptor pref) {
        if (pref.readAsGeneric())
            return getPreferenceObject(pref.getName(), pref.getTypeToken().getType());
        return (T) getPreferenceObject(pref.getName(), pref.getTypeClass());
    }

    public <T> T getPreferenceObject(String preference, Class<T> clazz) {
        return getPreferenceObject(preference, clazz, null);
    }

    public <T> T getPreferenceObject(String preference, Type type) {
        return getPreferenceObject(preference, type, null);
    }

    public <T> T getPreferenceObject(String preference, Class<T> clazz, T defaultValue) {
        String json = getPreferenceString(preference, null);
        if (json == null) {
            return defaultValue;
        }
        try {
        } catch (ClassCastException ex) {
            L.og.e("Tried to read as %s from preferences but class does not match. Returning default", clazz.getSimpleName());
            return defaultValue;
        }
        return gson.fromJson(json, clazz);
    }

    public <T> T getPreferenceObject(String preference, Type type, T defaultValue) {
        String json = getPreferenceString(preference, null);
        if (json == null) {
            return defaultValue;
        }
        try {
        } catch (ClassCastException ex) {
            L.og.e("Tried to read as parametrized type from preferences but class does not match. Returning default");
            return defaultValue;
        }
        return gson.fromJson(json, type);
    }

    public void clearPreferences() {
        context.getSharedPreferences(PREFERENCES_FILE, 0).edit().clear().commit();
    }

}
