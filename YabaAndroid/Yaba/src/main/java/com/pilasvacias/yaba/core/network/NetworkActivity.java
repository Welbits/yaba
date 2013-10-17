package com.pilasvacias.yaba.core.network;

import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.pilasvacias.yaba.core.BaseActivity;
import com.pilasvacias.yaba.modules.emt.EmtRequestManager;

import javax.inject.Inject;

/**
 * Created by pablo on 10/9/13.
 * welvi-android
 */
public class NetworkActivity extends BaseActivity {

    //Also don't use getters if not needed because they are 7x times slower than direct access.
    @Inject protected RequestQueue requestQueue;
    @Inject protected EmtRequestManager requestManager;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestManager.setNetworkActivity(this);
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public EmtRequestManager getRequestManager() {
        return requestManager;
    }

    @Override protected void onDestroy() {
        requestManager.cancelAllRequests();
        requestManager = null;
        super.onDestroy();
    }
}
