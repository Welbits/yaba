package com.pilasvacias.yaba.util;

import com.pilasvacias.yaba.application.YabaApplication;

/**
 * Created by Izan Rodrigo on 26/09/13.
 */
public class BusUtils {

    /**
     * Register bus if not registered yet.
     * @param isBusRegistered
     * @param object
     * @return
     */
    public static boolean registerBus(boolean isBusRegistered, Object object) {
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

    /**
     * Unregister bus if is registered.
     * @param isBusRegistered
     * @param object
     */
    public static void unregisterBus(boolean isBusRegistered, Object object) {
        if (isBusRegistered) {
            YabaApplication.getBus().unregister(object);
        }
    }

    /**
     * Post bus event, notifying subscribed classes.
     * @param isBusRegistered
     * @param event
     */
    public static void post(boolean isBusRegistered, Object event) {
        if (isBusRegistered) {
            YabaApplication.getBus().post(event);
        }
    }
}
