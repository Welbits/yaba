package com.pilasvacias.yaba.modules.emt.builders;

import com.android.volley.RequestQueue;
import com.pilasvacias.yaba.modules.emt.handlers.EmtErrorHandler;
import com.pilasvacias.yaba.modules.emt.handlers.EmtSuccessHandler;
import com.pilasvacias.yaba.modules.emt.models.EmtBody;
import com.pilasvacias.yaba.modules.emt.models.EmtData;
import com.pilasvacias.yaba.modules.emt.models.EmtRequest;
import com.pilasvacias.yaba.modules.network.builder.AbstractRequestBuilder;
import com.pilasvacias.yaba.modules.network.handlers.impl.DialogLoadingHandler;
import com.pilasvacias.yaba.modules.network.handlers.impl.FakeErrorHandler;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Created by pablo on 10/14/13.
 * welvi-android
 */
public class EmtRequestBuilder<T> extends AbstractRequestBuilder
        <
                EmtRequestBuilder<T>, //Type used to allow cast in abstract builder
                EmtRequest<T>, //Type of the request
                EmtSuccessHandler<T>, //Type of the handler
                EmtData<T> //Type of the data obtained
                > {


    //Visibility is package local to avoid getters
    EmtBody body;
    Class<T> responseType;

    /**
     * Use {@link EmtRequestManager}
     *
     * @param requestQueue
     */
    public EmtRequestBuilder(RequestQueue requestQueue) {
        super(requestQueue);
    }

    /**
     * Every EMT request has a body.
     * see: {@link com.pilasvacias.yaba.modules.emt.models.EmtBody}
     *
     * @param body The pojo that represents the body. It will be serialized as XML
     */
    public EmtRequestBuilder<T> body(EmtBody body) {
        this.body = body;
        return this;
    }

    /**
     * Every EMT request must have an action. A request with no body may
     * still have an action like GetGroups.
     *
     * @param bodyAsAction
     */
    public EmtRequestBuilder<T> body(final String bodyAsAction) {
        this.body = new FakeEmtBody(bodyAsAction);
        return this;
    }

    public EmtRequestBuilder<T> responseType(Class<T> responseType) {
        this.responseType = responseType;
        return this;
    }

    @Override
    public EmtRequest<T> create() {
        if (!ignoreErrors && errorHandler == null) {
            errorHandler = new EmtErrorHandler();
            errorHandler.setContext(context);
        } else {
            errorHandler = new FakeErrorHandler();
        }

        EmtRequest<T> request = new EmtRequest<T>(errorHandler, successHandler, body, responseType);
        request.setTag(tag);
        request.setVerbose(verbose);
        request.setFakeExecutionTime(fakeTime);
        request.setCacheRefreshTime(refreshTime);
        request.setCacheExpireTime(expireTime);
        request.setCacheSkip(cacheSkip);
        request.setShouldCache(cacheResult);

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

    public static class FakeEmtBody extends EmtBody {
        @XStreamOmitField
        String action;

        public FakeEmtBody(String action) {
            this.action = action;
        }

        @Override public String getSoapAction() {
            return action;
        }
    }


}
