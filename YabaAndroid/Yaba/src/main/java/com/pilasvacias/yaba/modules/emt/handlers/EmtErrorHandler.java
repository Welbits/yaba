package com.pilasvacias.yaba.modules.emt.handlers;

import com.android.volley.VolleyError;
import com.pilasvacias.yaba.modules.network.ErrorCause;
import com.pilasvacias.yaba.modules.network.handlers.ErrorHandler;
import com.pilasvacias.yaba.modules.network.handlers.LoadingHandler;

/**
 * Created by pablo on 15/10/13.
 */
public class EmtErrorHandler extends ErrorHandler {

    public EmtErrorHandler() {
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

    private void hideLoadingHandler(String message) {
        LoadingHandler loadingHandler = getLoadingHandler();
        if (loadingHandler != null)
            loadingHandler.hideLoading(message, false);
    }

}
