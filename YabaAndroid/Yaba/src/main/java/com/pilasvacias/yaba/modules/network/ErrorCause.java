package com.pilasvacias.yaba.modules.network;

import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.pilasvacias.yaba.modules.emt.models.EmtError;

/**
 * Created by pablo on 10/15/13.
 * welvi-android
 */
public enum ErrorCause {

    UNKNOWN_ERROR,
    NO_NETWORK_ERROR,
    PARSE_ERROR,
    SERVER_ERROR,
    TIMED_OUT_ERROR,
    EMT_ERROR;

    public static ErrorCause getError(VolleyError error) {
        if (error instanceof NetworkError)
            return NO_NETWORK_ERROR;
        if (error instanceof ParseError)
            return PARSE_ERROR;
        if (error instanceof ServerError)
            return SERVER_ERROR;
        if (error instanceof TimeoutError)
            return TIMED_OUT_ERROR;
        if (error instanceof EmtError)
            return EMT_ERROR;

        return UNKNOWN_ERROR;
    }

}
