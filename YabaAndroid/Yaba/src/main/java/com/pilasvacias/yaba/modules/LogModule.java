package com.pilasvacias.yaba.modules;

import com.pilasvacias.yaba.BuildConfig;
import com.pilasvacias.yaba.screens.MainActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import timber.log.Timber;

/**
 * Created by pablo on 10/9/13.
 * welvi-android
 */
@Module(injects = {MainActivity.class}, library = true)
public class LogModule {

    @Provides @Singleton Timber provideTimber() {
        return BuildConfig.DEBUG ? Timber.DEBUG : Timber.PROD;
    }

}
