package com.pilasvacias.yaba.screens;

import android.os.Bundle;
import android.webkit.WebView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.common.network.NetworkActivity;
import com.pilasvacias.yaba.modules.soap.emt.EmtBody;
import com.pilasvacias.yaba.modules.soap.emt.EmtRequest;
import com.pilasvacias.yaba.modules.soap.emt.EmtResult;
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

        requestQueue.add(new EmtRequest<GetGroupsResult>(new GetGroups(), GetGroupsResult.class, new Response.Listener<GetGroupsResult>() {
            @Override public void onResponse(GetGroupsResult response) {

            }
        }, new Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {

                String res = new String(error.networkResponse.data);
                L.og.e("error => %s", res.isEmpty() ? "No response" : res);

            }
        }
        ));

        //L.og.w("-\n%s", xml1);
        //L.og.w("-\n%s", xml2);
        //L.og.e("ok -> %b", xml1.equalsIgnoreCase(xml2));


    }

    @Default(required = false)
    private static class GetGroups extends EmtBody {

    }

    private static class GetGroupsResult extends EmtResult {
        String ReturnCode;
        String Description;
        String Expiration;
    }

}
