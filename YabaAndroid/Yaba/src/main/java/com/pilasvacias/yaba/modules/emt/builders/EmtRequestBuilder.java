package com.pilasvacias.yaba.modules.emt.builders;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.pilasvacias.yaba.modules.emt.handlers.EmtErrorHandler;
import com.pilasvacias.yaba.modules.emt.handlers.EmtSuccessHandler;
import com.pilasvacias.yaba.modules.emt.models.EmtBody;
import com.pilasvacias.yaba.modules.emt.models.EmtData;
import com.pilasvacias.yaba.modules.emt.models.EmtRequest;
import com.pilasvacias.yaba.modules.network.handlers.ErrorHandler;
import com.pilasvacias.yaba.modules.network.handlers.LoadingHandler;
import com.pilasvacias.yaba.modules.network.handlers.SuccessHandler;
import com.pilasvacias.yaba.modules.network.handlers.impl.DialogLoadingHandler;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.lang.ref.WeakReference;

/**
 * Created by pablo on 10/14/13.
 * welvi-android
 */
public class EmtRequestBuilder<T> {

    //Visibility is package local to avoid getters
    EmtBody body;
    Class<T> responseType;
    EmtSuccessHandler<T> successHandler;
    EmtErrorHandler errorHandler;
    RequestQueue requestQueue;
    LoadingHandler loadingHandler;
    Context context;
    Object tag;
    boolean verbose = false;
    boolean ignoreLoading = false;
    boolean ignoreErrors = false;
    boolean useCache = true;
    long fakeTime = 0L;
    long expireTime = 0L;
    long refreshTime = 0L;
    EmtChainRequest chainRequest;
    String loadingMessage;
    EmtRequest<T> emtRequest;

    /**
     * Use {@link EmtRequestManager}
     *
     * @param requestQueue
     */
    protected EmtRequestBuilder(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    /**
     * Every EMT request has a body.
     * see: {@link com.pilasvacias.yaba.modules.emt.models.EmtBody}
     *
     * @param body The POJO that represents the body. It will be serialized as XML
     * @return
     */
    public EmtRequestBuilder<T> body(EmtBody body) {
        this.body = body;
        return this;
    }

    /**
     * A fake waiting time simulating a long parse or long request. Useful
     * for debuggin configuration changes while loading happens. This action
     * is disabled in production.
     *
     * @param fakeTime
     * @return
     */
    public EmtRequestBuilder<T> fakeTime(long fakeTime) {
        this.fakeTime = fakeTime;
        return this;
    }

    /**
     * Cache time with no  auto refresh.
     *
     * @param cacheTime time to cache.
     * @return
     */
    public EmtRequestBuilder<T> cacheTime(long cacheTime) {
        return cacheTime(cacheTime, cacheTime);
    }

    /**
     * Cache time with refresh. If refresh time has passed the cache will be updated
     * in background and the old cache value will be returned.
     *
     * @param refreshTime
     * @param expireTime
     * @return
     */
    public EmtRequestBuilder<T> cacheTime(long refreshTime, long expireTime) {
        this.refreshTime = refreshTime;
        this.expireTime = expireTime;
        return this;
    }

    /**
     * Every EMT request must have an action. A request with no body may
     * still have an action like GetGroups.
     *
     * @param bodyAsAction
     * @return
     */
    public EmtRequestBuilder<T> body(final String bodyAsAction) {
        this.body = new BodyLessEmtBody(bodyAsAction);
        return this;
    }

    public EmtRequestBuilder<T> responseType(Class<T> responseType) {
        this.responseType = responseType;
        return this;
    }

    public EmtRequestBuilder<T> success(EmtSuccessHandler<T> successHandler) {
        this.successHandler = successHandler;
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


    public EmtRequestBuilder<T> cacheSkip(boolean use) {
        this.useCache = use;
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

    public EmtRequestBuilder<T> context(Context context) {
        this.context = context;
        return this;
    }

    public EmtRequestBuilder<T> chain(EmtChainRequest emtChainRequest) {
        this.chainRequest = emtChainRequest;
        return this;
    }

    public EmtChainRequest endLink() {
        return chainRequest;
    }

    public EmtRequest<T> getEmtRequest() {
        return emtRequest;
    }

    public EmtRequest<T> execute() {
        if (emtRequest == null)
            emtRequest = create();
        return execute(emtRequest);
    }

    public EmtRequest<T> execute(EmtRequest<T> emtRequest) {
        requestQueue.add(emtRequest);

        if (!ignoreLoading && loadingHandler != null)
            loadingHandler.showLoading(loadingMessage);

        return emtRequest;
    }

    public EmtRequest<T> create() {
        if (!ignoreErrors && errorHandler == null) {
            errorHandler = new EmtErrorHandler();
            errorHandler.setContext(context);
        }

        EmtRequest<T> request = new EmtRequest<T>(body, successHandler, errorHandler, responseType);
        request.setTag(tag);
        request.setVerbose(verbose);
        request.setFakeExecutionTime(fakeTime);
        request.setCacheRefreshTime(refreshTime);
        request.setCacheExpireTime(expireTime);
        request.setShouldCache(useCache);


        if (!ignoreLoading && loadingHandler == null)
            loadingHandler = new DialogLoadingHandler(context, request);

        if (!ignoreErrors)
            errorHandler.setLoadingHandler(loadingHandler);

        //This doesn't make much sense in EMT, perform and request and ignore the result.
        //but it's useful in POST, PUT or chained requests.
        if (successHandler != null)
            successHandler.setLoadingHandler(loadingHandler);


        return request;
    }

    public static class BodyLessEmtBody extends EmtBody {
        @XStreamOmitField
        String action;

        public BodyLessEmtBody(String action) {
            this.action = action;
        }

        @Override public String getSoapAction() {
            return action;
        }
    }

    public static class FakeSuccessHandler<T> extends EmtSuccessHandler<T> {
        @Override public void onSuccess(EmtData<T> result) {
        }
    }

    public static class FakeErrorHandler extends EmtErrorHandler {
        @Override public void handleError(VolleyError volleyError) {
        }

        @Override public void setLoadingHandler(LoadingHandler loadingHandler) {
        }
    }
}
