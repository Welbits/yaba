package com.pilasvacias.yaba.modules.emt;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.pilasvacias.yaba.modules.emt.models.EmtBody;
import com.pilasvacias.yaba.modules.emt.models.EmtRequest;
import com.pilasvacias.yaba.modules.emt.models.EmtResult;
import com.pilasvacias.yaba.modules.network.LoadingHandler;

/**
 * Created by pablo on 10/14/13.
 * welvi-android
 */
public class EmtRequestBuilder<T extends EmtResult> {

    private EmtBody body;
    private Class<T> responseType;
    private Response.Listener<T> listener = null;
    private EmtErrorHandler errorHandler = null;
    private RequestQueue requestQueue;
    private LoadingHandler loadingHandler;
    private Object tag;
    private boolean verbose;

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


    public EmtRequestBuilder<T> listener(Response.Listener<T> listener) {
        this.listener = listener;
        return this;
    }


    public EmtRequestBuilder<T> error(EmtErrorHandler errorListener) {
        this.errorHandler = errorListener;
        return this;
    }

    public EmtRequestBuilder<T> loading(LoadingHandler loadingHandler) {
        this.loadingHandler = loadingHandler;
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

    public EmtRequest<T> execute() {
        EmtRequest<T> request = new EmtRequest<T>(body, listener, errorHandler, responseType);
        request.setTag(tag);
        request.addMarker(body.getSoapAction());
        request.setVerbose(verbose);
        requestQueue.add(request);
        return request;
    }

}
