package com.pilasvacias.yaba.core;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.pilasvacias.yaba.application.YabaApplication;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;

/**
 * Created by pablo on 10/9/13.
 * welvi-android
 */
public class BaseActivity extends ActionBarActivity {

    //@Inject LocationManager locationManager;
    private ObjectGraph activityGraph;
    private List<Object> modules = new ArrayList<Object>();

    YabaApplication getYabaApplication() {
        return (YabaApplication) getApplication();
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateExtraModules(modules);
        activityGraph = getYabaApplication().getApplicationGraph().plus(modules.toArray());
        activityGraph.inject(this);
    }

    @Override protected void onDestroy() {
        activityGraph = null;
        super.onDestroy();
    }

    protected List<Object> getModules() {
        return modules;
    }

    public ObjectGraph getActivityGraph() {
        return activityGraph;
    }

    /**
     * Add the extra modules you may need. The application modules
     * are injected by default in all activities
     *
     * @param modules
     */
    protected void onCreateExtraModules(List<Object> modules) {

    }
}
