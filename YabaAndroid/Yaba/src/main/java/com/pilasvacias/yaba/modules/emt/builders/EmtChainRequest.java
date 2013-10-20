package com.pilasvacias.yaba.modules.emt.builders;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.pilasvacias.yaba.modules.emt.handlers.EmtErrorHandler;
import com.pilasvacias.yaba.modules.emt.models.EmtRequest;
import com.pilasvacias.yaba.modules.network.handlers.ErrorHandler;
import com.pilasvacias.yaba.modules.network.handlers.LoadingHandler;
import com.pilasvacias.yaba.modules.network.handlers.SuccessHandler;
import com.pilasvacias.yaba.modules.network.handlers.impl.DialogLoadingHandler;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by pablo on 10/20/13.
 */
public class EmtChainRequest {
    private final RequestQueue requestQueue;
    private final Context context;
    private final Object tag;
    private LinkedList<EmtRequestBuilder<?>> requestBuilders = new LinkedList<EmtRequestBuilder<?>>();
    private EmtErrorHandler chainErrorHandler;
    private LoadingHandler chainLoadingHandler;
    private String loadingMessage = "";
    private boolean ignoreLoading;
    private boolean ignoreErrors;
    private boolean errorHappened = false;

    public EmtChainRequest(RequestQueue requestQueue, Context context, Object tag) {
        this.requestQueue = requestQueue;
        this.context = context;
        this.tag = tag;
    }

    public <T> EmtRequestBuilder<T> newLink(Class<T> responseType) {
        EmtRequestBuilder<T> builder = new EmtRequestBuilder<T>(requestQueue)
                .responseType(responseType)
                .error(new EmtRequestBuilder.FakeErrorHandler())
                .success(new EmtRequestBuilder.FakeSuccessHandler<T>())
                .ignoreLoading(true)
                .context(context)
                .chain(this)
                .tag(tag);

        requestBuilders.addLast(builder);

        return builder;
    }

    public void execute() {

        LinkedList<Request<?>> requests = new LinkedList<Request<?>>();
        for (EmtRequestBuilder<?> emtRequestBuilder : requestBuilders) {
            emtRequestBuilder.create();
            requests.add(emtRequestBuilder.getEmtRequest());
        }

        for (int i = 0; i < requestBuilders.size(); i++) {
            final int finalIndex = i;
            requestBuilders.get(i).successHandler.setVisitor(new SuccessHandler.Visitor() {
                @Override public void beforeResponse(Object response) {
                }

                @Override public void afterResponse(Object response) {
                    if (finalIndex + 1 < requestBuilders.size() && !errorHappened)
                        requestBuilders.get(finalIndex + 1).execute();
                    else if(!ignoreLoading)
                        chainLoadingHandler.hideLoading("", true);
                }
            });
        }

        for (int i = 0; i < requestBuilders.size(); i++) {
            final int finalIndex = i;
            requestBuilders.get(i).errorHandler.setVisitor(new ErrorHandler.Visitor() {
                @Override public void beforeError(VolleyError response) {
                }

                @Override public void afterError(VolleyError response) {
                    errorHappened = true;
                    chainErrorHandler.onErrorResponse(response);
                }
            });
        }

        if (!ignoreErrors && chainErrorHandler == null)
            chainErrorHandler = new EmtErrorHandler();

        if (!ignoreLoading && chainLoadingHandler == null)
            chainLoadingHandler =
                    new DialogLoadingHandler(context, requests.toArray(new Request[requests.size()]));

        if (!ignoreLoading)
            chainLoadingHandler.showLoading(loadingMessage);

        if (!ignoreErrors)
            chainErrorHandler.setLoadingHandler(chainLoadingHandler);

        requestBuilders.get(0).execute();
    }

    public EmtChainRequest error(EmtErrorHandler errorHandler) {
        this.chainErrorHandler = errorHandler;
        return this;
    }

    public EmtChainRequest loading(LoadingHandler loadingHandler) {
        this.chainLoadingHandler = loadingHandler;
        return this;
    }

    public EmtChainRequest ignoreLoading(boolean ignore) {
        this.ignoreLoading = ignore;
        return this;
    }

    public EmtChainRequest ignoreErrors(boolean ignore) {
        this.ignoreErrors = ignore;
        return this;
    }
}
