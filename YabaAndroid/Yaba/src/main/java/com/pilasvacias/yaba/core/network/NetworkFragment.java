package com.pilasvacias.yaba.core.network;

import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.pilasvacias.yaba.core.activity.BaseFragment;
import com.pilasvacias.yaba.modules.emt.builders.EmtRequestManager;

import javax.inject.Inject;

/**
 * Created by pablo on 10/9/13.
 * welvi-android
 */
public class NetworkFragment extends BaseFragment {

    @Inject protected RequestQueue requestQueue;
    @Inject protected EmtRequestManager requestManager;

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityGraph().inject(this);
        requestManager.setContext(getActivity());
    }

    protected RequestQueue getRequestQueue() {
        return requestQueue;
    }

    protected EmtRequestManager getRequestManager() {
        return requestManager;
    }


    @Override public void onDestroy() {
        requestManager.cancelAllRequests();
        requestManager = null;
        requestQueue = null;
        super.onDestroy();
    }


}
