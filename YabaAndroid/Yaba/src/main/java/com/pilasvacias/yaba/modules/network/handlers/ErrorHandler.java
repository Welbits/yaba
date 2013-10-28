package com.pilasvacias.yaba.modules.network.handlers;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.lang.ref.WeakReference;

/**
 * Created by pablo on 15/10/13.
 */
public abstract class ErrorHandler implements Response.ErrorListener {

    private WeakReference<LoadingHandler> loadingHandler = new WeakReference<LoadingHandler>(null);
    private WeakReference<Context> context = new WeakReference<Context>(null);
    private Visitor visitor;

    public Visitor getVisitor() {
        return visitor;
    }

    public LoadingHandler getLoadingHandler() {
        return loadingHandler.get();
    }

    public Context getContext() {
        return context.get();
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (visitor != null)
            visitor.beforeError(error);
        handleError(error);
        if (visitor != null)
            visitor.afterError(error);
    }

    public abstract void handleError(VolleyError error);

    public void setContext(Context context) {
        this.context = new WeakReference<Context>(context);
    }

    public void setLoadingHandler(LoadingHandler loadingHandler) {
        this.loadingHandler = new WeakReference<LoadingHandler>(loadingHandler);
    }

    public interface Visitor {
        void beforeError(VolleyError response);

        void afterError(VolleyError response);
    }


}
