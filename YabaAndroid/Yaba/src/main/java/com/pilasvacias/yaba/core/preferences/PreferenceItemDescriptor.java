package com.pilasvacias.yaba.core.preferences;

import com.google.gson.reflect.TypeToken;

/**
 * Created by Pablo Orgaz - 10/24/13 - pabloogc@gmail.com - https://github.com/pabloogc
 */
public interface PreferenceItemDescriptor {

    boolean readAsGeneric();

    Class<?> getTypeClass();

    TypeToken<?> getTypeToken();

    String getName();
}
