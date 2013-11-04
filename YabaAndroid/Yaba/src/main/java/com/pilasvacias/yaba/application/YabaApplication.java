package com.pilasvacias.yaba.application;

import android.app.Application;

import com.pilasvacias.yaba.application.sync.util.SyncUtils;
import com.pilasvacias.yaba.modules.emt.EmtModule;
import com.pilasvacias.yaba.modules.util.ContextModule;
import com.squareup.otto.Bus;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;

/**
 * Created by pablo on 10/9/13.
 * welvi-android
 */
public class YabaApplication extends Application {
    private ObjectGraph applicationGraph;
    private static Bus bus;

    @Override public void onCreate() {
        super.onCreate();
        applicationGraph = ObjectGraph.create(getModules().toArray());
        SyncUtils.createSyncAccount(this);
    }

    @Override public void onTerminate() {
        super.onTerminate();
    }

    /**
     * Create the list of modules to be included in all activities. In this case
     * the ContextModule and SoapModules are added because they are global for
     * the application.
     *
     * @return the list of modules
     */
    protected List<Object> getModules() {
        return Arrays.asList(
                new ContextModule(this),
                new EmtModule());
    }

    public ObjectGraph getApplicationGraph() {
        return applicationGraph;
    }

    public static Bus getBus() {
        if (bus == null) {
            bus = new Bus();
        }
        return bus;
    }
}
