package com.pilasvacias.yaba.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.Surface;
import android.view.WindowManager;

/**
 * Created by Pablo Orgaz - 10/24/13 - pabloogc@gmail.com - https://github.com/pabloogc
 */
public class ScreenUtils {

    public static void lockScreenOrientation(Activity activity) {
        int orientation = activity.getRequestedOrientation();
        int rotation = ((WindowManager) activity.getSystemService(
                Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                break;
            case Surface.ROTATION_90:
                orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                break;
            case Surface.ROTATION_180:
                orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                break;
            default:
                orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                break;
        }

        activity.setRequestedOrientation(orientation);
    }

    public static void unlockScreenOrientation(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }


}
