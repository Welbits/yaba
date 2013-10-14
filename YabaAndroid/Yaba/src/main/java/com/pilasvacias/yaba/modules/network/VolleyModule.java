package com.pilasvacias.yaba.modules.network;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.pilasvacias.yaba.modules.util.ContextModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by pablo on 10/9/13.
 * welvi-android
 */
@Module(includes = ContextModule.class, library = true)
public class VolleyModule {

    @Provides @Singleton RequestQueue providesRequestQueue(Context context) {
        return Volley.newRequestQueue(context);
    }


}
