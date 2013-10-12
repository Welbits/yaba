package com.pilasvacias.yaba.modules.util;

import android.content.Context;
import android.location.LocationManager;

import com.pilasvacias.yaba.application.YabaApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by pablo on 10/9/13.
 * welvi-android
 */
@Module(injects = {},
        library = true)
public class LocationModule {

    private YabaApplication app;

    public LocationModule(YabaApplication app) {
        this.app = app;
    }

    @Provides @Singleton LocationManager provideLocationManager() {
        return (LocationManager) app.getSystemService(Context.LOCATION_SERVICE);
    }
}
