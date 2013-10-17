package com.pilasvacias.yaba.modules.emt.handlers;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.pilasvacias.yaba.core.network.NetworkActivity;
import com.pilasvacias.yaba.modules.emt.models.EmtResult;
import com.pilasvacias.yaba.modules.emt.models.EmtStatusCode;
import com.pilasvacias.yaba.modules.network.ErrorCause;
import com.pilasvacias.yaba.modules.network.handlers.ErrorHandler;
import com.pilasvacias.yaba.modules.network.handlers.LoadingHandler;

/**
 * Created by pablo on 15/10/13.
 */
public class EmtErrorHandler extends ErrorHandler {

    private LoadingHandler loadingHandler;
    private NetworkActivity networkActivity;

    public EmtErrorHandler() {
    }

    public void setNetworkActivity(NetworkActivity networkActivity) {
        this.networkActivity = networkActivity;
    }

    public void setLoadingHandler(LoadingHandler loadingHandler) {
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
        hideHandler(cause.name());
    }

    public boolean responseIsOk(EmtResult headers, NetworkResponse response) {
        EmtStatusCode statusCode = EmtStatusCode.getFromResponse(headers);
        //return statusCode == EmtStatusCode.PASSKEY_OK || statusCode == EmtStatusCode.PASSKEY_OK_NOT_NEEDED;
        return true;
    }

    private void hideHandler(String message) {
        if (loadingHandler != null)
            loadingHandler.hideLoading(message, false);
    }

}
