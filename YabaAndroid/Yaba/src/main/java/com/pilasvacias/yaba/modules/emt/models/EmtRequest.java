package com.pilasvacias.yaba.modules.emt.models;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.pilasvacias.yaba.BuildConfig;
import com.pilasvacias.yaba.modules.emt.EmtEnvelopeSerializer;
import com.pilasvacias.yaba.modules.emt.handlers.EmtErrorHandler;
import com.pilasvacias.yaba.modules.emt.handlers.EmtSuccessHandler;
import com.pilasvacias.yaba.modules.network.CacheMaker;
import com.pilasvacias.yaba.modules.network.models.AbstractRequest;
import com.pilasvacias.yaba.util.L;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pablo on 10/12/13.
 * welvi-android
 */
public class EmtRequest<T> extends AbstractRequest<EmtData<T>> {

    private final EmtBody body;
    private final Class<T> responseType;


    public EmtRequest(EmtBody body, EmtSuccessHandler<T> listener, EmtErrorHandler emtErrorHandler, Class<T> responseType) {
        super(Method.POST, "https://servicios.emtmadrid.es:8443/bus/servicebus.asmx", emtErrorHandler);
        this.body = body;
        this.responseType = responseType;
    }

    @Override public byte[] getBody() throws AuthFailureError {
        String xml = EmtEnvelopeSerializer.getInstance().toXML(body);
        if (isVerbose())
            L.og.d("emt sent action %s with body => \n%s", body.getSoapAction(), xml);
        try {
            return xml.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override public String getBodyContentType() {
        return "text/xml";
    }

    @Override public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("SOAPAction", "http://tempuri.org/" + body.getSoapAction());
        return headers;
    }

    @Override public String getCacheKey() {
        return body.getCacheKey();
    }

    @Override public EmtData<T> getParsedData(NetworkResponse response) {
        String xml = new String(response.data);
        return EmtEnvelopeSerializer.getInstance().fromXML(xml, responseType, body);
    }
}
