package com.pilasvacias.yaba.modules.emt.models;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

/**
 * Created by pablo on 10/16/13.
 * welvi-android
 */
public class EmtError extends VolleyError {

    EmtStatusCode emtStatusCode;

    public EmtError(EmtData<?> emtResult, NetworkResponse response) {
        super(response);
        if (emtResult != null)
            emtStatusCode = EmtStatusCode.getFromResponse(emtResult.getEmtInfo());
        else
            emtStatusCode = EmtStatusCode.UNKNOWN;
    }
}
