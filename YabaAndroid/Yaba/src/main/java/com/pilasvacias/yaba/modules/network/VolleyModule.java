package com.pilasvacias.yaba.modules.network;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.pilasvacias.yaba.modules.util.ContextModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Pablo Orgaz - 10/21/13 - pabloogc@gmail.com - https://github.com/pabloogc
 * <p/>
 * <p/>
 * A simple module depending on {@link com.pilasvacias.yaba.modules.util.ContextModule}
 * that is meant to be included in modules that need network.
 */
@Module(includes = ContextModule.class, library = true)
public class VolleyModule {

    @Provides @Singleton RequestQueue providesRequestQueue(Context context) {
        return Volley.newRequestQueue(context);
    }


}
