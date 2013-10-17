package com.pilasvacias.yaba.modules.emt.models;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

/**
 * Created by pablo on 10/16/13.
 * welvi-android
 */
public class EmtError extends VolleyError {

    EmtStatusCode emtStatusCode;

    public EmtError(NetworkResponse response, EmtData<?> emtResult) {
        super(response);
        emtStatusCode = EmtStatusCode.getFromResponse(emtResult.getEmtInfo());
    }
}
