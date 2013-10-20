package com.pilasvacias.yaba.modules.network.handlers;

import com.android.volley.Response;

import java.lang.ref.WeakReference;

/**
 * Created by pablo on 15/10/13.
 * <p/>
 * Success Handler used in {@link com.pilasvacias.yaba.modules.emt.builders.EmtRequestBuilder}
 */
public abstract class SuccessHandler<T> implements Response.Listener<T> {

    private WeakReference<LoadingHandler> loadingHandler;
    private Visitor visitor;

    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }

    public void setLoadingHandler(LoadingHandler loadingHandler) {
        this.loadingHandler = new WeakReference<LoadingHandler>(loadingHandler);
    }

    @Override public final void onResponse(T response) {
        if (loadingHandler != null && loadingHandler.get() != null)
            loadingHandler.get().hideLoading(null, true);
        if(visitor != null)
            visitor.beforeResponse(response);
        onSuccess(response);
        if(visitor != null)
            visitor.afterResponse(response);
    }

    public abstract void onSuccess(T result);

    public interface Visitor {
        void beforeResponse(Object response);
        void afterResponse(Object response);
    }

}
