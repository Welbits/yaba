package com.pilasvacias.yaba.modules.network;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by pablo on 15/10/13.
 */
public abstract class ErrorHandler implements Response.ErrorListener{

    @Override
    public void onErrorResponse(VolleyError error) {
        handleError(error);
    }

    //Just a naming thing.
    public abstract void handleError(VolleyError error);
}
