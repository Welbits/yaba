package com.pilasvacias.yaba.modules.network.handlers;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by pablo on 15/10/13.
 */
public abstract class ErrorHandler implements Response.ErrorListener {
    public Vistor getVistor() {
        return vistor;
    }

    public void setVistor(Vistor vistor) {
        this.vistor = vistor;
    }

    private Vistor vistor;

    @Override
    public final void onErrorResponse(VolleyError error) {
        if(vistor!= null)
            vistor.beforeError(error);
        handleError(error);
        if(vistor != null)
            vistor.afterError(error);
    }

    public abstract void handleError(VolleyError error);

    public interface Vistor {
        void beforeError(VolleyError response);
        void afterError(VolleyError response);
    }

}
