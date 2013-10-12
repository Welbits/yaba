package com.pilasvacias.yaba.modules.soap;

import com.pilasvacias.yaba.screens.MainActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by pablo on 10/11/13.
 * welvi-android
 */
@Module(injects =
        {
                MainActivity.class
        },
        library = true)
public class SoapModule {

    @Provides @Singleton EnvelopeSerializer provideEnvelopeFactory() {
        return new EnvelopeSerializer();
    }

}
