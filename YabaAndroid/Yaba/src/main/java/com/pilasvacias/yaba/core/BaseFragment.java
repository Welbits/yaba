package com.pilasvacias.yaba.core;

import android.app.Fragment;

import com.pilasvacias.yaba.util.BusUtils;

import dagger.ObjectGraph;

/**
 * Created by pablo on 10/9/13.
 * welvi-android
 */
public class BaseFragment extends Fragment {

    protected boolean isBusRegistered;

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    public ObjectGraph getActivityGraph() {
        return getBaseActivity().getActivityGraph();
    }

    protected void registerBus() {
        isBusRegistered = BusUtils.checkBusRegistered(isBusRegistered, this);
    }

    protected void postBus(Object event) {
        BusUtils.post(isBusRegistered, event);
    }

    @Override
    public void onDestroy() {
        BusUtils.unregisterBus(isBusRegistered, this);
        super.onDestroy();
    }

}
