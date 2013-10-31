package com.pilasvacias.yaba.modules.emt.builders;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.pilasvacias.yaba.modules.emt.handlers.EmtErrorHandler;

/**
 * Created by pablo on 10/14/13.
 * welvi-android
 */
public class EmtRequestManager {

    /**
     * Every request is associated with the EmtRequestManager that created it
     * so cancelling all request will cancel the requests done by this manager.
     */
    private final Object DEFAULT_TAG = new Object();
    private RequestQueue requestQueue;
    private Object tag = DEFAULT_TAG;
    /**
     * The container context it should be an activity.
     */
    private Context context;

    /**
     * @param requestQueue injected {@link com.pilasvacias.yaba.modules.network.VolleyModule}
     */
    public EmtRequestManager(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public void setRequestQueue(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    /**
     * Create a new EmtRequestBuilder to use the fluent interface.
     *
     * @param responseType The POJO class representing the expected response.
     * @return A new request builder.
     */
    public <T> EmtRequestBuilder<T> beginRequest(Class<T> responseType) {
        return new EmtRequestBuilder<T>(requestQueue)
                .url("https://servicios.emtmadrid.es:8443/bus/servicebus.asmx")
                .method(Request.Method.POST)
                .responseType(responseType)
                .tag(tag)
                .error(new EmtErrorHandler())
                .context(context);
    }

    public void cancelAllRequests() {
        cancelAllRequests(this);
    }

    public void cancelAllRequests(Object tag) {
        requestQueue.cancelAll(tag);
    }
}
