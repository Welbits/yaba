package com.pilasvacias.yaba.modules.network.models;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.pilasvacias.yaba.modules.network.CacheMaker;
import com.pilasvacias.yaba.modules.network.handlers.ErrorHandler;
import com.pilasvacias.yaba.modules.network.handlers.SuccessHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pablo Orgaz - 10/23/13 - pabloogc@gmail.com - https://github.com/pabloogc
 */
public abstract class PlayaRequest<T> extends Request<T> {

    private String cacheKey;
    private long fakeExecutionTime = 0;
    private long cacheRefreshTime = 0;
    private long cacheExpireTime = 0;
    private boolean cacheSkip = false;
    private SuccessHandler<T> successHandler;
    private boolean verbose = false;
    private String bodyContentType;
    private Map<String, String> headers = Collections.emptyMap();

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    private Object body;
    private int method = Method.GET;
    private String url;
    private HashMap<String, String> params;

    public PlayaRequest(ErrorHandler errorHandler) {
        super(Method.GET, "http://you.forgot/to/set/the/url", errorHandler);
    }

    @Override public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public void setSuccessHandler(SuccessHandler<T> responseListener) {
        this.successHandler = responseListener;
    }

    public void setFakeExecutionTime(long fakeTime) {
        this.fakeExecutionTime = fakeTime;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public void setCacheRefreshTime(long cacheRefreshTime) {
        this.cacheRefreshTime = cacheRefreshTime;
    }

    public void setCacheExpireTime(long cacheExpireTime) {
        this.cacheExpireTime = cacheExpireTime;
    }

    @Override protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }

    @Override protected Response<T> parseNetworkResponse(NetworkResponse response) {
        fakeLongRequest();
        T data = getParsedData(response);
        if (responseIsOk(response, data))
            return Response.success(data, CacheMaker.generateCache(response, cacheRefreshTime, cacheExpireTime));
        else
            return Response.error(generateErrorResponse(response, data));
    }

    private void fakeLongRequest() {
        //Just in case someone uses it and forgets
        if (fakeExecutionTime <= 0)
            return;

        try {
            Thread.sleep(fakeExecutionTime);
        } catch (InterruptedException e) {
        }
    }

    @Override protected void deliverResponse(T response) {
        if (successHandler != null && !isCanceled())
            successHandler.onResponse(response);
    }

    public abstract T getParsedData(NetworkResponse response);

    public abstract VolleyError generateErrorResponse(NetworkResponse response, T data);

    public abstract boolean responseIsOk(NetworkResponse response, T data);

    public Object getBodyObject() {
        return body;
    }

    public void setCacheSkip(boolean cacheSkip) {
        this.cacheSkip = cacheSkip;
    }

    @Override public Cache.Entry getCacheEntry() {
        Cache.Entry entry = super.getCacheEntry();
        //If we are skipping cache manually invalidate the cache times.
        if (cacheSkip && entry != null) {
            entry.ttl = 0;
            entry.softTtl = 0;
        }
        return entry;
    }

    @Override public String getBodyContentType() {
        return bodyContentType;
    }

    public void setBodyContentType(String bodyContentType) {
        this.bodyContentType = bodyContentType;
    }

    @Override
    public String getCacheKey() {
        if (cacheKey != null)
            return cacheKey;
        else
            return super.getCacheKey();
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }
}
