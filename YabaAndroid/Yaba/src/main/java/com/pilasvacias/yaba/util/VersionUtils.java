package com.pilasvacias.yaba.util;

import android.os.Build;

/**
 * Created by Izan Rodrigo on 22/09/13.
 */
public class VersionUtils {

    public static boolean isVersionGreaterThan(int version) {
        return Build.VERSION.SDK_INT > version;
    }

    public static boolean isVersionGreaterOrEqualThan(int version) {
        return Build.VERSION.SDK_INT >= version;
    }

    public static boolean isVersionEqualTo(int version) {
        return Build.VERSION.SDK_INT == version;
    }

    public static boolean isVersionLowerThan(int version) {
        return Build.VERSION.SDK_INT < version;
    }

    public static boolean isVersionLowerOrEqualThan(int version) {
        return Build.VERSION.SDK_INT <= version;
    }

    public static boolean compare(int version, CompareVersionMode mode) {
        boolean resul = false;
        switch (mode) {
            case GREATER:
                resul = isVersionGreaterThan(version);
                break;
            case GREATER_OR_EQUAL:
                resul = isVersionGreaterOrEqualThan(version);
                break;
            case EQUAL:
                resul = isVersionEqualTo(version);
                break;
            case LOWER:
                resul = isVersionLowerThan(version);
                break;
            case LOWER_OR_EQUAL:
                resul = isVersionLowerOrEqualThan(version);
                break;
        }
        return resul;
    }

    public enum CompareVersionMode {
        GREATER,
        GREATER_OR_EQUAL,
        EQUAL,
        LOWER,
        LOWER_OR_EQUAL
    }
}
