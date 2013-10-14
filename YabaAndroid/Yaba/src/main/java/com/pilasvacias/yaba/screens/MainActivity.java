package com.pilasvacias.yaba.screens;

import android.os.Bundle;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.common.network.NetworkActivity;
import com.pilasvacias.yaba.modules.emt.models.EmtResult;

public class MainActivity extends NetworkActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        VolleyLog.DEBUG = true;

        createRequest();
        createRequest();
        createRequest();
        createRequest();

    }

    private void createRequest() {
        requestManager
                .<GetGroupsResult>beginRequest()
                .body("GetGroups")
                .responseType(GetGroupsResult.class)
                .listener(new Response.Listener<GetGroupsResult>() {
                    @Override public void onResponse(GetGroupsResult response) {
                    }
                })
                .error(new Response.ErrorListener() {
                    @Override public void onErrorResponse(VolleyError error) {
                    }
                })
                .verbose(true)
                .execute();
    }

    private static class GetGroupsResult extends EmtResult {

    }


}
