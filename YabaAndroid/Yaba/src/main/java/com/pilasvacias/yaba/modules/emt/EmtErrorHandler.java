package com.pilasvacias.yaba.modules.emt;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.pilasvacias.yaba.modules.network.ErrorHandler;

/**
 * Created by pablo on 15/10/13.
 */
public class EmtErrorHandler extends ErrorHandler {
    @Override
    public void handleError(VolleyError error) {

    }

    public boolean responseIsOk(NetworkResponse response) {
        return true;
    }
}
