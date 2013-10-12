package com.pilasvacias.yaba.application;

import android.app.Application;

import com.pilasvacias.yaba.modules.soap.SoapModule;
import com.pilasvacias.yaba.modules.util.ContextModule;

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

    /**
     * Create the list of modules to be included in all activities. In this case
     * the ContextModule and SoapModules are added because they are global for
     * the application.
     *
     * @return the list of modules
     */
    protected List<Object> getModules() {
        return Arrays.<Object>asList(
                new ContextModule(this),
                new SoapModule());
    }

    public ObjectGraph getApplicationGraph() {
        return applicationGraph;
    }

}
