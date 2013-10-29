package com.pilasvacias.yaba.modules.network.models;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.pilasvacias.yaba.BuildConfig;
import com.pilasvacias.yaba.modules.network.CacheMaker;
import com.pilasvacias.yaba.modules.network.handlers.ErrorHandler;
import com.pilasvacias.yaba.modules.network.handlers.SuccessHandler;

/**
 * Created by Pablo Orgaz - 10/23/13 - pabloogc@gmail.com - https://github.com/pabloogc
 */
public abstract class AbstractRequest<T> extends Request<T> {

    private String cacheKey;
    private long fakeExecutionTime = 0;
    private long cacheRefreshTime = 0;
    private long cacheExpireTime = 0;
    private boolean cacheSkip = false;
    private SuccessHandler<T> responseListener;
    private ErrorHandler emtErrorHandler;
    private boolean verbose = false;
    private Object body;
    private int method;
    private String url;

    public AbstractRequest(
            int method,
            String url,
            SuccessHandler<T> successHandler,
            ErrorHandler errorHandler) {
        super(method, url, errorHandler);
        this.method = method;
        this.url = url;
        this.emtErrorHandler = errorHandler;
        this.responseListener = successHandler;
    }

//    @Override public String getUrl() {
//        if (url != null)
//            return url;
//        return super.getUrl();
//    }
//
//    public void setUrl(String url) {
//        this.url = url;
//    }
//
//    public int getMethod() {
//        return method;
//    }
//
//    public void setMethod(int method) {
//      this.method = method;
//   }

    public ErrorHandler getEmtErrorHandler() {
        return emtErrorHandler;
    }

    public void setEmtErrorHandler(ErrorHandler emtErrorHandler) {
        this.emtErrorHandler = emtErrorHandler;
    }

    public SuccessHandler<T> getResponseListener() {
        return responseListener;
    }

    public void setResponseListener(SuccessHandler<T> responseListener) {
        this.responseListener = responseListener;
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

    public long getCacheExpireTime() {
        return cacheExpireTime;
    }

    public void setCacheExpireTime(long cacheExpireTime) {
        this.cacheExpireTime = cacheExpireTime;
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
        if (!BuildConfig.DEBUG)
            return;

        try {
            Thread.sleep(fakeExecutionTime);
        } catch (InterruptedException e) {
        }
    }

    @Override protected void deliverResponse(T response) {
        if (responseListener != null && !isCanceled())
            responseListener.onResponse(response);
    }

    public abstract T getParsedData(NetworkResponse response);

    public abstract VolleyError generateErrorResponse(NetworkResponse response, T data);

    public abstract boolean responseIsOk(NetworkResponse response, T data);

    public boolean skippingCache() {
        return cacheSkip;
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

    @Override public String getCacheKey() {
        if (cacheKey != null)
            return cacheKey;
        else
            return super.getCacheKey();
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }
}
