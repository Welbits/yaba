package com.pilasvacias.yaba.core.preferences;

import com.google.gson.reflect.TypeToken;

/**
 * Created by Pablo Orgaz - 10/24/13 - pabloogc@gmail.com - https://github.com/pabloogc
 */
public enum YabaPreferences implements PreferenceItemDescriptor {

    stringPreference(String.class);
    private TypeToken<?> typeToken;
    private Class<?> clazz;
    private String name;

    YabaPreferences(Class<?> clazz) {
        this.clazz = clazz;
        this.typeToken = null;
        this.name = name();
    }


    YabaPreferences(TypeToken<?> typeToken) {
        this.clazz = null;
        this.typeToken = typeToken;
        this.name = name();
    }

    @Override public boolean readAsGeneric() {
        return typeToken != null;
    }

    @Override public Class<?> getTypeClass() {
        return clazz;
    }

    @Override public TypeToken<?> getTypeToken() {
        return typeToken;
    }

    @Override public String getName() {
        return name;
    }
}
