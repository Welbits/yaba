package com.pilasvacias.yaba.application;

import android.app.Application;

import com.pilasvacias.yaba.modules.LocationModule;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;

/**
 * Created by pablo on 10/9/13.
 * welvi-android
 */
public class YabaApplication extends Application {
    private ObjectGraph applicationGraph;

    @Override public void onCreate() {
        super.onCreate();
        applicationGraph = ObjectGraph.create(getModules().toArray());
    }

    protected List<Object> getModules() {
        return Arrays.<Object>asList(new LocationModule(this));
    }

    public ObjectGraph getApplicationGraph() {
        return applicationGraph;
    }

}
