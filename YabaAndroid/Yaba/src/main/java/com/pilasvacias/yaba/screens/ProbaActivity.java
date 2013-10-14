package com.pilasvacias.yaba.screens;

import android.os.Bundle;
import android.webkit.WebView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.common.network.NetworkActivity;
import com.pilasvacias.yaba.modules.emt.models.EmtRequest;
import com.pilasvacias.yaba.modules.emt.models.EmtResult;
import com.pilasvacias.yaba.modules.util.L;

import org.simpleframework.xml.Default;

import butterknife.InjectView;
import butterknife.Views;

public class ProbaActivity extends NetworkActivity {

    @InjectView(R.id.text)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proba);
        Views.inject(this);
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
