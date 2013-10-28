package com.pilasvacias.yaba.core.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by creania on 10/09/13.
 */
public class JsonPreferences {

    public static final String PREFERENCES_FILE = "yabaprefs";
    public static final Gson DEFAULT_GSON = new Gson();
    private Gson gson;
    private Context context;
    private String prefsFile;


    //========================
    // Constructors
    //========================

    public JsonPreferences(Context context, String prefsFile, Gson gson) {
        this.gson = gson;
        this.context = context;
        this.prefsFile = prefsFile;
    }

    public static JsonPreferences get(Context context) {
        return new JsonPreferences(context, PREFERENCES_FILE, DEFAULT_GSON);
    }

    //========================
    // Util
    //========================

    private String getPreferenceString(String preference, String defValue) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(prefsFile, Context.MODE_PRIVATE);
        return sharedPrefs.getString(preference, defValue);
    }

    private void setPreferenceString(String preference, String string) {
        SharedPreferences settings = context.getSharedPreferences(prefsFile, 0);
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
            return (T) getPreferenceObject(pref.getName(), pref.getTypeToken());
        return (T) getPreferenceObject(pref.getName(), pref.getTypeClass());
    }

    //===============================
    //Using a Class
    //===============================

    public <T> T getPreferenceObject(String preference, Class<T> clazz) {
        return getPreferenceObject(preference, clazz, null);
    }

    public <T> T getPreferenceObject(String preference, Class<T> clazz, T defaultValue) {
        String json = getPreferenceString(preference, null);
        if (json == null) {
            return defaultValue;
        }
        return gson.fromJson(json, clazz);
    }

    //===============================
    //Using a TypeToken for generics
    //===============================

    public <T> T getPreferenceObject(String preference, TypeToken<T> typeToken) {
        return getPreferenceObject(preference, typeToken, null);
    }

    public <T> T getPreferenceObject(String preference, TypeToken<T> typeToken, T defaultValue) {
        String json = getPreferenceString(preference, null);
        if (json == null) {
            return defaultValue;
        }
        return gson.fromJson(json, typeToken.getType());
    }

    public void clearPreferences() {
        context.getSharedPreferences(prefsFile, 0).edit().clear().commit();
    }

}
