package com.pilasvacias.yaba.core;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.pilasvacias.yaba.core.network.NetworkActivity;
import com.pilasvacias.yaba.util.BusUtils;
import com.squareup.otto.Bus;

import javax.inject.Inject;

/**
 * Created by pablo on 10/9/13.
 * welvi-android
 */
public class BaseFragment extends Fragment {

    protected boolean isBusRegistered;

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getBaseActivity().getActivityGraph().inject(this);
    }

    public NetworkActivity getBaseActivity() {
        return (NetworkActivity) getActivity();
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
