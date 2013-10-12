package com.pilasvacias.yaba.common;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.pilasvacias.yaba.application.YabaApplication;

import java.util.ArrayList;
import java.util.List;

import dagger.ObjectGraph;

/**
 * Created by pablo on 10/9/13.
 * welvi-android
 */
public class BaseActivity extends FragmentActivity {

    //@Inject LocationManager locationManager;
    private ObjectGraph activityGraph;

    YabaApplication getYabaApplication() {
        return (YabaApplication) getApplication();
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityGraph = getYabaApplication().getApplicationGraph().plus(getModules().toArray());
    }

    @Override protected void onDestroy() {
        activityGraph = null;
        super.onDestroy();
    }

    protected List<Object> getModules() {
        return new ArrayList<Object>();
    }

    public ObjectGraph getActivityGraph() {
        return activityGraph;
    }
}
