package com.pilasvacias.yaba.modules.emt;

import com.android.volley.RequestQueue;
import com.pilasvacias.yaba.modules.emt.builders.EmtRequestManager;
import com.pilasvacias.yaba.modules.network.VolleyModule;
import com.pilasvacias.yaba.screens.MainActivity;
import com.pilasvacias.yaba.screens.ProbaActivity;
import com.pilasvacias.yaba.screens.favorites.FavoritesFragment;
import com.pilasvacias.yaba.screens.lines.LinesFragment;
import com.pilasvacias.yaba.screens.nocturnos.NocturnosFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by pablo on 10/11/13.
 *
 */
@Module(injects =
        {
                MainActivity.class,
                MainActivity.DummyFragment.class,
                FavoritesFragment.class,
                LinesFragment.class,
                NocturnosFragment.class,
                ProbaActivity.class
        },
        includes = VolleyModule.class,
        library = true)
public class EmtModule {

    @Provides @Singleton EmtEnvelopeSerializer provideEnvelopeSerializer() {
        return new EmtEnvelopeSerializer();
    }

    @Provides EmtRequestManager provideEmtRequestManager(RequestQueue requestQueue) {
        return new EmtRequestManager(requestQueue);
    }

}
