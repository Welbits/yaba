package com.pilasvacias.yaba.modules.network.models;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.pilasvacias.yaba.BuildConfig;
import com.pilasvacias.yaba.modules.emt.handlers.EmtErrorHandler;
import com.pilasvacias.yaba.modules.emt.models.EmtError;
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

    public ErrorHandler getEmtErrorHandler() {
        return emtErrorHandler;
    }

    public void setEmtErrorHandler(ErrorHandler emtErrorHandler) {
        this.emtErrorHandler = emtErrorHandler;
    }

    public SuccessHandler<T> getSuccessHandler() {
        return successHandler;
    }

    public void setSuccessHandler(SuccessHandler<T> successHandler) {
        this.successHandler = successHandler;
    }

    public long getFakeExecutionTime() {
        return fakeExecutionTime;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    private SuccessHandler<T> successHandler;
    private ErrorHandler emtErrorHandler;
    private boolean verbose = false;

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public AbstractRequest(int method, String url, Response.ErrorListener listener) {
        super(method, url, listener);
    }

    public long getCacheRefreshTime() {
        return cacheRefreshTime;
    }

    public long getCacheExpireTime() {
        return cacheExpireTime;
    }

    @SuppressWarnings("unchecked")
    @Override protected Response<T> parseNetworkResponse(NetworkResponse response) {
        fakeLongRequest();

        T data = getParsedData(response);
        if (emtErrorHandler.responseIsOk(data, response))
            return Response.success(data, CacheMaker.generateCache(response, getCacheRefreshTime(), getCacheExpireTime()));
        else
            return Response.error(emtErrorHandler.generateErrorResponse(data, response));
    }

    @Override public String getCacheKey() {
        return getUrl() + cacheKey;
    }

    public void setCacheExpireTime(long cacheExpireTime) {
        this.cacheExpireTime = cacheExpireTime;
    }

    public void setCacheRefreshTime(long cacheRefreshTime) {
        this.cacheRefreshTime = cacheRefreshTime;
    }

    public void setFakeExecutionTime(long fakeTime) {
        this.fakeExecutionTime = fakeTime;
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
        if (successHandler != null && !isCanceled())
            successHandler.onResponse(response);
    }

    public abstract T getParsedData(NetworkResponse response);
}
