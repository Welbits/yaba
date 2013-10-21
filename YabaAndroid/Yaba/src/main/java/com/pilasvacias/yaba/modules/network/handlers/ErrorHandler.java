package com.pilasvacias.yaba.modules.network.handlers;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by pablo on 15/10/13.
 */
public abstract class ErrorHandler implements Response.ErrorListener {
    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }

    private Visitor visitor;

    @Override
    public final void onErrorResponse(VolleyError error) {
        if(visitor != null)
            visitor.beforeError(error);
        handleError(error);
        if(visitor != null)
            visitor.afterError(error);
    }

    public abstract void handleError(VolleyError error);

    public interface Visitor {
        void beforeError(VolleyError response);
        void afterError(VolleyError response);
    }

}
