package com.pilasvacias.yaba.core;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.pilasvacias.yaba.core.network.NetworkActivity;
import com.pilasvacias.yaba.util.BusUtils;
import com.squareup.otto.Bus;

import javax.inject.Inject;

import dagger.ObjectGraph;

/**
 * Created by pablo on 10/9/13.
 * welvi-android
 */
public class BaseFragment extends Fragment {

    protected boolean isBusRegistered;

    public NetworkActivity getBaseActivity() {
        return (NetworkActivity) getActivity();
    }

    public ObjectGraph getActivityGrapth(){
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
