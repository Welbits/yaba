package com.pilasvacias.yaba.core.experimental;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by pablo on 10/17/13.
 * welvi-android
 */
@Retention(RUNTIME) @Target(FIELD)
public @interface SaveState {
}
