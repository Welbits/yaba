package com.pilasvacias.yaba.modules.emt;

import com.android.volley.RequestQueue;
import com.pilasvacias.yaba.common.network.NetworkActivity;
import com.pilasvacias.yaba.modules.emt.models.EmtResult;

/**
 * Created by pablo on 10/14/13.
 * welvi-android
 */
public class EmtRequestManager {

    private RequestQueue requestQueue;
    /**
     * Every request is associated with the EmtRequestManager that created it by default.
     * Cancelling all request will the requests done by this manager.
     */
    private Object tag = this;
    /**
     * The container activity.
     */
    private NetworkActivity networkActivity;

    /**
     * @param requestQueue injected {@link com.pilasvacias.yaba.modules.network.VolleyModule}
     */
    public EmtRequestManager(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public NetworkActivity getNetworkActivity() {
        return networkActivity;
    }

    public void setNetworkActivity(NetworkActivity networkActivity) {
        this.networkActivity = networkActivity;
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

    public <T extends EmtResult> EmtRequestBuilder<T> beginRequest(Class<T> responseType) {
        return new EmtRequestBuilder<T>(requestQueue)
                .responseType(responseType)
                .tag(tag)
                .activity(networkActivity);
    }

    public void cancelAllRequests() {
        cancelAllRequests(this);
    }

    public void cancelAllRequests(Object tag) {
        requestQueue.cancelAll(tag);
    }
}
