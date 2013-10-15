package com.pilasvacias.yaba.modules.emt;

import com.android.volley.RequestQueue;
import com.pilasvacias.yaba.modules.emt.models.EmtResult;

/**
 * Created by pablo on 10/14/13.
 * welvi-android
 */
public class EmtRequestManager {

    private RequestQueue requestQueue;

    public EmtRequestManager(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public <T extends EmtResult> EmtRequestBuilder<T> beginRequest(Class<T> responseType) {
        return new EmtRequestBuilder<T>(requestQueue).responseType(responseType);
    }
}
