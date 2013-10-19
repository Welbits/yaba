package com.pilasvacias.yaba.util;

import com.pilasvacias.yaba.application.YabaApplication;

/**
 * Created by Izan Rodrigo on 26/09/13.
 */
public class BusUtils {
    public static boolean checkBusRegistered(boolean isBusRegistered, Object object) {
        if (!isBusRegistered) {
            try {
                YabaApplication.getBus().register(object);
                isBusRegistered = true;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return isBusRegistered;
    }

    public static void unregisterBus(boolean isBusRegistered, Object object) {
        if (isBusRegistered) {
            YabaApplication.getBus().unregister(object);
        }
    }

    public static void post(boolean isBusRegistered, Object event) {
        if (isBusRegistered) {
            YabaApplication.getBus().post(event);
        }
    }
}
