package com.pilasvacias.yaba.modules.emt;

import com.android.volley.RequestQueue;
import com.pilasvacias.yaba.common.network.NetworkActivity;
import com.pilasvacias.yaba.modules.emt.handlers.EmtErrorHandler;
import com.pilasvacias.yaba.modules.emt.models.EmtBody;
import com.pilasvacias.yaba.modules.emt.models.EmtRequest;
import com.pilasvacias.yaba.modules.emt.models.EmtResult;
import com.pilasvacias.yaba.modules.network.handlers.LoadingHandler;
import com.pilasvacias.yaba.modules.network.handlers.SuccessHandler;
import com.pilasvacias.yaba.modules.network.handlers.impl.DialogLoadingHandler;

/**
 * Created by pablo on 10/14/13.
 * welvi-android
 */
public class EmtRequestBuilder<T extends EmtResult> {

    private EmtBody body;
    private Class<T> responseType;
    private SuccessHandler<T> sucessHandler = null;
    private EmtErrorHandler errorHandler = null;
    private RequestQueue requestQueue;
    private LoadingHandler loadingHandler;
    private NetworkActivity networkActivity;
    private Object tag;
    private boolean verbose = false;
    private boolean ignoreLoading = false;
    private boolean ignoreErrors = false;
    private long fakeTime = 0L;
    private long expireTime = 0L;
    private long refreshTime = 0L;

    /**
     * Use {@link com.pilasvacias.yaba.modules.emt.EmtRequestManager}
     *
     * @param requestQueue
     */
    EmtRequestBuilder(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public EmtRequestBuilder<T> body(EmtBody body) {
        this.body = body;
        return this;
    }

    public EmtRequestBuilder<T> fakeTime(long fakeTime) {
        this.fakeTime = fakeTime;
        return this;
    }

    public EmtRequestBuilder<T> cacheTime(long cacheTime) {
        return cacheTime(cacheTime, cacheTime);
    }

    public EmtRequestBuilder<T> cacheTime(long refreshTime, long expireTime) {
        this.refreshTime = refreshTime;
        this.expireTime = expireTime;
        return this;
    }

    public EmtRequestBuilder<T> body(final String bodyAsAction) {
        this.body = new EmtBody() {
            @Override public String getSoapAction() {
                return bodyAsAction;
            }
        };
        return this;
    }

    public EmtRequestBuilder<T> responseType(Class<T> responseType) {
        this.responseType = responseType;
        return this;
    }

    public EmtRequestBuilder<T> success(SuccessHandler<T> successHandler) {
        this.sucessHandler = successHandler;
        return this;
    }

    public EmtRequestBuilder<T> error(EmtErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        return this;
    }

    public EmtRequestBuilder<T> loading(LoadingHandler loadingHandler) {
        this.loadingHandler = loadingHandler;
        return this;
    }

    public EmtRequestBuilder<T> ignoreLoading(boolean ignore) {
        this.ignoreLoading = ignore;
        return this;
    }

    public EmtRequestBuilder<T> ignoreErrors(boolean ignore) {
        this.ignoreErrors = ignore;
        return this;
    }

    public EmtRequestBuilder<T> tag(Object tag) {
        this.tag = tag;
        return this;
    }

    public EmtRequestBuilder<T> verbose(boolean verbose) {
        this.verbose = verbose;
        return this;
    }

    public EmtRequestBuilder<T> activity(NetworkActivity networkActivity) {
        this.networkActivity = networkActivity;
        return this;
    }

    public EmtRequest<T> execute() {
        return execute(create());
    }

    public EmtRequest<T> execute(EmtRequest<T> emtRequest) {
        EmtRequest<T> request = create();
        requestQueue.add(request);

        if (loadingHandler != null && !ignoreLoading)
            loadingHandler.showLoading("h3h3h3");

        return request;
    }

    public EmtRequest<T> create() {
        if (!ignoreErrors) {
            if (errorHandler == null)
                errorHandler = new EmtErrorHandler();

            errorHandler.setNetworkActivity(networkActivity);
        }

        EmtRequest<T> request = new EmtRequest<T>(body, sucessHandler, errorHandler, responseType);
        request.setTag(tag);
        request.addMarker(body.getSoapAction());
        request.setVerbose(verbose);
        request.setFakeExecutionTime(fakeTime);
        request.setCacheRefreshTime(refreshTime);
        request.setCacheExpireTime(expireTime);


        if (!ignoreLoading && loadingHandler == null)
            loadingHandler = new DialogLoadingHandler(networkActivity, request);

        if (errorHandler != null) {
            errorHandler.setLoadingHandler(loadingHandler);
        }

        if (sucessHandler != null) {
            sucessHandler.setLoadingHandler(loadingHandler);
        }

        return request;
    }


}
