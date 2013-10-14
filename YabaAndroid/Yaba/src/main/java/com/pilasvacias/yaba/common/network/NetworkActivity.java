package com.pilasvacias.yaba.common.network;

import com.android.volley.RequestQueue;
import com.pilasvacias.yaba.common.BaseActivity;
import com.pilasvacias.yaba.modules.emt.EmtRequestManager;

import javax.inject.Inject;

/**
 * Created by pablo on 10/9/13.
 * welvi-android
 */
public class NetworkActivity extends BaseActivity {

    //Also don't use getters because they are 7x times slower than direct access.
    @Inject protected RequestQueue requestQueue;
    @Inject protected EmtRequestManager requestManager;

}
