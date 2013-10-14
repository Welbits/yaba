package com.pilasvacias.yaba.modules.network;

import com.android.volley.Request;
import com.android.volley.Response;
import com.pilasvacias.yaba.modules.util.Duration;

/**
 * Created by pablo on 10/12/13.
 * welvi-android
 */
public abstract class CacheMaker<T> extends Request<T> {

    protected static final long defaultClientCacheExpiry = 10 * Duration.ONE_SECOND; //10 seconds

    public CacheMaker(int method, String url, Response.ErrorListener listener) {
        super(method, url, listener);
    }

    protected Long getClientCacheExpiry() {
        return defaultClientCacheExpiry;
    }

}
