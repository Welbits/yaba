package com.pilasvacias.yaba.modules.network.toolbox;

import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pilasvacias.yaba.modules.network.builder.PlayaRequestBuilder;

/**
 * Created by Pablo Orgaz - 10/30/13 - pabloogc@gmail.com - https://github.com/pabloogc
 */
public class GsonRequestBuilder<T> extends PlayaRequestBuilder<GsonRequestBuilder<T>, GsonRequest<T>, Object, T> {

    private TypeToken<T> token;
    private Class<T> clazz;
    private Gson gson;

    public GsonRequestBuilder(RequestQueue requestQueue) {
        super(requestQueue);
    }

    @Override public void configure(GsonRequest<T> request) {
        if (gson != null)
            request.setGson(gson);
    }

    @Override public GsonRequest<T> create() {
        if (clazz != null)
            return new GsonRequest<T>(errorHandler, clazz);
        else if (token != null)
            return new GsonRequest<T>(errorHandler, token);
        else
            throw new IllegalStateException("Class or TypeToken not set for request");
    }

    public GsonRequestBuilder<T> token(TypeToken<T> token) {
        this.token = token;
        this.clazz = null;
        return this;
    }

    public GsonRequestBuilder<T> token(Class<T> clazz) {
        this.clazz = clazz;
        this.token = null;
        return this;
    }

    public GsonRequestBuilder<T> gson(Gson gson) {
        this.gson = gson;
        return this;
    }

}
