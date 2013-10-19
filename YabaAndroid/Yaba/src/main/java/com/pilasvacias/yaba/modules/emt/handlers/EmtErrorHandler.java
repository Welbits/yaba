package com.pilasvacias.yaba.modules.emt.handlers;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.pilasvacias.yaba.modules.emt.models.EmtData;
import com.pilasvacias.yaba.modules.network.ErrorCause;
import com.pilasvacias.yaba.modules.network.handlers.ErrorHandler;
import com.pilasvacias.yaba.modules.network.handlers.LoadingHandler;

import java.lang.ref.WeakReference;

/**
 * Created by pablo on 15/10/13.
 */
public class EmtErrorHandler extends ErrorHandler {

    private WeakReference<LoadingHandler> loadingHandler;
    private Context context;

    public EmtErrorHandler() {
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setLoadingHandler(WeakReference<LoadingHandler> loadingHandler) {
        this.loadingHandler = loadingHandler;
    }

    @Override
    public void handleError(VolleyError volleyError) {

        ErrorCause cause = ErrorCause.getError(volleyError);
        switch (cause) {
            case UNKNOWN_ERROR:
                break;
            case NO_NETWORK_ERROR:
                break;
            case PARSE_ERROR:
                break;
            case SERVER_ERROR:
                break;
            case TIMED_OUT_ERROR:
                break;
            case EMT_ERROR:
                break;
        }
        hideLoadingHandler(cause.name());
    }

    public boolean responseIsOk(EmtData<?> result, NetworkResponse response) {
        return result != null && result.getEmtInfo().getResultCode() == 0;
    }

    private void hideLoadingHandler(String message) {
        if (loadingHandler!= null && loadingHandler.get() != null)
            loadingHandler.get().hideLoading(message, false);
    }

}
