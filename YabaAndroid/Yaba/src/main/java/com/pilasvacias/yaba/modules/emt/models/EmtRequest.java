package com.pilasvacias.yaba.modules.emt.models;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.pilasvacias.yaba.BuildConfig;
import com.pilasvacias.yaba.modules.emt.EmtEnvelopeSerializer;
import com.pilasvacias.yaba.modules.emt.handlers.EmtErrorHandler;
import com.pilasvacias.yaba.modules.network.handlers.SuccessHandler;
import com.pilasvacias.yaba.modules.util.l;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pablo on 10/12/13.
 * welvi-android
 */
public class EmtRequest<T extends EmtResult> extends Request<T> {

    private final EmtBody body;
    private final Class<T> responseType;
    private final SuccessHandler<T> listener;
    private EmtErrorHandler emtErrorHandler;
    private boolean verbose = false;
    private long fakeTime = 0;

    public EmtRequest(EmtBody body, SuccessHandler<T> listener, EmtErrorHandler emtErrorHandler, Class<T> responseType) {
        super(Method.POST, "https://servicios.emtmadrid.es:8443/bus/servicebus.asmx", emtErrorHandler);
        this.body = body;
        this.emtErrorHandler = emtErrorHandler;
        this.responseType = responseType;
        this.listener = listener;
    }

    public void setFakeTime(long fakeTime) {
        this.fakeTime = fakeTime;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    @Override protected Response<T> parseNetworkResponse(NetworkResponse response) {
        if (fakeTime > 0)
            fakeLongRequest();

        String xml = new String(response.data);
        T data = EmtEnvelopeSerializer.getInstance().fromXML(xml, responseType);

        if (verbose && data != null)
            l.og.d("Result for %s => status: %d : message: %s",
                    body.getSoapAction(),
                    data.getRequestInfo().getReturnCode(),
                    data.getRequestInfo().getDescription());


        if (emtErrorHandler.responseIsOk(data, response))
            return Response.success(data, HttpHeaderParser.parseCacheHeaders(response));
        else
            return Response.error(new EmtError(response, data));


    }

    private void fakeLongRequest() {
        //Just in case someone uses it and forgets
        if (!BuildConfig.DEBUG)
            return;

        try {
            Thread.sleep(fakeTime);
        } catch (InterruptedException e) {
        }
    }

    @Override protected void deliverResponse(T response) {
        if (listener != null)
            listener.onResponse(response);
    }

    @Override public byte[] getBody() throws AuthFailureError {
        String xml = EmtEnvelopeSerializer.getInstance().toXML(body);
        if (verbose)
            l.og.d("emt sent body => \n%s", xml);
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
}
