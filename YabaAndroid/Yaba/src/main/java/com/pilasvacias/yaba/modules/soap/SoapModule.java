package com.pilasvacias.yaba.modules.soap;

import com.pilasvacias.yaba.modules.network.VolleyModule;
import com.pilasvacias.yaba.screens.MainActivity;
import com.pilasvacias.yaba.screens.ProbaActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by pablo on 10/11/13.
 * welvi-android
 *
 */
@Module(injects =
        {
                MainActivity.class,
                MainActivity.DummyFragment.class,
                ProbaActivity.class
        },
        includes = VolleyModule.class,
        complete = false,
        library = true)
public class SoapModule {

    @Provides @Singleton EmtEnvelopeSerializer provideEnvelopeSerializer() {
        return new EmtEnvelopeSerializer();
    }

}
