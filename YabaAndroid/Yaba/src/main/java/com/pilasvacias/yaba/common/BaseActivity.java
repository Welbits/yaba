package com.pilasvacias.yaba.common;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.pilasvacias.yaba.application.YabaApplication;
import com.pilasvacias.yaba.modules.LogModule;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.ObjectGraph;
import timber.log.Timber;

/**
 * Created by pablo on 10/9/13.
 * welvi-android
 */
public class BaseActivity extends FragmentActivity {

    @Inject Lazy<Timber> timberLazy;

    //@Inject LocationManager locationManager;

    private ObjectGraph activityGraph;

    YabaApplication getYabaApplication() {
        return (YabaApplication) getApplication();
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //activityGraph = getYabaApplication().getApplicationGraph().plus(getModules().toArray());
        activityGraph = ObjectGraph.create(new LogModule());
        activityGraph.inject(this);
        //locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

    }

    @Override protected void onDestroy() {
        activityGraph = null;
        super.onDestroy();
    }

    protected List<Object> getModules() {
        return Arrays.<Object>asList(new LogModule());
    }

    protected Timber getTimber() {
        return timberLazy.get();
    }

    public ObjectGraph getActivityGraph() {
        return activityGraph;
    }
}
