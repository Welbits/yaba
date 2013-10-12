package com.pilasvacias.yaba.modules.util;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module(library = true)
public class ContextModule {

    private final Context mContext;

    public ContextModule(Context context) {
        mContext = context;
    }

    @Provides
    public Context provideContext() {
        return mContext;
    }

}