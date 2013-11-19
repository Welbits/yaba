package com.pilasvacias.yaba.core.activity;

import android.app.Activity;
import android.os.Bundle;

import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.application.YabaApplication;
import com.pilasvacias.yaba.core.ads.AdFragment;
import com.pilasvacias.yaba.util.BusUtils;

import java.util.ArrayList;
import java.util.List;

import dagger.ObjectGraph;

/**
 * Created by pablo on 10/9/13.
 * yaba-android
 */
public class BaseActivity extends Activity {

    //@Inject LocationManager locationManager;
    private ObjectGraph activityGraph;
    private List<Object> modules = new ArrayList<Object>();
    protected boolean isBusRegistered;

    YabaApplication getYabaApplication() {
        return (YabaApplication) getApplication();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateExtraModules(modules);
        activityGraph = getYabaApplication().getApplicationGraph().plus(modules.toArray());


        AdFragment adFragment = new AdFragment();

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.base_fragment_container, adFragment)
                .commit();
    }

    @Override
    protected void onDestroy() {
        BusUtils.unregisterBus(isBusRegistered, this);
        activityGraph = null;
        super.onDestroy();
    }

    protected void registerBus() {
        isBusRegistered = BusUtils.registerBus(isBusRegistered, this);
    }

    protected void postBus(Object event) {
        BusUtils.post(isBusRegistered, event);
    }

    protected BaseActivity getActivity() {
        return this;
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
