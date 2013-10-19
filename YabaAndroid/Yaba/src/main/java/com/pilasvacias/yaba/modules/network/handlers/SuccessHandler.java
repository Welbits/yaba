package com.pilasvacias.yaba.modules.network.handlers;

import com.android.volley.Response;

import java.lang.ref.WeakReference;

/**
 * Created by pablo on 15/10/13.
 * <p/>
 * Success Handler used in {@link com.pilasvacias.yaba.modules.emt.EmtRequestBuilder}
 */
public abstract class SuccessHandler<T> implements Response.Listener<T> {

    private WeakReference<LoadingHandler> loadingHandler;
    private Vistor<T> vistor;

    public Vistor<T> getVistor() {
        return vistor;
    }

    public void setVistor(Vistor<T> vistor) {
        this.vistor = vistor;
    }

    public void setLoadingHandler(WeakReference<LoadingHandler> loadingHandler) {
        this.loadingHandler = loadingHandler;
    }

    @Override public final void onResponse(T response) {
        if (loadingHandler != null && loadingHandler.get() != null)
            loadingHandler.get().hideLoading(null, true);
        if(vistor != null)
            vistor.beforeResponse(response);
        onSuccess(response);
        if(vistor != null)
            vistor.afterResponse(response);
    }

    public abstract void onSuccess(T result);

    public interface Vistor<K> {
        void beforeResponse(K response);
        void afterResponse(K response);
    }

}
