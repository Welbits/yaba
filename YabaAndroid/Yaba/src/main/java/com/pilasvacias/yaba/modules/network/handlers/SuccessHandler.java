package com.pilasvacias.yaba.modules.network.handlers;

import com.android.volley.Response;

/**
 * Created by pablo on 15/10/13.
 * <p/>
 * Success Handler used in {@link com.pilasvacias.yaba.modules.emt.EmtRequestBuilder}
 */
public abstract class SuccessHandler<T> implements Response.Listener<T> {

    private LoadingHandler loadingHandler;

    public LoadingHandler getLoadingHandler() {
        return loadingHandler;
    }

    public void setLoadingHandler(LoadingHandler loadingHandler) {
        this.loadingHandler = loadingHandler;
    }

    @Override public final void onResponse(T response) {
        if (loadingHandler != null)
            loadingHandler.hideLoading(null, true);
        onSuccess(response);
    }

    public abstract void onSuccess(T result);

}
