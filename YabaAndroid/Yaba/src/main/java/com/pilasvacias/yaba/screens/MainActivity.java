package com.pilasvacias.yaba.screens;

import android.os.Bundle;
import android.webkit.WebView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.common.network.NetworkActivity;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.Views;

public class MainActivity extends NetworkActivity {

    @InjectView(R.id.text)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Views.setDebug(true);
        Views.inject(this);

        Test in = new Test();
        in.lista.add(new Otro());
        in.lista.add(new Otro());
        String xml1 = envelopeSerializer.toXML(in);
        Test rec = envelopeSerializer.fromXML(xml1, Test.class);
        String xml2 = envelopeSerializer.toXML(rec);

        requestQueue.add(new StringRequest(Request.Method.GET, "http://www.google.com/mobile/", new Response.Listener<String>() {
            @Override public void onResponse(String response) {
                final String mimeType = "text/html; charset=utf-8";
                final String encoding = "utf-8";
                webView.loadData(response, mimeType, encoding);
            }
        }, new Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {

            }
        }
        ));

        //L.og.w("-\n%s", xml1);
        //L.og.w("-\n%s", xml2);
        //L.og.e("ok -> %b", xml1.equalsIgnoreCase(xml2));


    }

    @Default
    @Root
    private static class Test extends Base {
        int x = 3;
        int y = 9;
        List<Otro> lista = new ArrayList<Otro>();
    }

    @Default
    @Root
    private static class Otro {
        static int x = 0;
        String elem_t = "===============";
    }

    @Default
    @Root
    private static class Base {
        int auth = 335;
        String key = "key";
    }


}
