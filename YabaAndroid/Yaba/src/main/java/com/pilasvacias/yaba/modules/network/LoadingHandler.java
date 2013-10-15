package com.pilasvacias.yaba.modules.network;

/**
 * Created by pablo on 15/10/13.
 */
public interface LoadingHandler {

    void showLoading(String message);

    void hideLoading(String message, boolean success);


}
