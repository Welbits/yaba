package com.pilasvacias.yaba.modules.network.handlers.impl;

import com.android.volley.VolleyError;
import com.pilasvacias.yaba.modules.emt.handlers.EmtErrorHandler;
import com.pilasvacias.yaba.modules.network.handlers.LoadingHandler;

/**
 * Created by Pablo Orgaz - 10/27/13 - pabloogc@gmail.com - https://github.com/pabloogc
 */
public class FakeErrorHandler extends EmtErrorHandler {
    @Override public void handleError(VolleyError volleyError) {
    }

    @Override public void setLoadingHandler(LoadingHandler loadingHandler) {
    }
}
